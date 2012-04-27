
package GraveStone.item;

import GraveStone.mod_GraveStone;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBlockGSMemorial extends ItemBlock {

    public ItemBlockGSMemorial(int id) {
        super(id);
        setHasSubtypes(true);
        setUnlocalizedName("multiBlock");
    }

    @Override
    public int getMetadata(int damageValue) {
        return 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        byte graveType = 0;
        if (itemStack.stackTagCompound != null) {
            graveType = itemStack.stackTagCompound.getByte("GraveType");
        }
        return getUnlocalizedName() + "." + mod_GraveStone.memorial.NAMES[graveType];
    }

    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (stack.stackTagCompound.hasKey("DeathText") && !stack.stackTagCompound.getString("DeathText").equals("")) {
            list.add(stack.stackTagCompound.getString("DeathText"));
        }
    }
}
