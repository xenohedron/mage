package mage.cards.s;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.AttachEffect;
import mage.abilities.effects.common.RegenerateSourceEffect;
import mage.abilities.effects.common.continuous.BoostEnchantedEffect;
import mage.abilities.effects.common.continuous.GainAbilityAttachedEffect;
import mage.abilities.keyword.EnchantAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.AttachmentType;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.target.TargetPermanent;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author North
 */
public final class SkeletalGrimace extends CardImpl {

    public SkeletalGrimace(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ENCHANTMENT},"{1}{B}");
        this.subtype.add(SubType.AURA);

        // Enchant creature
        TargetPermanent auraTarget = new TargetCreaturePermanent();
        this.getSpellAbility().addTarget(auraTarget);
        this.getSpellAbility().addEffect(new AttachEffect(Outcome.BoostCreature));
        Ability ability = new EnchantAbility(auraTarget);
        this.addAbility(ability);

        // Enchanted creature gets +1/+1 and has "{B}: Regenerate this creature."
        Ability staticAbility = new SimpleStaticAbility(new BoostEnchantedEffect(1, 1));
        Ability gainedAbility = new SimpleActivatedAbility(new RegenerateSourceEffect(), new ManaCostsImpl<>("{B}"));
        staticAbility.addEffect(new GainAbilityAttachedEffect(gainedAbility, AttachmentType.AURA)
                .setText("and has \"{B}: Regenerate this creature.\""));
        this.addAbility(staticAbility);
    }

    private SkeletalGrimace(final SkeletalGrimace card) {
        super(card);
    }

    @Override
    public SkeletalGrimace copy() {
        return new SkeletalGrimace(this);
    }
}
