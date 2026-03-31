package net.sussyit.endermanbossmod.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class EyeOfEndEntity extends Mob {
    private UUID ownerUUID;
    private UUID targetUUID;
    private Monster owner;
    private Player targetPlayer;

    private static final EntityDataAccessor<Integer> ATTACK_STAGE =
            SynchedEntityData.defineId(EyeOfEndEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACK_STAGE, 0);
    }

    public void setAttackStage(int stage) {
        this.entityData.set(ATTACK_STAGE, stage);
    }

    public int getAttackStage() {
        return this.entityData.get(ATTACK_STAGE);
    }

    public EyeOfEndEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setInvulnerable(true);
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

    public void setTargetPlayer(Player player) {
        this.targetUUID = player.getUUID();
    }

    private Player getTargetPlayer() {
        if(targetUUID == null) return null;
        if(this.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getPlayerByUUID(targetUUID);
        } else {
            return this.level().getPlayerByUUID(targetUUID);
        }
    }

    @Override
    public void tick() {
        super.tick();

        Monster owner = getOwner();

        if(owner instanceof EndermanBossEntity boss) {
            if(boss.getAttack() == EndermanBossEntity.ATTACK_NONE) {
                this.discard();
            }
            this.setAttackStage(boss.getTimerStageEyeOfEndAttack());
        }

        Player targetPlayer = getTargetPlayer();

        if (targetPlayer != null && targetPlayer.isAlive()) {
            this.getLookControl().setLookAt(targetPlayer, 30.0F, 30.0F);
        }

        int stage = this.getAttackStage();

        if(!this.level().isClientSide()) {
            if(stage == EndermanBossEntity.OPEN) {
                if (targetPlayer != null && targetPlayer.isAlive()) {
                    Vec3 playerLook = targetPlayer.getLookAngle().normalize();
                    Vec3 toEntity = this.position().subtract(targetPlayer.getEyePosition()).normalize();

                    double dot = playerLook.dot(toEntity);

                    // threshold: closer to 1 = more precise
                    if (dot > 0.4) { // 🔥 tweak this
                        targetPlayer.hurt(targetPlayer.damageSources().magic(), 15.0f);
                    }
                }
            }
        }


        if(this.tickCount > 100) {
            this.discard();
        }
    }
}
