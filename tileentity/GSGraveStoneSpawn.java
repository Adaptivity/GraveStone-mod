package net.minecraft.GraveStone.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;

public class GSGraveStoneSpawn {

    private TileEntityGSGraveStone tileEntity;
    private static final Random rand = new Random();
    private static final String[] MOB_ID = {"Zombie", "Skeleton"};
    private static final int PLAYER_RANGE = 35;
    /** The stored delay before a new spawn. */
    private int delay = 600;
    private static final int MIN_DELAY = 800;
    private static final int MAX_DELAY = 1800;
    private Entity spawnedMob;
    private List field_92060_e = null;
    /** The extra NBT data to add to spawned entities */
    private TileEntityGSGraveStoneSpawnData spawnerTags = null;
    /** Maximum number of entities for limiting mob spawning */
    private static final int MAX_NEARBY_ENTITIES = 3;
    /** Range for spawning new entities with gravestone */
    private static final int SPAWN_RANGE = 1;

    public GSGraveStoneSpawn(TileEntityGSGraveStone tileEntity) {
        this.tileEntity = tileEntity;
    }

    /**
     * Returns true if there is a player in range (using World.getClosestPlayer)
     */
    public boolean anyPlayerInRange() {
        return tileEntity.worldObj.getClosestPlayer(tileEntity.xCoord + 0.5D, tileEntity.yCoord + 0.5D, tileEntity.zCoord + 0.5D, PLAYER_RANGE) != null;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity() {
        if (!tileEntity.worldObj.isDaytime() && anyPlayerInRange()) {
            double x, y, z;

            if (tileEntity.worldObj.isRemote) {
                if (this.delay > 0) {
                    --this.delay;
                }
            } else {
                if (this.delay == -1) {
                    this.updateDelay();
                }

                if (this.delay > 0) {
                    --this.delay;
                    return;
                }

                boolean var12 = false;

                Entity spawnMob = getMobEntity();

                if (spawnMob == null) {
                    return;
                } else {
                    int nearbyEntitiesCount = tileEntity.worldObj.getEntitiesWithinAABB(spawnMob.getClass(), AxisAlignedBB.getAABBPool().getAABB(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
                            tileEntity.xCoord + 1, tileEntity.yCoord + 1, tileEntity.zCoord + 1).expand(1.0D, 4.0D, SPAWN_RANGE * 2)).size();

                    if (nearbyEntitiesCount >= MAX_NEARBY_ENTITIES) {
                        this.updateDelay();
                        return;
                    }

                    x = tileEntity.xCoord + 0.5;
                    y = tileEntity.yCoord;
                    z = tileEntity.zCoord + 0.5;

                    EntityLiving livingEntity = (EntityLiving) spawnMob;
                    float rotation = tileEntity.worldObj.rand.nextFloat() * 360.0F;
                    boolean canSpawn = false;

                    spawnMob.setLocationAndAngles(x, y, z, rotation, 0.0F);
                    if (livingEntity.getCanSpawnHere()) {
                        canSpawn = true;
                    } else {
                        if (spawnMob instanceof EntitySkeleton) {
                            EntitySkeleton witherSkeleton = (EntitySkeleton) spawnMob;
                            if (witherSkeleton.getSkeletonType() == 1) {
                                x += 1;
                                spawnMob.setLocationAndAngles(x, y, z, rotation, 0.0F);
                                if (livingEntity.getCanSpawnHere()) {
                                    canSpawn = true;
                                } else {
                                    x -= 1;
                                    z += 1;
                                    spawnMob.setLocationAndAngles(x, y, z, rotation, 0.0F);
                                    if (livingEntity.getCanSpawnHere()) {
                                        canSpawn = true;
                                    } else {
                                        z -= 2;
                                        spawnMob.setLocationAndAngles(x, y, z, rotation, 0.0F);
                                        if (livingEntity.getCanSpawnHere()) {
                                            canSpawn = true;
                                        } else {
                                            x -= 1;
                                            z += 1;
                                            spawnMob.setLocationAndAngles(x, y, z, rotation, 0.0F);
                                            if (livingEntity.getCanSpawnHere()) {
                                                canSpawn = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (canSpawn && rand.nextInt(40) == 13) {
                        x = tileEntity.xCoord + tileEntity.worldObj.rand.nextFloat();
                        y = tileEntity.yCoord + tileEntity.worldObj.rand.nextFloat();
                        z = tileEntity.zCoord + tileEntity.worldObj.rand.nextFloat();
                        tileEntity.worldObj.spawnParticle("smoke", x, y, z, 0.0D, 0.0D, 0.0D);
                        tileEntity.worldObj.spawnParticle("flame", x, y, z, 0.0D, 0.0D, 0.0D);

                        this.writeNBTTagsTo(spawnMob);
                        tileEntity.worldObj.spawnEntityInWorld(spawnMob);
                        tileEntity.worldObj.playAuxSFX(2004, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 0);

                        if (livingEntity != null) {
                            livingEntity.spawnExplosionParticle();
                        }

                        var12 = true;
                    }
                }


                if (var12) {
                    this.updateDelay();
                }
            }
        }
    }

    /**
     * will create the entity from the internalID the first time it is accessed
     */
    public Entity getMobEntity() {
        String id = this.getMobID();

        if (tileEntity.yCoord < 51 && tileEntity.worldObj.getBlockId(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord) == Block.netherBrick.blockID) {
            if (rand.nextInt(20) == 0) {
                if (id.equals("Skeleton")) {
                    EntitySkeleton entity = (EntitySkeleton) EntityList.createEntityByName(id, tileEntity.worldObj);
                    entity.setSkeletonType(1);
                    this.spawnedMob = entity;
                } else {
                    if (id.equals("Zombie")) {
                        id = "PigZombie";
                    }

                    Entity entity = EntityList.createEntityByName(id, tileEntity.worldObj);
                    this.spawnedMob = entity;
                }
            } else {
                Entity entity = EntityList.createEntityByName(id, tileEntity.worldObj);
                this.spawnedMob = entity;
            }
        } else {
            Entity entity = EntityList.createEntityByName(id, tileEntity.worldObj);
            this.spawnedMob = entity;
        }

        return this.spawnedMob;
    }

    /*
     * return random mob id from list
     */
    public String getMobID() {
        return MOB_ID[rand.nextInt(MOB_ID.length)];
    }

    /**
     * Sets the delay before a new spawn (base delay of 200 + random number up to 600).
     */
    private void updateDelay() {
        delay = MIN_DELAY + tileEntity.worldObj.rand.nextInt(MAX_DELAY - MIN_DELAY);

        if (this.field_92060_e != null && this.field_92060_e.size() > 0) {
            tileEntity.worldObj.markBlockForUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        }

        int block = tileEntity.getBlockType().blockID;
        tileEntity.worldObj.addBlockEvent(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, block, 1, 0);
    }

    public String getEntityId() {
        return this.spawnerTags == null ? this.getMobID() : this.spawnerTags.field_92033_c;
    }

    public void readSpawn(NBTTagCompound nbtTag) {
        delay = nbtTag.getShort("Delay");

        if (nbtTag.hasKey("SpawnPotentials")) {
            field_92060_e = new ArrayList();
            NBTTagList ntbList = nbtTag.getTagList("SpawnPotentials");

            for (int i = 0; i < ntbList.tagCount(); ++i) {
                field_92060_e.add(new TileEntityGSGraveStoneSpawnData(tileEntity, (NBTTagCompound) ntbList.tagAt(i)));
            }
        } else {
            this.field_92060_e = null;
        }


        if (tileEntity.worldObj != null && tileEntity.worldObj.isRemote) {
            spawnedMob = null;
        }
    }

    public void writeSpawn(NBTTagCompound nbtTag) {
        nbtTag.setShort("Delay", (short) delay);

        if (spawnerTags != null) {
            nbtTag.setCompoundTag("SpawnData", (NBTTagCompound) spawnerTags.field_92032_b.copy());
        }

        if (spawnerTags != null || field_92060_e != null && field_92060_e.size() > 0) {
            NBTTagList nbtList = new NBTTagList();

            if (field_92060_e != null && field_92060_e.size() > 0) {
                Iterator it = field_92060_e.iterator();

                while (it.hasNext()) {
                    TileEntityGSGraveStoneSpawnData tileEntityData = (TileEntityGSGraveStoneSpawnData) it.next();
                    nbtList.appendTag(tileEntityData.func_92030_a());
                }
            } else {
                nbtList.appendTag(spawnerTags.func_92030_a());
            }

            nbtTag.setTag("SpawnPotentials", nbtList);
        }
    }

    public void writeNBTTagsTo(Entity par1Entity) {
        if (spawnerTags != null) {
            NBTTagCompound var2 = new NBTTagCompound();
            par1Entity.addEntityID(var2);
            Iterator var3 = spawnerTags.field_92032_b.getTags().iterator();

            while (var3.hasNext()) {
                NBTBase var4 = (NBTBase) var3.next();
                var2.setTag(var4.getName(), var4.copy());
            }

            par1Entity.readFromNBT(var2);
        } else if (par1Entity instanceof EntityLiving && par1Entity.worldObj != null) {
            ((EntityLiving) par1Entity).initCreature();
        }
    }
    
    public void setMinDelay() {
        delay = MIN_DELAY;
    }
}
