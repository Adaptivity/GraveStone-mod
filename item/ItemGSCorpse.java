package gravestone.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gravestone.ModGraveStone;
import gravestone.item.enums.EnumCorpse;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ItemGSCorpse extends Item {
    
    private static Icon[] icons;
    
    public ItemGSCorpse(int id) {
        super(id);
        setCreativeTab(ModGraveStone.creativeTab);
        setUnlocalizedName("Corpse");
        this.setHasSubtypes(true);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        } else {
            CorpseHelper.addInfo(stack.getItemDamage(), list, stack.stackTagCompound);
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye
     * returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs tab, List list) {
        for (int damage = 0; damage < EnumCorpse.values().length; ++damage) {
            list.add(CorpseHelper.getDefaultCorpse(id, damage));
        }
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }
    @Override
    public String getItemDisplayName(ItemStack itemStack) {
        return EnumCorpse.getById((byte) itemStack.getItemDamage()).getName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icons = new Icon[EnumCorpse.values().length];

        for (int i = 0; i < EnumCorpse.values().length; ++i) {
            this.icons[i] = register.registerIcon(EnumCorpse.values()[i].getIcon());
        }
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage) {
        if (damage < 0 || damage >= EnumCorpse.values().length) {
            damage = 0;
        }

        return this.icons[damage];
    }
}
