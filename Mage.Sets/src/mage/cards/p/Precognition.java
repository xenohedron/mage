package mage.cards.p;

import mage.abilities.Ability;
import mage.abilities.triggers.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.cards.*;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.players.Player;
import mage.target.common.TargetOpponent;

import java.util.UUID;

/**
 * @author fireshoes
 */
public final class Precognition extends CardImpl {

    public Precognition(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{4}{U}");

        // At the beginning of your upkeep, you may look at the top card of target opponent's library. If you do, you may put that card on the bottom of that player's library.
        Ability ability = new BeginningOfUpkeepTriggeredAbility(new PrecognitionEffect(), true);
        ability.addTarget(new TargetOpponent());
        this.addAbility(ability);
    }

    private Precognition(final Precognition card) {
        super(card);
    }

    @Override
    public Precognition copy() {
        return new Precognition(this);
    }
}

class PrecognitionEffect extends OneShotEffect {

    PrecognitionEffect() {
        super(Outcome.Detriment);
        staticText = "you may look at the top card of target opponent's library. You may put that card on the bottom of that player's library";
    }

    private PrecognitionEffect(final PrecognitionEffect effect) {
        super(effect);
    }

    @Override
    public PrecognitionEffect copy() {
        return new PrecognitionEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Player player = game.getPlayer(source.getFirstTarget());
        if (controller != null && player != null) {
            Card card = player.getLibrary().getFromTop(game);
            if (card != null) {
                Cards cards = new CardsImpl(card);
                controller.lookAtCards("Precognition", cards, game);
                if (controller.chooseUse(outcome, "Put that card on the bottom of its owner's library?", source, game)) {
                    controller.moveCardToLibraryWithInfo(card, source, game, Zone.LIBRARY, false, false);
                } else {
                    game.informPlayers(controller.getLogName() + " puts the card back on top of the library.");
                }
                return true;
            }
        }
        return false;
    }

}
