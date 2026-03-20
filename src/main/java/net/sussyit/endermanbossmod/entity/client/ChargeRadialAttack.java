package net.sussyit.endermanbossmod.entity.client;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.sussyit.endermanbossmod.entity.ModEntities;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;
import net.sussyit.endermanbossmod.entity.custom.EndermanEyeEntity;

import java.util.List;


public class ChargeRadialAttack implements IBossAttack{
    int timer = 0;

    private final int WARNING_DURATION = 100;
    private final int ATTACK_DURATION = 20;
    private final double RADIUS = 30.0;

    @Override
    public void start(EndermanBossEntity boss) {
        boss.chargeAttackAnimationState.start(boss.tickCount);
        boss.setAttack(EndermanBossEntity.CHARGE_RADIAL_ATTACK);
        timer = 0;
    }

    @Override
    public void tick(EndermanBossEntity boss) {
        timer++;

        if (timer < WARNING_DURATION) {
            if(timer == 1) {
                EndermanEyeEntity endermanEye = new EndermanEyeEntity(ModEntities.ENDERMANEYE.get(), boss.level());
                endermanEye.setOwner(boss);
                endermanEye.setPos(boss.getX(), boss.getY()+3, boss.getZ());

                boss.level().addFreshEntity(endermanEye);
            }
            //spawn enderman eye entity and also warning circle
        }

        if (timer >= WARNING_DURATION && timer <= (WARNING_DURATION + ATTACK_DURATION) && boss.getAttack() == EndermanBossEntity.CHARGE_RADIAL_ATTACK) {
            this.detonateCircle(boss);
        }
    }

    @Override
    public void stop(EndermanBossEntity boss) {
        boss.chargeAttackAnimationState.stop();

    }

    @Override
    public boolean isFinished() {
        return timer > (WARNING_DURATION + ATTACK_DURATION);
    }

    @Override
    public int getAnimationId() {
        return EndermanBossEntity.CHARGE_RADIAL_ATTACK;
    }

    @Override
    public int getDuration() {
        return WARNING_DURATION + ATTACK_DURATION;
    }

    private void detonateCircle(EndermanBossEntity boss) {
        List<LivingEntity> targets = boss.level().getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(RADIUS), e -> e != boss);
        for(LivingEntity target : targets) {
            if (boss.distanceTo(target) <= RADIUS) {
                target.hurt(boss.damageSources().mobAttack(boss), 20.0f);

                double dx = target.getX() - boss.getX();
                double dz = target.getZ() - boss.getZ();
                target.knockback(1.1, -dx, -dz);
            }
        }
    }
}
