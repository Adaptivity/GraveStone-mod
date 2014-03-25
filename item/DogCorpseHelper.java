package gravestone.item;

import gravestone.ModGraveStone;
import java.util.List;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class DogCorpseHelper extends CorpseHelper {

    private DogCorpseHelper() {
    }
    
    public static ItemStack getDefaultCorpse(int id, int type) {
        ItemStack corpse = new ItemStack(id, 1, type);
        NBTTagCompound nbtTag = new NBTTagCompound();
        
        nbtTag.setByte("Collar", (byte) 14);
        
        corpse.setTagCompound(nbtTag);
        return corpse;
    }
    
    public static void setNbt(EntityWolf dog, NBTTagCompound nbt) {
        setName(dog, nbt);
        nbt.setByte("Collar", (byte) dog.getCollarColor());
    }

    public static void spawnDog(World world, int x, int y, int z, NBTTagCompound nbtTag, EntityPlayer player) {
        EntityWolf wolf = new EntityWolf(world);
        setMobName(wolf, nbtTag);
        wolf.setTamed(true);
        wolf.setHealth(20);
        wolf.setOwner(player.getCommandSenderName());
        wolf.setCollarColor(nbtTag.getByte("Collar"));
        world.setEntityState(wolf, (byte) 7);
        spawnMob(wolf, world, x, y, z);
    }

    public static void addInfo(List list, NBTTagCompound nbtTag) {
        addNameInfo(list, nbtTag);
        if (hasCollar(nbtTag)) {
            list.add(getCollarStr(nbtTag));
        }
    }

    private static boolean hasCollar(NBTTagCompound nbtTag) {
        return nbtTag.hasKey("Collar");
    }

    private static String getCollarStr(NBTTagCompound nbtTag) {
        return ModGraveStone.proxy.getLocalizedString("item.corpse.collar") + " " + 
                ModGraveStone.proxy.getLocalizedString(getCollar(nbtTag.getByte("Collar")));
    }
    
    private static String getCollar(int type) {
        switch (type) {
            case 0:
                return "item.corpse.collar.white";
            case 1:
                return "item.corpse.collar.orange";
            case 2:
                return "item.corpse.collar.purple";
            case 3:
                return "item.corpse.collar.azure";
            case 4:
                return "item.corpse.collar.yellow";
            case 5:
                return "item.corpse.collar.lime";
            case 6:
                return "item.corpse.collar.pink";
            case 7:
                return "item.corpse.collar.grey";
            case 8:
                return "item.corpse.collar.light_grey";
            case 9:
                return "item.corpse.collar.turquoise";//бирюзовый
            case 10:
                return "item.corpse.collar.violet";
            case 11:
                return "item.corpse.collar.blue";
            case 12:
                return "item.corpse.collar.brown";
            case 13:
                return "item.corpse.collar.green";
            case 14:
                return "item.corpse.collar.red";
            case 15:
                return "item.corpse.collar.black";
            default:
                return "item.corpse.collar.unknown";
        }
    }
}
