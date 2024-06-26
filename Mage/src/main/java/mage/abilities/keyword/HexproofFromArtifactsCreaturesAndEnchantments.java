package mage.abilities.keyword;

import mage.MageObject;
import mage.abilities.Ability;
import mage.game.Game;

import java.io.ObjectStreamException;

/**
 * Hexproof from artifacts, creatures, and enchantments
 *
 * @author TheElk801
 */
public class HexproofFromArtifactsCreaturesAndEnchantments extends HexproofBaseAbility {

    private static final HexproofFromArtifactsCreaturesAndEnchantments instance;

    static {
        instance = new HexproofFromArtifactsCreaturesAndEnchantments();
    }

    private Object readResolve() throws ObjectStreamException {
        return instance;
    }

    public static HexproofFromArtifactsCreaturesAndEnchantments getInstance() {
        return instance;
    }

    private HexproofFromArtifactsCreaturesAndEnchantments() {
        super();
    }

    @Override
    public boolean checkObject(MageObject sourceObject, Ability source, Game game) {
        return sourceObject.isArtifact(game) || sourceObject.isCreature(game) || sourceObject.isEnchantment(game);
    }

    @Override
    public HexproofFromArtifactsCreaturesAndEnchantments copy() {
        return instance;
    }

    @Override
    public String getRule() {
        return "hexproof from artifacts, creatures, and enchantments";
    }

    @Override
    public String getCardIconHint(Game game) {
        return "hexproof from artifacts, creatures, and enchantments";
    }
}
