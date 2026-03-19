package net.sussyit.endermanbossmod.entity.client;

import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;

public class EyeOfEndAttack implements IBossAttack{
    int timer = 0;

    private final int WARNING_DURATION = 25;
    private final int ATTACK_DURATION = 75;

    @Override
    public void start(EndermanBossEntity boss) {

    }

    @Override
    public void tick(EndermanBossEntity boss) {

    }

    @Override
    public void stop(EndermanBossEntity boss) {

    }

    @Override
    public boolean isFinished() {
        return timer > WARNING_DURATION + ATTACK_DURATION;
    }

    @Override
    public int getAnimationId() {
        return EndermanBossEntity.EYES_OF_END;
    }

    @Override
    public int getDuration() {
        return WARNING_DURATION + ATTACK_DURATION;
    }
}
