package net.sussyit.endermanbossmod.entity.client;

import net.minecraft.world.entity.LivingEntity;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;

import java.util.List;

public class KnockbackAttack implements IBossAttack{
    private int timer = 0;

    private final int WARNING_DURATION = 10;
    private final int ATTACK_DURATION = 10;

    private final double RADIUS = 4;

    @Override
    public void start(EndermanBossEntity boss) {
        boss.knockBackAnimationState.start(boss.tickCount);
        boss.setAttack(EndermanBossEntity.KNOCKBACK_ATTACK);
    }

    @Override
    public void tick(EndermanBossEntity boss) {
        timer++;

        if (timer >= WARNING_DURATION && timer <= WARNING_DURATION + ATTACK_DURATION) {
            detonateCircle(boss);
        }
    }

    @Override
    public void stop(EndermanBossEntity boss) {
        boss.knockBackAnimationState.stop();
    }

    @Override
    public boolean isFinished() {
        return timer > WARNING_DURATION + ATTACK_DURATION;
    }

    @Override
    public int getAnimationId() {
        return EndermanBossEntity.KNOCKBACK_ATTACK;
    }

    @Override
    public int getDuration() {
        return WARNING_DURATION + ATTACK_DURATION;
    }

    private void detonateCircle(EndermanBossEntity boss) {
        List<LivingEntity> targets = boss.level().getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(RADIUS), e -> e != boss);
        for(LivingEntity target : targets) {
            if (boss.distanceTo(target) <= RADIUS) {
                target.hurt(boss.damageSources().mobAttack(boss), 1.0f);

                double dx = target.getX() - boss.getX();
                double dz = target.getZ() - boss.getZ();
                target.knockback(8.0, -dx, -dz);
            }
        }
    }
}
