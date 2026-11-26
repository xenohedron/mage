package mage.cards;

import mage.ObjectColor;
import mage.abilities.Abilities;
import mage.abilities.Ability;
import mage.abilities.PlayLandAbility;
import mage.abilities.SpellAbility;
import mage.constants.*;
import mage.game.Game;

import java.util.UUID;

public class TransformingDoubleFacedCardHalf extends DoubleFacedCardHalf {

    public TransformingDoubleFacedCardHalf(
            UUID ownerId, CardSetInfo setInfo,
            SuperType[] cardSuperTypes, CardType[] cardTypes, SubType[] cardSubTypes,
            String costs, TransformingDoubleFacedCard parentCard, SpellAbilityType spellAbilityType
    ) {
        super(ownerId, setInfo, cardSuperTypes, cardTypes, cardSubTypes, costs, parentCard, spellAbilityType);
    }

    protected TransformingDoubleFacedCardHalf(final TransformingDoubleFacedCardHalf card) {
        super(card);
        this.parentCard = card.parentCard;
    }

    public TransformingDoubleFacedCardHalf(
            UUID ownerId, CardSetInfo setInfo,
            SuperType[] superTypesRight, CardType[] typesRight, SubType[] subTypesRight, String colorRight, TransformingDoubleFacedCard parentCard) {
        super(ownerId, setInfo, superTypesRight, typesRight, subTypesRight, "", parentCard, SpellAbilityType.TRANSFORMED_RIGHT);
        this.getColor().setColor(new ObjectColor(colorRight));
    }

    @Override
    public boolean cast(Game game, Zone fromZone, SpellAbility ability, UUID controllerId) {
        if (ability.getSpellAbilityCastMode() == SpellAbilityCastMode.DISTURB && !isBackSide()) {
            return getOtherSide().cast(game, fromZone, ability, controllerId);
        }
        return super.cast(game, fromZone, ability, controllerId);
    }

    @Override
    public TransformingDoubleFacedCardHalf copy() {
        return new TransformingDoubleFacedCardHalf(this);
    }

    @Override
    public TransformingDoubleFacedCard getParentCard() {
        return (TransformingDoubleFacedCard) parentCard;
    }

    @Override
    public Abilities<Ability> getAbilities() {
        final Abilities<Ability> all = super.getAbilities();

        // lands on the back of transforming double-faced cards should not be playable
        if (this.isBackSide()) {
            all.removeIf(ability -> ability instanceof PlayLandAbility);
        }

        return all;
    }

    @Override
    public boolean hasAbility(Ability ability, Game game) {
        // 712.8a While a double-faced card is outside the game or in a zone other than the battlefield or stack,
        // it has only the characteristics of its front face.
        // The back half shares the main card's zone there, so without this its abilities (e.g. disturb's
        // "exile it instead") would work from the library or hand.
        if (isBackSide()) {
            Zone zone = game.getState().getZone(getId());
            if (zone != Zone.STACK && zone != Zone.BATTLEFIELD) {
                return false;
            }
        }
        return super.hasAbility(ability, game);
    }
}
