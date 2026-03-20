package net.sussyit.endermanbossmod.entity.client;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sussyit.endermanbossmod.entity.ModEntities;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;
import net.sussyit.endermanbossmod.entity.custom.EyeOfEndEntity;
import net.sussyit.endermanbossmod.util.CameraShakeUtils;

public class EyeOfEndAttack implements IBossAttack{
    int timer = 0;

    private final int HIDDEN_DURATION = 10;
    private final int CLOSED_DURATION = 43;
    private final int WARNING_DURATION = 2;
    private final int ATTACK_DURATION = 55;


    @Override
    public void start(EndermanBossEntity boss) {
        timer = 0;
        boss.eyeOfEndAttackAnimationState.start(boss.tickCount);
        boss.setAttack(EndermanBossEntity.EYES_OF_END);
    }


    @Override
    public void tick(EndermanBossEntity boss) {
        if (boss.level().isClientSide) return;
        timer++;

        if(boss.getAttack() == EndermanBossEntity.EYES_OF_END) {
            if(timer == 1) {
                Player target = boss.level().getNearestPlayer(boss, 15.0D);
                if (target != null && target.isAlive()) {
                    EyeOfEndEntity eyeOfEnd = new EyeOfEndEntity(ModEntities.EYEOFEND.get(), boss.level());
                    eyeOfEnd.setOwner(boss);
                    eyeOfEnd.setTargetPlayer(target);

                    Vec3 look = target.getLookAngle(); // direction player is facing

                    double distance = 30.0; // how far in front of player

                    double targetX = target.getX() + look.x * distance;
                    double targetY = target.getEyeY();
                    double targetZ = target.getZ() + look.z * distance;


                    double newX = eyeOfEnd.getX() + (targetX - eyeOfEnd.getX());
                    double newY = eyeOfEnd.getY() + (targetY - eyeOfEnd.getY() + 8.0f);
                    double newZ = eyeOfEnd.getZ() + (targetZ - eyeOfEnd.getZ());

                    AABB box = eyeOfEnd.getBoundingBox().move(newX - eyeOfEnd.getX(), newY - eyeOfEnd.getY(), newZ - eyeOfEnd.getZ());

                    if (!boss.level().noCollision(box)) {
                        boss.setAttack(EndermanBossEntity.ATTACK_NONE);
                        return;
                    }

                    eyeOfEnd.setPos(newX, newY, newZ);

                    boss.level().addFreshEntity(eyeOfEnd);

                } else {
                    boss.setAttack(EndermanBossEntity.ATTACK_NONE);
                    return;
                }
            }
            if(timer < HIDDEN_DURATION) {
                boss.setTimerStageEyeOfEndAttack(EndermanBossEntity.HIDDEN);
            }
            if (timer < CLOSED_DURATION + HIDDEN_DURATION && timer > HIDDEN_DURATION){
                boss.setTimerStageEyeOfEndAttack(EndermanBossEntity.CLOSED);
            }
            if (timer >= CLOSED_DURATION + HIDDEN_DURATION && timer < (CLOSED_DURATION + WARNING_DURATION + HIDDEN_DURATION)) {
                boss.setTimerStageEyeOfEndAttack(EndermanBossEntity.WARNING);
            }
            if (timer >= (CLOSED_DURATION + WARNING_DURATION + HIDDEN_DURATION) && timer < (CLOSED_DURATION + WARNING_DURATION + ATTACK_DURATION + HIDDEN_DURATION)) {
                if(timer == (CLOSED_DURATION + WARNING_DURATION + HIDDEN_DURATION)) {
                    if(!boss.level().isClientSide) {
                        boss.cleanupMinions();
                    }
                    CameraShakeUtils.shake(ATTACK_DURATION, 1f, true, 0.25f);
                }
                boss.setTimerStageEyeOfEndAttack(EndermanBossEntity.OPEN);
            }
            }

    }

    @Override
    public void stop(EndermanBossEntity boss) {

    }

    @Override
    public boolean isFinished() {
        return timer > WARNING_DURATION + ATTACK_DURATION + CLOSED_DURATION + HIDDEN_DURATION;
    }

    @Override
    public int getAnimationId() {
        return EndermanBossEntity.EYES_OF_END;
    }

    @Override
    public int getDuration() {
        return WARNING_DURATION + ATTACK_DURATION + CLOSED_DURATION + HIDDEN_DURATION;
    }
}
