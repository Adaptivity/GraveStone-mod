/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gravestone.core.compatibility;

import cpw.mods.fml.common.Loader;
import forestry.api.core.BlockInterface;
import gravestone.GraveStoneLogger;
import gravestone.config.GraveStoneConfig;
import gravestone.core.GSBlock;
import gravestone.core.GSBiomes;
import gravestone.core.GSEntity;
import gravestone.core.GSItem;
import gravestone.core.GSMobSpawn;
import gravestone.core.GSReciepes;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class GSCompatibility {

    private static GSCompatibility instance;

    private GSCompatibility() {
        instance = this;
    }

    public static GSCompatibility getInstance() {
        return (instance == null) ? new GSCompatibility() : instance;
    }
    public static final short BATTLEGEAR_FIRST_SLOT = 150;
    public static final short BATTLEGEAR_LAST_SLOT = 155;
    private static boolean isMoCreaturesInstalled = false;
    private static boolean isHighlandsInstalled = false;
    private static boolean isBiomesOPlentyInstalled = false;
    private static boolean isExtrabiomesXLInstalled = false;
    private static boolean isBattlegearInstalled = false;
    private static boolean isArsMagicaInstalled = false;
    // mo_creatures mobs classes
    public static final String MO_CREATURES_S_SKELETON = "drzhark.mocreatures.entity.monster.MoCEntitySilverSkeleton";
    public static final String MO_CREATURES_WRAITH = "drzhark.mocreatures.entity.monster.MoCEntityWraith";
    public static final String MO_CREATURES_F_WRAITH = "drzhark.mocreatures.entity.monster.MoCEntityFlameWraith";
    public static final String MO_CREATURES_SCORPIONS = "drzhark.mocreatures.entity.monster.MoCEntityScorpion";

    public static boolean isMoCreaturesInstalled() {
        return isMoCreaturesInstalled;
    }

    public static boolean isHighlandsInstalled() {
        return isHighlandsInstalled;
    }

    public static boolean isBiomesOPlentyInstalled() {
        return isBiomesOPlentyInstalled;
    }

    public static boolean isExtrabiomesXLInstalled() {
        return isExtrabiomesXLInstalled;
    }

    public static boolean isBattlegearInstalled() {
        return isBattlegearInstalled;
    }

    public static boolean isArsMagicaInstalled() {
        return isArsMagicaInstalled;
    }

    public void checkMods() {

        // adding foreign mobs
        if (Loader.isModLoaded("MoCreatures")) {
            isMoCreaturesInstalled = true;
            if (GraveStoneConfig.spawnMoCreaturesMobs) {
                GSMobSpawn.addMoCreaturesMobs();
            }
        }

        //if (Loader.isModLoaded("TwilightForest")) {
            //isTwilightForestInstalled = true;
        //}

        // adding foreign bioms
        if (Loader.isModLoaded("Highlands")) {
            isHighlandsInstalled = true;
            GSBiomes.loadHighlandsBiomes();
            GSBiomes.addHighlandsBiomes();
        }

        if (Loader.isModLoaded("BiomesOPlenty")) {
            isBiomesOPlentyInstalled = true;
            GSBiomes.loadBiomsOPlentyBiomes();
            GSBiomes.addBiomsOPlentyBiomes();
        }

        if (Loader.isModLoaded("ExtrabiomesXL")) {
            isExtrabiomesXLInstalled = true;
            GSBiomes.addExtrabiomsXLBiomes();
        }

        if (Loader.isModLoaded("battlegear2")) {
            isBattlegearInstalled = true;
        }

        if (Loader.isModLoaded("arsmagica2")) {
            isArsMagicaInstalled = true;
        }

        if (Loader.isModLoaded("Thaumcraft")) {
            initThaumCraft();
            GSReciepes.addSkullCandleReciepes(ItemApi.getBlock("blockCandle", 0));
        }
        
        if (Loader.isModLoaded("Forestry")) {
            GSReciepes.addSkullCandleReciepes(BlockInterface.getBlock("candle"));
        }
    }

    public static boolean hasSoulbound(ItemStack stack) {
        Map enchantments = EnchantmentHelper.getEnchantments(stack);
        for (Object id : enchantments.keySet()) {
            Enchantment ench = Enchantment.enchantmentsList[((Integer) id).shortValue()];
            if (ench.getClass().getName().equals("am2.enchantments.EnchantmentSoulbound")) {
                return true;
            }
        }
        return false;
    }

    private void initThaumCraft() {
        try {
            ThaumcraftApi.registerObjectTag(GSBlock.graveStone.blockID, -1, new AspectList().add(Aspect.SOUL, 3)
                    .add(Aspect.DEATH, 3).add(Aspect.UNDEAD, 3));
            ThaumcraftApi.registerObjectTag(GSBlock.memorial.blockID, -1, new AspectList().add(Aspect.SOUL, 10)
                    .add(Aspect.DEATH, 10).add(Aspect.UNDEAD, 10));
            // trap
            ThaumcraftApi.registerObjectTag(GSBlock.trap.blockID, 0, new AspectList().add(Aspect.FIRE, 1).add(Aspect.STONE, 2)
                    .add(Aspect.MECHANISM, 5).add(Aspect.DARKNESS, 5).add(Aspect.SOUL, 5).add(Aspect.MAGIC, 5));
            ThaumcraftApi.registerObjectTag(GSBlock.trap.blockID, 1, new AspectList().add(Aspect.STONE, 2).add(Aspect.MECHANISM, 5)
                    .add(Aspect.WEATHER, 5).add(Aspect.SOUL, 5).add(Aspect.MAGIC, 5));
            // spawner
            ThaumcraftApi.registerObjectTag(GSBlock.witherSpawner.blockID, 0, new AspectList().add(Aspect.MAGIC, 20)
                    .add(Aspect.UNDEAD, 20).add(Aspect.SOUL, 20).add(Aspect.DARKNESS, 20).add(Aspect.ELDRITCH, 20));
            // bone blocks
            ThaumcraftApi.registerObjectTag(GSBlock.boneBlock.blockID, 0, new AspectList().add(Aspect.DEATH, 9).add(Aspect.FLESH, 9));
            ThaumcraftApi.registerObjectTag(GSBlock.boneBlock.blockID, 1, new AspectList().add(Aspect.DEATH, 9).add(Aspect.FLESH, 9)
                    .add(Aspect.UNDEAD, 9).add(Aspect.SOUL, 9));
            ThaumcraftApi.registerObjectTag(GSBlock.boneSlab.blockID, -1, new AspectList().add(Aspect.DEATH, 4).add(Aspect.FLESH, 4));
            ThaumcraftApi.registerObjectTag(GSBlock.boneStairs.blockID, -1, new AspectList().add(Aspect.DEATH, 6).add(Aspect.FLESH, 6));
            // haunted chest
            ThaumcraftApi.registerObjectTag(GSBlock.hauntedChest.blockID, -1, new AspectList().add(Aspect.SOUL, 5).add(Aspect.MAGIC, 5)
                    .add(Aspect.TREE, 3).add(Aspect.VOID, 4));
            // skull candle
            ThaumcraftApi.registerObjectTag(GSBlock.skullCandle.blockID, -1, new AspectList().add(Aspect.SOUL, 4).add(Aspect.DEATH, 4)
                    .add(Aspect.UNDEAD, 4).add(Aspect.LIGHT, 4));
            // items
            ThaumcraftApi.registerObjectTag(GSItem.chisel.itemID, 0, new AspectList().add(Aspect.TOOL, 2));
            
            // entity
            ThaumcraftApi.registerEntityTag(GSEntity.ZOMBIE_DOG_NAME, new AspectList().add(Aspect.BEAST, 2)
                    .add(Aspect.UNDEAD, 2).add(Aspect.EARTH, 2));
            ThaumcraftApi.registerEntityTag(GSEntity.ZOMBIE_CAT_NAME, new AspectList().add(Aspect.BEAST, 2)
                    .add(Aspect.UNDEAD, 2).add(Aspect.ENTROPY, 1));
            ThaumcraftApi.registerEntityTag(GSEntity.SKEKETON_DOG_NAME, new AspectList().add(Aspect.BEAST, 1)
                    .add(Aspect.UNDEAD, 3).add(Aspect.EARTH, 1));
            ThaumcraftApi.registerEntityTag(GSEntity.SKEKETON_CAT_NAME, new AspectList().add(Aspect.BEAST, 1)
                    .add(Aspect.UNDEAD, 3).add(Aspect.ENTROPY, 1));
            // Crawlers
            ThaumcraftApi.registerEntityTag(GSEntity.SKULL_CRAWLER_NAME, new AspectList().add(Aspect.SOUL, 4).add(Aspect.DEATH, 4)
                    .add(Aspect.UNDEAD, 4));
            ThaumcraftApi.registerEntityTag(GSEntity.WITHER_SKULL_CRAWLER_NAME, new AspectList().add(Aspect.SOUL, 4).add(Aspect.DEATH, 4)
                    .add(Aspect.UNDEAD, 4).add(Aspect.DARKNESS, 4));
            ThaumcraftApi.registerEntityTag(GSEntity.ZOMBIE_SKULL_CRAWLER_NAME, new AspectList().add(Aspect.SOUL, 4).add(Aspect.DEATH, 4)
                    .add(Aspect.UNDEAD, 4).add(Aspect.FLESH, 4));
        } catch (Exception e) {
            GraveStoneLogger.logError("Error in thaumcraft integration");
            e.printStackTrace();
        }
    }
}
