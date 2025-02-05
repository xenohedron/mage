package mage.cards.s;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.triggers.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.effects.RestrictionEffect;
import mage.abilities.effects.common.ExileTargetEffect;
import mage.abilities.effects.common.LoseLifeSourceControllerEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.counters.CounterType;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.mageobject.AnotherPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.common.TargetCreaturePermanent;

import java.util.UUID;

/**
 * @author Saga
 */
public final class ShaukuEndbringer extends CardImpl {

    public ShaukuEndbringer(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{5}{B}{B}");
        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.VAMPIRE);
        this.power = new MageInt(5);
        this.toughness = new MageInt(5);

        this.addAbility(FlyingAbility.getInstance());

        // Shauku, Endbringer can't attack if there's another creature on the battlefield.
        this.addAbility(new SimpleStaticAbility(new ShaukuEndbringerEffect()));

        // At the beginning of your upkeep, you lose 3 life.
        this.addAbility(new BeginningOfUpkeepTriggeredAbility(new LoseLifeSourceControllerEffect(3)));

        // {T}: Exile target creature and put a +1/+1 counter on Shauku.
        Ability ability = new SimpleActivatedAbility(new ExileTargetEffect(), new TapSourceCost());
        ability.addEffect(new AddCountersSourceEffect(CounterType.P1P1.createInstance()).concatBy("and"));
        ability.addTarget(new TargetCreaturePermanent());
        this.addAbility(ability);
    }

    private ShaukuEndbringer(final ShaukuEndbringer card) {
        super(card);
    }

    @Override
    public ShaukuEndbringer copy() {
        return new ShaukuEndbringer(this);
    }
}

class ShaukuEndbringerEffect extends RestrictionEffect {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("another creature");

    static {
        filter.add(AnotherPredicate.instance);
    }

    public ShaukuEndbringerEffect() {
        super(Duration.WhileOnBattlefield);
        staticText = "{this} can't attack if there's another creature on the battlefield.";
    }

    private ShaukuEndbringerEffect(final ShaukuEndbringerEffect effect) {
        super(effect);
    }

    @Override
    public ShaukuEndbringerEffect copy() {
        return new ShaukuEndbringerEffect(this);
    }

    @Override
    public boolean canAttack(Game game, boolean canUseChooseDialogs) {
        return false;
    }

    @Override
    public boolean applies(Permanent permanent, Ability source, Game game) {
        return permanent.getId().equals(source.getSourceId()) &&
                game.getBattlefield().count(filter, source.getControllerId(), source, game) > 0;
    }
}
