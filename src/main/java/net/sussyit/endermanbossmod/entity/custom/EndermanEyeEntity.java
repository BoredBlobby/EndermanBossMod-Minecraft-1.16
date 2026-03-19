package net.sussyit.endermanbossmod.entity.custom;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class EndermanEyeEntity extends Mob {
    public final AnimationState eyeSpinAnimationState = new AnimationState();
    private UUID ownerUUID;
    private Monster owner;

    public EndermanEyeEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        //this.noPhysics = true;
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
        this.eyeSpinAnimationState.startIfStopped(this.tickCount);

        Monster owner = getOwner();

        if(owner instanceof EndermanBossEntity boss) {
            if(boss.getAttack() == EndermanBossEntity.ATTACK_NONE) {
                this.discard();
            }
        }


        if(this.tickCount > 100) {
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }



}
