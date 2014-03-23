package gravestone.block;

import gravestone.block.enums.EnumGraves;
import gravestone.config.GraveStoneConfig;
import gravestone.ModGraveStone;
import gravestone.tileentity.GSGraveStoneItems;
import gravestone.tileentity.TileEntityGSGraveStone;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gravestone.GraveStoneLogger;
import gravestone.tileentity.DeathMessageInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class BlockGSGraveStone extends BlockContainer {

    private static final Random rand = new Random();
    public static final byte[] GENERATED_GRAVES = {
        (byte) EnumGraves.VERTICAL_PLATE.ordinal(),
        (byte) EnumGraves.CROSS.ordinal(),
        (byte) EnumGraves.HORISONTAL_PLATE.ordinal()
    };
    public static final byte[] PETS_GRAVES = {
        (byte) EnumGraves.DOG_STATUE.ordinal(),
        (byte) EnumGraves.CAT_STATUE.ordinal()
    };
    public static final byte[] DOG_GRAVES = {(byte) EnumGraves.DOG_STATUE.ordinal()};
    public static final byte[] CAT_GRAVES = {(byte) EnumGraves.CAT_STATUE.ordinal()};
    public static final byte[] HORSE_GRAVES = {(byte) EnumGraves.HORSE_STATUE.ordinal()};
    public static final byte[] SWORD_GRAVES = {
        (byte) EnumGraves.WOODEN_SWORD.ordinal(),
        (byte) EnumGraves.STONE_SWORD.ordinal(),
        (byte) EnumGraves.IRON_SWORD.ordinal(),
        (byte) EnumGraves.GOLDEN_SWORD.ordinal(),
        (byte) EnumGraves.DIAMOND_SWORD.ordinal()
    };
    public static final byte[] GENERATED_SWORD_GRAVES = {
        (byte) EnumGraves.WOODEN_SWORD.ordinal(),
        (byte) EnumGraves.STONE_SWORD.ordinal()
    };

    public enum EnumGraveType {

        ALL_GRAVES,
        PLAYER_GRAVES,
        PETS_GRAVES,
        DOGS_GRAVES,
        CATS_GRAVES,
        HORSE_GRAVES
    }

    public BlockGSGraveStone(int par1) {
        super(par1, Material.rock);
        this.isBlockContainer = true;
        this.setStepSound(Block.soundStoneFootstep);
        this.setUnlocalizedName("GraveStone");
        this.setHardness(4.5F);
        this.setResistance(5F);
        this.setCreativeTab(ModGraveStone.creativeTab);
        this.setTextureName("stone");
    }

    /**
     * Called when the block is placed in the world
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
        GraveStoneHelper.replaceGround(world, x, y - 1, z);
        int direction = MathHelper.floor_float(player.rotationYaw);

        if (direction < 0) {
            direction = 360 + direction;
        }

        int metadata = GraveStoneHelper.getMetadataBasedOnRotation(direction);
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
        TileEntityGSGraveStone tileEntity = (TileEntityGSGraveStone) world.getBlockTileEntity(x, y, z);

        if (tileEntity != null) {
            if (itemStack.stackTagCompound != null) {
                if (itemStack.stackTagCompound.hasKey("GraveType")) {
                    tileEntity.setGraveType(itemStack.stackTagCompound.getByte("GraveType"));
                } else {
                    tileEntity.setGraveType((byte) 0);
                }

                if (itemStack.stackTagCompound.hasKey("isLocalized") && itemStack.stackTagCompound.getBoolean("isLocalized")) {
                    tileEntity.getDeathTextComponent().setLocalized();

                    if (itemStack.stackTagCompound.hasKey("name") && itemStack.stackTagCompound.hasKey("KillerName")) {
                        tileEntity.getDeathTextComponent().setName(itemStack.stackTagCompound.getString("name"));
                        tileEntity.getDeathTextComponent().setKillerName(itemStack.stackTagCompound.getString("KillerName"));
                    }
                }

                if (itemStack.stackTagCompound.hasKey("DeathText")) {
                    tileEntity.getDeathTextComponent().setDeathText(itemStack.stackTagCompound.getString("DeathText"));
                }

                if (itemStack.stackTagCompound.hasKey("Age")) {
                    tileEntity.setAge(itemStack.stackTagCompound.getInteger("Age"));
                }

                if (itemStack.stackTagCompound.hasKey("SwordType")) {
                    byte swordType = itemStack.stackTagCompound.getByte("SwordType");
                    tileEntity.setSword(swordType);

                    if (swordType != 0) {
                        if (itemStack.stackTagCompound.hasKey("SwordDamage")) {
                            tileEntity.setDamage(itemStack.stackTagCompound.getInteger("SwordDamage"));
                        }

                        if (itemStack.stackTagCompound.hasKey("SwordName")) {
                            tileEntity.setSwordName(itemStack.stackTagCompound.getString("SwordName"));
                        }

                        if (itemStack.stackTagCompound.hasKey("SwordNBT")) {
                            tileEntity.setEnchantment(itemStack.stackTagCompound.getCompoundTag("SwordNBT"));
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified
     * coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return GraveStoneHelper.canPlaceBlockAt(world.getBlockId(x, y - 1, z));
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y,
     * z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
        int meta = access.getBlockMetadata(x, y, z);
        EnumGraves graveType;
        TileEntityGSGraveStone tileEntity = (TileEntityGSGraveStone) access.getBlockTileEntity(x, y, z);

        if (tileEntity != null) {
            graveType = tileEntity.getGraveType();
        } else {
            graveType = EnumGraves.VERTICAL_PLATE;
        }

        switch (graveType) {
            case VERTICAL_PLATE:
                switch (meta) {
                    case 0:
                        this.setBlockBounds(0.125F, 0, 0.0625F, 0.875F, 0.9375F, 0.1875F);
                        break;
                    case 1:
                        this.setBlockBounds(0.125F, 0, 0.8125F, 0.875F, 0.9375F, 0.9375F);
                        break;
                    case 2:
                        this.setBlockBounds(0.0625F, 0, 0.125F, 0.1875F, 0.9375F, 0.875F);
                        break;
                    case 3:
                        this.setBlockBounds(0.8125F, 0, 0.125F, 0.9375F, 0.9375F, 0.875F);
                        break;
                }
                break;
            case CROSS:
                switch (meta) {
                    case 0:
                        this.setBlockBounds(0.125F, 0, 0.0625F, 0.875F, 1, 0.1875F);
                        break;
                    case 1:
                        this.setBlockBounds(0.125F, 0, 0.8125F, 0.875F, 1, 0.9375F);
                        break;
                    case 2:
                        this.setBlockBounds(0.0625F, 0, 0.125F, 0.1875F, 1, 0.875F);
                        break;
                    case 3:
                        this.setBlockBounds(0.8125F, 0, 0.125F, 0.9375F, 1, 0.875F);
                        break;
                }
                break;
            case HORISONTAL_PLATE:
                switch (meta) {
                    case 0:
                        this.setBlockBounds(0.09375F, 0, 0.0625F, 0.90625F, 0.0625F, 0.9375F);
                        break;
                    case 1:
                        this.setBlockBounds(0.09375F, 0, 0.0625F, 0.90625F, 0.0625F, 0.9375F);
                        break;
                    case 2:
                        this.setBlockBounds(0.0625F, 0, 0.09375F, 0.9375F, 0.0625F, 0.90625F);
                        break;
                    case 3:
                        this.setBlockBounds(0.0625F, 0, 0.09375F, 0.9375F, 0.0625F, 0.90625F);
                        break;
                }
                break;
            case DOG_STATUE:
                switch (meta) {
                    case 0:
                        this.setBlockBounds(0.35F, 0, 0.3F, 0.6F, 0.5F, 0.9F);
                        break;
                    case 1:
                        this.setBlockBounds(0.35F, 0, 0.7F, 0.6F, 0.5F, 0.1F);
                        break;
                    case 2:
                        this.setBlockBounds(0.3F, 0, 0.35F, 0.9F, 0.5F, 0.6F);
                        break;
                    case 3:
                        this.setBlockBounds(0.7F, 0, 0.35F, 0.1F, 0.5F, 0.6F);
                        break;
                }
                break;
            case CAT_STATUE:
                switch (meta) {
                    case 0:
                        this.setBlockBounds(0.43F, 0, 0.3F, 0.57F, 0.5F, 0.75F);
                        break;
                    case 1:
                        this.setBlockBounds(0.43F, 0, 0.7F, 0.57F, 0.5F, 0.25F);
                        break;
                    case 2:
                        this.setBlockBounds(0.3F, 0, 0.43F, 0.75F, 0.5F, 0.57F);
                        break;
                    case 3:
                        this.setBlockBounds(0.7F, 0, 0.43F, 0.25F, 0.5F, 0.57F);
                        break;
                }
                break;
            case WOODEN_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case GOLDEN_SWORD:
            case DIAMOND_SWORD:
                switch (meta) {
                    case 0:
                        this.setBlockBounds(0.375F, 0, 0.4375F, 0.625F, 0.9F, 0.5625F);
                        break;
                    case 1:
                        this.setBlockBounds(0.375F, 0, 0.4375F, 0.625F, 0.9F, 0.5625F);
                        break;
                    case 2:
                        this.setBlockBounds(0.4375F, 0, 0.375F, 0.5625F, 0.9F, 0.625F);
                        break;
                    case 3:
                        this.setBlockBounds(0.4375F, 0, 0.375F, 0.5625F, 0.9F, 0.625F);
                        break;
                }
                break;
            case HORSE_STATUE:
                switch (meta) {
                    case 0:
                        this.setBlockBounds(0.375F, 0, 0.275F, 0.625F, 0.85F, 0.725F);
                        break;
                    case 1:
                        this.setBlockBounds(0.375F, 0, 0.275F, 0.625F, 0.85F, 0.725F);
                        break;
                    case 2:
                        this.setBlockBounds(0.275F, 0, 0.375F, 0.725F, 0.85F, 0.625F);
                        break;
                    case 3:
                        this.setBlockBounds(0.275F, 0, 0.375F, 0.725F, 0.85F, 0.625F);
                        break;
                }
                break;
                
        }
    }

    /**
     * Called when the block is attempted to be harvested
     */
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
        player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        player.addExhaustion(0.025F);
        GraveStoneHelper.spawnMob(world, x, y, z);

        if (GraveStoneConfig.silkTouchForGraves) {
            if (EnchantmentHelper.getSilkTouchModifier(player)) {
                dropBlock(world, x, y, z);
            } else {
                TileEntityGSGraveStone tileEntity = (TileEntityGSGraveStone) world.getBlockTileEntity(x, y, z);

                if (tileEntity != null && GraveStoneHelper.isSwordGrave(tileEntity)) {
                    tileEntity.dropSword();
                }
            }
        } else {
            dropBlock(world, x, y, z);
        }
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList();

        if (!GraveStoneConfig.silkTouchForGraves) {
            ret.add(getBlockItemStack(world, x, y, z));
        } else {
            TileEntityGSGraveStone tileEntity = (TileEntityGSGraveStone) world.getBlockTileEntity(x, y, z);

            if (tileEntity != null && GraveStoneHelper.isSwordGrave(tileEntity)) {
                ret.add(tileEntity.getSwordItem());
            }
        }

        return ret;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it.
     * (x, y, z) are the coordinates of the block and metadata is the block's
     * subtype/damage.
     */
    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata) {
    }

    /**
     * Return true if a player with Silk Touch can harvest this block directly,
     * and not its normal drops.
     */
    @Override
    public boolean canSilkHarvest() {
        return GraveStoneConfig.silkTouchForGraves;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public int idDropped(int par1, Random random, int par3) {
        return GraveStoneConfig.silkTouchForGraves ? 0 : this.blockID;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False
     * (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two adjacent blocks and also whether
     * the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        return GraveStoneConfig.graveRenderID;
    }

    /**
     * Called right before the block is destroyed by a player. Args: world, x,
     * y, z, metaData
     */
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        GraveStoneHelper.spawnMob(world, x, y, z);
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
        return getExplosionResistance(par1Entity);
    }

    @Override
    public float getExplosionResistance(Entity par1Entity) {
        return 18000000F;
    }

    /**
     * Called when the block is destroyed by an explosion. Useful for allowing
     * the block to take into account tile entities, metadata, etc. when
     * exploded, before it is removed.
     */
    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        TileEntityGSGraveStone te = (TileEntityGSGraveStone) world.getBlockTileEntity(x, y, z);

        if (te != null) {
            if (world.isRemote) {
                if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof ItemSpade) {
                } else {
                    String name;
                    String deathText;
                    String killerName;
                    deathText = te.getDeathTextComponent().getDeathText();

                    if (deathText.length() != 0) {
                        if (te.getDeathTextComponent().isLocalized()) {
                            name = te.getDeathTextComponent().getName();
                            killerName = ModGraveStone.proxy.getLocalizedEntityName(te.getDeathTextComponent().getKillerName());

                            if (killerName.length() == 0) {
                                player.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions(deathText, new Object[]{name}));
                            } else {
                                player.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions(deathText, new Object[]{name, killerName}));
                            }
                        } else {
                            player.sendChatToPlayer(ChatMessageComponent.createFromText(deathText));
                        }

                        if (te.getAge() != -1) {
                            //entityPlayer.sendChatToPlayer("Had lived " + entity.getAge() + " days");
                        }
                    }
                }
            } else {
                if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof ItemSpade) {
                    GraveStoneLogger.logInfo(player.username + " loot grave at " + x + "/" + y + "/" + z);
                    te.dropAllItems();
                }
            }
        }

        return false;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing
     * the block.
     */
    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityGSGraveStone(world);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        GraveStoneHelper.replaceGround(world, x, y - 1, z);
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an
     * update, as appropriate
     */
    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        TileEntityGSGraveStone tileEntity = (TileEntityGSGraveStone) world.getBlockTileEntity(x, y, z);

        if (tileEntity != null) {
            tileEntity.dropAllItems();
        }

        super.breakBlock(world, x, y, z, par5, par6);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
        if (!world.isBlockSolidOnSide(x, y - 1, z, ForgeDirection.DOWN, true)) {
            this.dropBlockAsItem(world, x, y, z, 0, 0);
            world.setBlock(x, y, z, 0, 0, 2);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and
     * wood.
     */
    @Override
    public int damageDropped(int metadata) {
        return 0;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tabs, List list) {
        for (byte i = 0; i < EnumGraves.WOODEN_SWORD.ordinal(); i++) {
            ItemStack stack = new ItemStack(id, 1, 0);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByte("GraveType", i);

            stack.setTagCompound(nbt);
            list.add(stack);
        }
        
        // horse graves
        {
            ItemStack stack = new ItemStack(id, 1, 0);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByte("GraveType", (byte) EnumGraves.HORSE_STATUE.ordinal());

            stack.setTagCompound(nbt);
            list.add(stack);
        }
        
        
        // swords 
        for (byte i = (byte) EnumGraves.WOODEN_SWORD.ordinal(); i <= EnumGraves.DIAMOND_SWORD.ordinal(); i++) {
            ItemStack graveStoneStack = new ItemStack(id, 1, 0);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByte("GraveType", i);

            if (GraveStoneHelper.isSwordGrave(i)) {
                nbt.setByte("SwordType", GraveStoneHelper.graveTypeToSwordType(i));
            }

            graveStoneStack.setTagCompound(nbt);
            list.add(graveStoneStack);
        }
        // enchanted swords
        for (byte i = (byte) EnumGraves.WOODEN_SWORD.ordinal(); i <= EnumGraves.DIAMOND_SWORD.ordinal(); i++) {
            ItemStack graveStoneStack = new ItemStack(id, 1, 0);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByte("GraveType", i);

            if (GraveStoneHelper.isSwordGrave(i)) {
                nbt.setByte("SwordType", GraveStoneHelper.graveTypeToSwordType(i));
                NBTTagCompound enchantmentTags = new NBTTagCompound();
                enchantmentTags.setTag("ench", new NBTTagList());
                nbt.setCompoundTag("SwordNBT", enchantmentTags);
            }

            graveStoneStack.setTagCompound(nbt);
            list.add(graveStoneStack);
        }
    }

    /**
     * Drop grave as item block
     */
    private void dropBlock(World world, int x, int y, int z) {
        ItemStack itemStack = getBlockItemStack(world, x, y, z);

        if (itemStack != null) {
            this.dropBlockAsItem_do(world, x, y, z, itemStack);
        }
    }

    /**
     * Get grave block as item block
     */
    private ItemStack getBlockItemStack(World world, int x, int y, int z) {
        ItemStack itemStack = this.createStackedBlock(0);
        TileEntityGSGraveStone tileEntity = (TileEntityGSGraveStone) world.getBlockTileEntity(x, y, z);

        if (tileEntity != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByte("GraveType", tileEntity.getGraveTypeNum());

            if (tileEntity.getDeathTextComponent().isLocalized()) {
                nbt.setBoolean("isLocalized", true);
                nbt.setString("name", tileEntity.getDeathTextComponent().getName());
                nbt.setString("KillerName", tileEntity.getDeathTextComponent().getKillerName());
            }

            nbt.setString("DeathText", tileEntity.getDeathTextComponent().getDeathText());
            nbt.setInteger("Age", tileEntity.getAge());

            if (tileEntity.getSword() != 0) {
                nbt.setByte("SwordType", tileEntity.getSword());
                nbt.setInteger("SwordDamage", tileEntity.getDamage());
                nbt.setString("SwordName", tileEntity.getSwordName());
                nbt.setCompoundTag("SwordNBT", tileEntity.getEnchantment());
            }

            itemStack.setTagCompound(nbt);
        }

        return itemStack;
    }

    /**
     * Create grave on death
     */
    public void createOnDeath(World world, int x, int y, int z, DeathMessageInfo deathInfo, int direction, List<ItemStack> items, int age, EnumGraveType entityType) {
        if (direction < 0) {
            direction = 360 + direction;
        }

        byte graveType = 0;
        byte swordType = 0;
        ItemStack sword = null;

        if (GraveStoneConfig.generateSwordGraves && world.rand.nextInt(4) == 0 && entityType.equals(EnumGraveType.PLAYER_GRAVES)) {
            sword = GraveStoneHelper.checkSword(items);
            if (sword != null) {
                swordType = GraveStoneHelper.getSwordType(sword.itemID);
            }
        }
        switch (entityType) {
            case PLAYER_GRAVES:
                if (swordType == 0) {
                    graveType = GENERATED_GRAVES[rand.nextInt(GENERATED_GRAVES.length)];
                } else {
                    graveType = (byte) (4 + swordType);
                }
                break;
            case DOGS_GRAVES:
                graveType = DOG_GRAVES[rand.nextInt(DOG_GRAVES.length)];
                break;
            case CATS_GRAVES:
                graveType = CAT_GRAVES[rand.nextInt(CAT_GRAVES.length)];
                break;
            case HORSE_GRAVES:
                graveType = HORSE_GRAVES[rand.nextInt(HORSE_GRAVES.length)];
                break;
        }

        boolean canGenerateGrave = false;
        while ((world.isAirBlock(x, y - 1, z) || world.getBlockMaterial(x, y - 1, z).isLiquid() || world.getBlockMaterial(x, y - 1, z).isReplaceable()) && y > 1) {
            y--;
        }
        if (canGenerateGraveAtCoordinates(world, x, y, z)) {
            canGenerateGrave = true;
        } else {
            for (byte xShift = -5; xShift < 6; xShift++) {
                for (byte zShift = -5; zShift < 6; zShift++) {
                    if (canGenerateGraveAtCoordinates(world, x + xShift, y, z + zShift)) {
                        x += xShift;
                        z += zShift;
                        canGenerateGrave = true;
                        break;
                    }
                }
                if (canGenerateGrave) {
                    break;
                }
            }
        }

        if (canGenerateGrave) {
            world.setBlock(x, y, z, GraveStoneConfig.graveStoneID, GraveStoneHelper.getMetadataBasedOnRotation(direction), 0x02);
            TileEntityGSGraveStone tileEntity = (TileEntityGSGraveStone) world.getBlockTileEntity(x, y, z);

            if (tileEntity != null) {
                if (sword != null) {
                    tileEntity.setSword(swordType);
                    tileEntity.setDamage(sword.getItemDamage());
                    tileEntity.setSwordName(sword.getDisplayName());

                    if (sword.getEnchantmentTagList() != null && sword.getEnchantmentTagList().tagCount() > 0) {
                        tileEntity.setEnchantment(sword.getTagCompound());
                    }
                }

                tileEntity.getDeathTextComponent().setLocalized();
                tileEntity.getDeathTextComponent().setName(deathInfo.getName());
                tileEntity.getDeathTextComponent().setDeathText(deathInfo.getDeathMessage());
                tileEntity.getDeathTextComponent().setKillerName(deathInfo.getKillerName());
                tileEntity.setItems(items);
                tileEntity.setGraveType(graveType);
                tileEntity.setAge(age);
                //System.out.println("!!!!!!!!!!!!! " + age);
            }
            GraveStoneLogger.logInfo("Create " + deathInfo.getName() + "'s grave at " + x + "x" + y + "x" + z);
        } else {
            ItemStack itemStack = this.createStackedBlock(0);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByte("GraveType", graveType);
            nbt.setBoolean("isLocalized", true);
            nbt.setString("name", deathInfo.getName());
            nbt.setString("DeathText", deathInfo.getDeathMessage());
            nbt.setString("KillerName", deathInfo.getKillerNameForTE());
//            nbt.setInteger("Age", age);

            if (swordType != 0) {
                nbt.setByte("SwordType", swordType);
                nbt.setInteger("SwordDamage", sword.getItemDamage());
                nbt.setString("SwordName", sword.getDisplayName());

                if (sword.getEnchantmentTagList() != null && sword.getEnchantmentTagList().tagCount() > 0) {
                    nbt.setCompoundTag("SwordNBT", sword.getTagCompound());
                }
            }

            itemStack.setTagCompound(nbt);
            this.dropBlockAsItem_do(world, x, y, z, itemStack);

            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i) != null) {
                        GSGraveStoneItems.dropItem(items.get(i), world, x, y, z);
                    }
                }
            }
            GraveStoneLogger.logInfo("Can not create " + deathInfo.getName() + "'s grave at " + x + "x" + y + "x" + z);
        }
    }

    private boolean canGenerateGraveAtCoordinates(World world, int x, int y, int z) {
        return (world.isAirBlock(x, y, z) || world.getBlockMaterial(x, y, z).isLiquid() || world.getBlockMaterial(x, y, z).isReplaceable());
    }
}
