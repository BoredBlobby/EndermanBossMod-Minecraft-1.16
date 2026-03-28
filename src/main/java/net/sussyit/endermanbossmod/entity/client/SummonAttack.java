package net.sussyit.endermanbossmod.entity.client;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;

public class SummonAttack implements IBossAttack{
    int timer = 0;
    private final int WARNING_DURATION = 20;
    private final int ATTACK_DURATION = 100;

    @Override
    public void start(EndermanBossEntity boss) {
        boss.summonAttackAnimationState.startIfStopped(boss.tickCount);
        boss.setAttack(EndermanBossEntity.SUMMON_ATTACK);
        timer = 0;
    }

    @Override
    public void tick(EndermanBossEntity boss) {
        timer++;

        if(timer == 1){
            boss.playSound(SoundEvents.ENDERMAN_AMBIENT, 1.0F, 1.0F);
        }

        if (timer > WARNING_DURATION && timer <= (WARNING_DURATION + ATTACK_DURATION)) {
            if (timer % 20 == 0) {
                EnderMan enderman = EntityType.ENDERMAN.create(boss.level());
                boss.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.0F);

                if (enderman != null) {
                    Player targetPlayer = boss.level().getNearestPlayer(boss, 20.0D);

                    if(targetPlayer != null && targetPlayer.isAlive()) {

                        enderman.moveTo(boss.getX(), boss.getY(), boss.getZ());

                        // Set target to the player
                        enderman.setTarget(targetPlayer);
                        enderman.addTag("boss_minion");
                        enderman.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10);
                        enderman.setHealth(10);

                        // Optional: make it instantly aggressive
                        enderman.setPersistentAngerTarget(targetPlayer.getUUID());
                        enderman.startPersistentAngerTimer();

                        boss.level().addFreshEntity(enderman);
                    }
                }
            }
        }
    }

    @Override
    public void stop(EndermanBossEntity boss) {
        boss.summonAttackAnimationState.stop();
    }

    @Override
    public boolean isFinished() {
        return timer > WARNING_DURATION + ATTACK_DURATION;
    }

    @Override
    public int getAnimationId() {
        return EndermanBossEntity.SUMMON_ATTACK;
    }

    @Override
    public int getDuration() {
        return WARNING_DURATION + ATTACK_DURATION;
    }
}
