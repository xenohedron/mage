package mage.cards.a;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.common.TapVariableTargetCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.DestroyTargetEffect;
import mage.abilities.keyword.VigilanceAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.filter.common.FilterControlledPermanent;
import mage.filter.predicate.permanent.TappedPredicate;
import mage.game.permanent.token.KnightToken;
import mage.target.common.TargetCreaturePermanent;
import mage.target.targetadjustment.PowerTargetAdjuster;

import java.util.UUID;

/**
 * @author jack-the-BOSS
 */
public final class AryelKnightOfWindgrace extends CardImpl {

    static final FilterControlledPermanent filter = new FilterControlledPermanent(SubType.KNIGHT, "untapped Knights you control");

    static {
        filter.add(TappedPredicate.UNTAPPED);
    }

    public AryelKnightOfWindgrace(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{2}{W}{B}");

        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.KNIGHT);
        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        // Vigilance
        this.addAbility(VigilanceAbility.getInstance());

        // {2}{W}, {T}: Create a 2/2 white Knight creature token with vigilance.
        Ability ability = new SimpleActivatedAbility(new CreateTokenEffect(new KnightToken()), new ManaCostsImpl<>("{2}{W}"));
        ability.addCost(new TapSourceCost());
        this.addAbility(ability);

        // {B}, {T}, Tap X untapped Knights you control: Destroy target creature with power X or less.
        //Simple costs
        ability = new SimpleActivatedAbility(new DestroyTargetEffect()
                .setText("Destroy target creature with power X or less"), new ManaCostsImpl<>("{B}"));
        ability.addCost(new TapSourceCost());
        ability.addCost(new TapVariableTargetCost(filter));
        ability.addTarget(new TargetCreaturePermanent());
        ability.setTargetAdjuster(new PowerTargetAdjuster(ComparisonType.OR_LESS));
        this.addAbility(ability);
    }

    private AryelKnightOfWindgrace(final AryelKnightOfWindgrace card) {
        super(card);
    }

    @Override
    public AryelKnightOfWindgrace copy() {
        return new AryelKnightOfWindgrace(this);
    }
}
