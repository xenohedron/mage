package mage.abilities.effects.common;

import mage.abilities.Ability;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.dynamicvalue.common.StaticValue;
import mage.abilities.effects.OneShotEffect;
import mage.constants.Outcome;
import mage.game.Game;
import mage.game.permanent.token.Token;
import mage.players.Player;
import mage.util.CardUtil;

/**
 * @author Susucr
 * <p>
 * Have the Controller of target object (permanent, or spell) create Tokens.
 */
public class CreateTokenControllerTargetEffect extends OneShotEffect {
    private final Token token;
    private final DynamicValue amount;
    private final boolean tapped;

    public CreateTokenControllerTargetEffect(Token token) {
        this(token, 1, false);
    }

    public CreateTokenControllerTargetEffect(Token token, int amount, boolean tapped) {
        this(token, StaticValue.get(amount), tapped);
    }

    public CreateTokenControllerTargetEffect(Token token, DynamicValue amount, boolean tapped) {
        super(Outcome.Neutral);
        this.token = token;
        this.amount = amount.copy();
        this.tapped = tapped;
        this.staticText = makeText();
    }

    protected CreateTokenControllerTargetEffect(final CreateTokenControllerTargetEffect effect) {
        super(effect);
        this.token = effect.token.copy();
        this.amount = effect.amount.copy();
        this.tapped = effect.tapped;
    }

    @Override
    public CreateTokenControllerTargetEffect copy() {
        return new CreateTokenControllerTargetEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controllerOfTarget = getTargetPointer().getControllerOfFirstTargetOrLKI(game, source);
        if (controllerOfTarget != null) {
            int value = amount.calculate(game, source, this);
            return token.putOntoBattlefield(value, game, source, controllerOfTarget.getId(), tapped, false);
        }
        return false;
    }

    private String makeText() {
        String text = "Its controller creates ";

        if (token.getDescription().contains(", a legendary")) {
            text += token.getDescription();
            return text;
        }

        if (amount.toString().equals("1")) {
            if (tapped) {
                text += "a tapped " + token.getDescription();
            } else {
                text += CardUtil.addArticle(token.getDescription());
            }
        } else {
            text += CardUtil.numberToText(amount.toString()) + " ";
            if (tapped) {
                text += "tapped ";
            }
            text += token.getDescription().replace("token. It has", "tokens. They have");
            if (token.getDescription().endsWith("token")) {
                text += "s";
            }
            text = text.replace("token ", "tokens ");
        }

        String message = amount.getMessage();
        if (!message.isEmpty()) {
            if (amount.toString().equals("X")) {
                text += ", where X is ";
            } else {
                text += " for each ";
            }
        }
        text += message;

        return text;
    }
}
