package net.sussyit.endermanbossmod.entity.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SpikeEntity extends Mob {
    private UUID ownerUUID;
    private Monster owner;

    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(SpikeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_ROTATION = SynchedEntityData.defineId(SpikeEntity.class, EntityDataSerializers.FLOAT);

    public final AnimationState spikeAnimationState = new AnimationState();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SCALE, 1.0f);
        builder.define(DATA_ROTATION, 0.0f);
    }

    // Getters and Setters
    public void setScale(float scale) { this.entityData.set(DATA_SCALE, scale); }
    public float getScale() { return this.entityData.get(DATA_SCALE); }

    public void setSpikeRotation(float degrees) { this.entityData.set(DATA_ROTATION, degrees); }
    public float getSpikeRotation() { return this.entityData.get(DATA_ROTATION); }

    public SpikeEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.ATTACK_DAMAGE, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 6.0);
    }

    public void setOwner(Monster monster) {
        this.owner = monster;
        this.ownerUUID = monster.getUUID();
    }

    private Monster getOwner() {
        if (owner == null && ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(ownerUUID);
            if (entity instanceof Monster living) {
                owner = living;
            }
        }
        return owner;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        this.spikeAnimationState.startIfStopped(this.tickCount);
        Monster owner = getOwner();

        if(owner instanceof EndermanBossEntity boss) {
            if(boss.getAttack() == EndermanBossEntity.ATTACK_NONE) {
                this.discard();
            }
        }

        if(this.tickCount > 30) {
            this.discard();
        }

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

}
