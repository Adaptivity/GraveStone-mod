package gravestone.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class EntitySkullCrawler extends EntityMob {

    public EntitySkullCrawler(World world) {
        super(world);
        this.setSize(0.8F, 0.8F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.worldObj.isRemote) {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.9D);
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this
     * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
     * PigZombies).
     */
    @Override
    protected Entity findPlayerToAttack() {
        return this.worldObj.getClosestVulnerablePlayerToEntity(this, 2);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "mob.skeleton.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "mob.skeleton.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    @Override
    protected void playStepSound(int par1, int par2, int par3, int par4) {
        this.playSound("mob.spider.step", 0.15F, 1.0F);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden
     * by each mob to define their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float par2) {
        if (par2 > 2.0F && par2 < 6.0F && this.rand.nextInt(10) == 0) {
            if (this.onGround) {
                double d0 = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                float f2 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
                this.motionX = d0 / (double) f2 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
                this.motionZ = d1 / (double) f2 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
                this.motionY = 0.4000000059604645D;
            }
        } else {
            super.attackEntity(entity, par2);
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    @Override
    protected int getDropItemId() {
        return Item.bone.itemID;
    }

    /**
     * Drop 0-2 items of this living's type.
     *
     * @param hitByPlayer - Whether this entity has recently been hit by a
     * player.
     * @param lootLevel - Level of Looting used to kill this mob.
     */
    @Override
    protected void dropFewItems(boolean hitByPlayer, int lootLevel) {
        super.dropFewItems(hitByPlayer, lootLevel);

        if (hitByPlayer && (this.rand.nextInt(10) == 0 || (lootLevel > 0 && this.rand.nextInt(6 - lootLevel) == 0))) {
            this.dropItem(Item.skull.itemID, 1);
        }
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    /**
     * Sets the Entity inside a web block.
     */
    @Override
    public void setInWeb() {
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
        return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns
     * false. The WatchableObject is updated using setBesideClimableBlock.
     */
    public boolean isBesideClimbableBlock() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to
     * 0x01 if par1 is true or 0x00 if it is false.
     */
    public void setBesideClimbableBlock(boolean par1) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (par1) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 &= -2;
        }

        this.dataWatcher.updateObject(16, Byte.valueOf(b0));
    }

    @Override
    public EntityLivingData onSpawnWithEgg(EntityLivingData data) {
        Object par1EntityLivingData1 = super.onSpawnWithEgg(data);

        /*
         if (par1EntityLivingData1 == null) {
         par1EntityLivingData1 = new SpiderEffectsGroupData();

         if (this.worldObj.difficultySetting > 2 && this.worldObj.rand.nextFloat() < 0.1F * this.worldObj.getLocationTensionFactor(this.posX, this.posY, this.posZ)) {
         ((SpiderEffectsGroupData) par1EntityLivingData1).func_111104_a(this.worldObj.rand);
         }
         }

         if (par1EntityLivingData1 instanceof SpiderEffectsGroupData) {
         int i = ((SpiderEffectsGroupData) par1EntityLivingData1).field_111105_a;

         if (i > 0 && Potion.potionTypes[i] != null) {
         this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
         }
         }*/

        return (EntityLivingData) par1EntityLivingData1;
    }
}
