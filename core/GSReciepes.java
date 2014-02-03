
package gravestone.core;

import cpw.mods.fml.common.registry.GameRegistry;
import gravestone.block.GraveStoneHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class GSReciepes {

    private GSReciepes() {
        
    }

    public static void registration() {
        // chisel reciep
        GameRegistry.addRecipe(new ItemStack(GSItem.chisel), "   ", "s  ", " i ", 's', Item.stick, 'i', Item.ingotIron);
        // bone blocks
        GameRegistry.addRecipe(new ItemStack(GSBlock.boneBlock, 1, 0), "xxx", "xxx", "xxx", 'x', Item.bone);
        GameRegistry.addRecipe(new ItemStack(GSBlock.boneBlock, 9, 1), "xxx", "xsx", "xxx", 'x', GSBlock.boneBlock, 's', new ItemStack(Item.skull, 1, 0));
        GameRegistry.addRecipe(new ItemStack(GSBlock.boneBlock), "x", "x", 'x', GSBlock.boneSlab);
        GameRegistry.addRecipe(new ItemStack(GSBlock.boneSlab, 6), "xxx", 'x', GSBlock.boneBlock);
        GameRegistry.addRecipe(new ItemStack(GSBlock.boneStairs, 4), "x  ", "xx ", "xxx", 'x', GSBlock.boneBlock);
        // Bone block to bones
        GameRegistry.addRecipe(new ItemStack(Item.bone, 9), "x", 'x', GSBlock.boneBlock);
        
        // graves
        GameRegistry.addRecipe(getStackWithNTB(GSBlock.graveStone, (byte) 0, "GraveType"), "sc", 's', Block.stone, 'c', GSItem.chisel);
        GameRegistry.addRecipe(getStackWithNTB(GSBlock.graveStone, (byte) 1, "GraveType"), " c", "s ", 's', Block.stone, 'c', GSItem.chisel);
        GameRegistry.addRecipe(getStackWithNTB(GSBlock.graveStone, (byte) 2, "GraveType"), "c", "s", 's', Block.stone, 'c', GSItem.chisel);
        // pet graves
        GameRegistry.addRecipe(getStackWithNTB(GSBlock.graveStone, (byte) 3, "GraveType"), " c", "  ", "s ", 's', Block.stone, 'c', GSItem.chisel);
        GameRegistry.addRecipe(getStackWithNTB(GSBlock.graveStone, (byte) 4, "GraveType"), " c", "  ", " s", 's', Block.stone, 'c', GSItem.chisel);
        // sword graves
        GameRegistry.addRecipe(getSwordGravestone(GSBlock.graveStone, (byte) 5), "sc", 's', Item.swordWood, 'c', GSItem.chisel);
        GameRegistry.addRecipe(getSwordGravestone(GSBlock.graveStone, (byte) 6), "sc", 's', Item.swordStone, 'c', GSItem.chisel);
        GameRegistry.addRecipe(getSwordGravestone(GSBlock.graveStone, (byte) 7), "sc", 's', Item.swordIron, 'c', GSItem.chisel);
        GameRegistry.addRecipe(getSwordGravestone(GSBlock.graveStone, (byte) 8), "sc", 's', Item.swordGold, 'c', GSItem.chisel);
        GameRegistry.addRecipe(getSwordGravestone(GSBlock.graveStone, (byte) 9), "sc", 's', Item.swordDiamond, 'c', GSItem.chisel);
    }
    
    private static ItemStack getStackWithNTB(Block block, byte graveType, String ntbName) {
        ItemStack stack = new ItemStack(block, 1, 0);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte(ntbName, graveType);
        stack.setTagCompound(nbt);
        return stack;
    }
    
    private static ItemStack getSwordGravestone(Block block, byte graveType) {
        ItemStack stack = new ItemStack(block, 1, 0);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("GraveType", graveType);
        nbt.setByte("SwordType", GraveStoneHelper.graveTypeToSwordType(graveType));
        stack.setTagCompound(nbt);
        return stack;
    }
}
