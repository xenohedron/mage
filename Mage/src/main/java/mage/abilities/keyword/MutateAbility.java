package mage.abilities.keyword;

import mage.abilities.Ability;
import mage.abilities.SpellAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.dynamicvalue.common.SourceMutatedCount;
import mage.abilities.hint.ValueHint;
import mage.cards.Card;
import mage.constants.*;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.Predicates;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.common.TargetCreaturePermanent;

import java.util.*;

/**
 * @author NinthWorld
 * <p>
 * <p>
 * 702.140. Mutate
 * <p>
 * 702.140a Mutate appears on some creature cards. It represents a static
 * ability that functions while the spell with mutate is on the stack.
 * “Mutate [cost]” means “You may pay [cost] rather than pay this spell’s
 * mana cost. If you do, it becomes a mutating creature spell and targets
 * a non-Human creature with the same owner as this spell.” Casting a
 * spell using its mutate ability follows the rules for paying alternative
 * costs (see 601.2b and 601.2f–h).
 * <p>
 * 702.140b As a mutating creature spell begins resolving, if its target is
 * illegal, it ceases to be a mutating creature spell and continues resolving
 * as a creature spell and will be put onto the battlefield under the control
 * of the spell’s controller.
 * <p>
 * 702.140c As a mutating creature spell resolves, if its target is legal, it
 * doesn’t enter the battlefield. Rather, it merges with the target creature
 * and becomes one object represented by more than one card or token (see rule
 * 723, “Merging with Permanents”). The spell’s controller chooses whether the
 * spell is put on top of the creature or on the bottom. The resulting permanent
 * is a mutated permanent.
 * <p>
 * 702.140d An ability that triggers whenever a creature mutates triggers when
 * a spell merges with a creature as a result of a resolving mutating creature
 * spell.
 * <p>
 * 702.140e A mutated permanent has all abilities of each card and token that
 * represents it. Its other characteristics are derived from the topmost card
 * or token.
 * <p>
 * 702.140f Any effect that refers to or modifies the mutating creature spell
 * refers to or modifies the mutated permanent it merges with as it resolves.
 */
public class MutateAbility extends SpellAbility {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("non-Human creature you own");

    static {
        filter.add(TargetController.YOU.getOwnerPredicate());
        filter.add(Predicates.not(SubType.HUMAN.getPredicate()));
    }

    protected static final String MUTATE_KEYWORD = "Mutate";
    protected static final String MUTATE_REMINDER = " <i>(If you cast this spell for its mutate cost, "
            + "put it over or under target non-Human creature you own. "
            + "They mutate into the creature on top plus all abilities from under it.)</i>";

    public MutateAbility(Card card, String mutateCosts) {
        super(new ManaCostsImpl<>(mutateCosts), card.getName(), Zone.HAND,
                SpellAbilityType.BASE_ALTERNATE, SpellAbilityCastMode.MUTATE);
        this.timing = TimingRule.SORCERY;
        this.addTarget(new TargetCreaturePermanent(filter));
        this.addHint(new MutateCountHint());
    }

    private MutateAbility(final MutateAbility ability) {
        super(ability);
    }

    @Override
    public MutateAbility copy() {
        return new MutateAbility(this);
    }

    @Override
    public String getRule(boolean all) {
        return getRule();
    }

    @Override
    public String getRule() {
        return MUTATE_KEYWORD + " " + getManaCostsToPay().getText() + MUTATE_REMINDER;
    }

    public static Set<Card> getAllCardsFromPermanentLeftBattlefield(Permanent target) {
        return getAllCardsFromPermanentsLeftBattlefield(Collections.singletonList(target));
    }

    public static Set<Card> getAllCardsFromPermanentsLeftBattlefield(Collection<Permanent> targets) {
        Set<Card> toReturn = new LinkedHashSet<>();
        targets.forEach(card -> {
            toReturn.add(card.getMainCard());
            card.getMutatedOverList().stream().map(Card::getMainCard).forEach(toReturn::add);
        });
        return toReturn;
    }

}

class MutateCountHint extends ValueHint {

    MutateCountHint() {
        super("Number of times {this} has mutated", SourceMutatedCount.instance);
    }

    @Override
    public String getText(Game game, Ability ability) {
        if (game.getPermanent(ability.getSourceId()) == null) {
            return ""; // only visible on battlefield
        }
        return super.getText(game, ability);
    }

    private MutateCountHint(final MutateCountHint hint) {
        super(hint);
    }

    @Override
    public MutateCountHint copy() {
        return new MutateCountHint(this);
    }

}
