package net.sussyit.endermanbossmod.entity.client;

import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;

public interface IBossAttack {
    void start(EndermanBossEntity boss);
    void tick(EndermanBossEntity boss);
    void stop(EndermanBossEntity boss);
    boolean isFinished();
    int getAnimationId();
    int getDuration();
}
