package mage.abilities.keyword;

import mage.abilities.Ability;
import mage.abilities.ActivatedAbilityImpl;
import mage.abilities.costs.CostAdjuster;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.hint.common.MonstrousHint;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.util.CardUtil;


/**
 * Monstrosity
 * <p>
 * 701.28. Monstrosity
 * <p>
 * 701.28a “Monstrosity N” means “If this permanent isn't monstrous, put N +1/+1 counters on it
 * and it becomes monstrous.” Monstrous is a condition of that permanent that can be
 * referred to by other abilities.
 * <p>
 * 701.28b If a permanent's ability instructs a player to “monstrosity X,” other abilities of
 * that permanent may also refer to X. The value of X in those abilities is equal to
 * the value of X as that permanent became monstrous.
 * <p>
 * * Once a creature becomes monstrous, it can't become monstrous again. If the creature
 * is already monstrous when the monstrosity ability resolves, nothing happens.
 * <p>
 * * Monstrous isn't an ability that a creature has. It's just something true about that
 * creature. If the creature stops being a creature or loses its abilities, it will
 * continue to be monstrous.
 * <p>
 * * An ability that triggers when a creature becomes monstrous won't trigger if that creature
 * isn't on the battlefield when its monstrosity ability resolves.
 *
 * @author LevelX2
 */

public class MonstrosityAbility extends ActivatedAbilityImpl {

    private final int monstrosityValue;

    public MonstrosityAbility(String manaString, int monstrosityValue) {
        this(manaString, monstrosityValue, null, "");
    }

    /**
     * @param manaString
     * @param monstrosityValue use Integer.MAX_VALUE for monstrosity X.
     * @param costAdjuster
     * @param costAdjusterText Clarifies the cost adjusting condition(s).
     */
    public MonstrosityAbility(String manaString, int monstrosityValue, CostAdjuster costAdjuster, String costAdjusterText) {
        super(Zone.BATTLEFIELD, new BecomeMonstrousSourceEffect(monstrosityValue, costAdjusterText), new ManaCostsImpl<>(manaString));
        this.monstrosityValue = monstrosityValue;
        this.addHint(MonstrousHint.instance);
        setCostAdjuster(costAdjuster);
    }

    protected MonstrosityAbility(final MonstrosityAbility ability) {
        super(ability);
        this.monstrosityValue = ability.monstrosityValue;
    }

    @Override
    public MonstrosityAbility copy() {
        return new MonstrosityAbility(this);
    }

    public int getMonstrosityValue() {
        return monstrosityValue;
    }
}


class BecomeMonstrousSourceEffect extends OneShotEffect {

    public BecomeMonstrousSourceEffect(int monstrosityValue) {
        this(monstrosityValue, "");
    }

    public BecomeMonstrousSourceEffect(int monstrosityValue, String costAdjusterText) {
        super(Outcome.BoostCreature);
        this.staticText = setText(monstrosityValue, costAdjusterText);
    }

    protected BecomeMonstrousSourceEffect(final BecomeMonstrousSourceEffect effect) {
        super(effect);
    }

    @Override
    public BecomeMonstrousSourceEffect copy() {
        return new BecomeMonstrousSourceEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = source.getSourcePermanentIfItStillExists(game);
        if (permanent == null || permanent.isMonstrous()) {
            return false;
        }
        int monstrosityValue = ((MonstrosityAbility) source).getMonstrosityValue();
        // handle monstrosity = X
        if (monstrosityValue == Integer.MAX_VALUE) {
            monstrosityValue = CardUtil.getSourceCostsTag(game, source, "X", 0);
        }
        permanent.addCounters(
                CounterType.P1P1.createInstance(monstrosityValue),
                source.getControllerId(), source, game
        );
        permanent.setMonstrous(true);
        game.fireEvent(GameEvent.getEvent(
                GameEvent.EventType.BECOMES_MONSTROUS, source.getSourceId(),
                source, source.getControllerId(), monstrosityValue
        ));
        return true;
    }

    private String setText(int monstrosityValue, String costAdjusterText) {
        return "Monstrosity " + (monstrosityValue == Integer.MAX_VALUE ? "X" : monstrosityValue) +
                ". " + costAdjusterText + "<i>(If this creature isn't monstrous, put " +
                (monstrosityValue == Integer.MAX_VALUE ? "X" : CardUtil.numberToText(monstrosityValue)) +
                " +1/+1 counters on it and it becomes monstrous.)</i>";
    }
}
