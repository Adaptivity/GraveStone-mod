package gravestone.core.compatibility;

import gravestone.GraveStoneLogger;
import gravestone.core.GSBlock;
import gravestone.core.GSEntity;
import gravestone.core.GSItem;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class GSCompatibilityThaumcraft {

    private GSCompatibilityThaumcraft() {

    }

    public static void addAspects() {
        try {
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.graveStone), new int[]{-1}, new AspectList().add(Aspect.SOUL, 3)
                    .add(Aspect.DEATH, 3).add(Aspect.UNDEAD, 3));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.memorial), new int[]{-1}, new AspectList().add(Aspect.SOUL, 10)
                    .add(Aspect.DEATH, 10).add(Aspect.UNDEAD, 10));
            // trap

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Aspect.STONE
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.trap), new int[]{0}, new AspectList().add(Aspect.FIRE, 1)
                    .add(Aspect.MECHANISM, 5).add(Aspect.DARKNESS, 5).add(Aspect.SOUL, 5).add(Aspect.MAGIC, 5));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.trap), new int[]{1}, new AspectList().add(Aspect.MECHANISM, 5)
                    .add(Aspect.WEATHER, 5).add(Aspect.SOUL, 5).add(Aspect.MAGIC, 5));
            // spawner
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.spawner), new int[]{0}, new AspectList().add(Aspect.MAGIC, 20)
                    .add(Aspect.UNDEAD, 20).add(Aspect.SOUL, 20).add(Aspect.DARKNESS, 20).add(Aspect.ELDRITCH, 20));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.spawner), new int[]{1}, new AspectList().add(Aspect.MAGIC, 20)
                    .add(Aspect.UNDEAD, 20).add(Aspect.SOUL, 20).add(Aspect.ELDRITCH, 20));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.spawner), new int[]{2}, new AspectList().add(Aspect.MAGIC, 20)
                    .add(Aspect.UNDEAD, 20).add(Aspect.SOUL, 20).add(Aspect.ELDRITCH, 20));
            // bone blocks
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.boneBlock), new int[]{0}, new AspectList().add(Aspect.DEATH, 9).add(Aspect.FLESH, 9));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.boneBlock), new int[]{1}, new AspectList().add(Aspect.DEATH, 9).add(Aspect.FLESH, 9)
                    .add(Aspect.UNDEAD, 9).add(Aspect.SOUL, 9));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.boneBlock), new int[]{2}, new AspectList().add(Aspect.DEATH, 9).add(Aspect.FLESH, 9)
                    .add(Aspect.TRAP, 2).add(Aspect.UNDEAD, 5));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.boneBlock), new int[]{3}, new AspectList().add(Aspect.DEATH, 9).add(Aspect.FLESH, 9)
                    .add(Aspect.UNDEAD, 9).add(Aspect.SOUL, 9).add(Aspect.TRAP, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.boneSlab), new int[]{-1}, new AspectList().add(Aspect.DEATH, 4).add(Aspect.FLESH, 4));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.boneStairs), new int[]{-1}, new AspectList().add(Aspect.DEATH, 6).add(Aspect.FLESH, 6));
            // haunted chest
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.hauntedChest), new int[]{-1}, new AspectList().add(Aspect.SOUL, 5).add(Aspect.MAGIC, 5)
                    .add(Aspect.TREE, 3).add(Aspect.VOID, 4));
            // candle
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.candle), new int[]{0}, new AspectList().add(Aspect.LIGHT, 4));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.skullCandle), new int[]{0}, new AspectList().add(Aspect.SOUL, 4).add(Aspect.DEATH, 4)
                    .add(Aspect.UNDEAD, 4).add(Aspect.LIGHT, 4));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.skullCandle), new int[]{1}, new AspectList().add(Aspect.SOUL, 4).add(Aspect.DEATH, 4)
                    .add(Aspect.UNDEAD, 4).add(Aspect.LIGHT, 4).add(Aspect.POISON, 4));
            ThaumcraftApi.registerObjectTag(new ItemStack(GSBlock.skullCandle), new int[]{2}, new AspectList().add(Aspect.SOUL, 4).add(Aspect.DEATH, 4)
                    .add(Aspect.FLESH, 4).add(Aspect.LIGHT, 4));

            // items
            ThaumcraftApi.registerObjectTag(new ItemStack(GSItem.chisel), new int[]{0}, new AspectList().add(Aspect.TOOL, 2));

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
            ThaumcraftApi.registerEntityTag(GSEntity.SKULL_CRAWLER_NAME, new AspectList().add(Aspect.DEATH, 2).add(Aspect.UNDEAD, 2));
            ThaumcraftApi.registerEntityTag(GSEntity.WITHER_SKULL_CRAWLER_NAME, new AspectList().add(Aspect.DEATH, 2)
                    .add(Aspect.UNDEAD, 2).add(Aspect.DARKNESS, 2));
            ThaumcraftApi.registerEntityTag(GSEntity.ZOMBIE_SKULL_CRAWLER_NAME, new AspectList().add(Aspect.DEATH, 2).add(Aspect.UNDEAD, 2));
        } catch (Exception e) {
            GraveStoneLogger.logError("Error in thaumcraft integration");
            e.printStackTrace();
        }
    }

}
