package net.sussyit.endermanbossmod.entity.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sussyit.endermanbossmod.entity.ModEntities;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;
import net.sussyit.endermanbossmod.entity.custom.SpikeEntity;

public class SpikeAttack implements IBossAttack{
    private int attackStep = 0;
    private int attackCooldown = 0;
    private Vec3 attackDirection = Vec3.ZERO;
    private Player nearestPlayer = null;
    private Vec3 startPos;

    @Override
    public void start(EndermanBossEntity boss) {
        nearestPlayer = boss.level().getNearestPlayer(boss, 50.0);
        this.attackStep= 0;
        this.attackCooldown = 0;
        this.startPos = boss.position();
        boss.setAttack(EndermanBossEntity.SPIKE_ATTACK);
        if(nearestPlayer != null && nearestPlayer.isAlive()) {
            Vec3 rawDir = nearestPlayer.position().subtract(boss.position());
            this.attackDirection = new Vec3(rawDir.x, 0, rawDir.z).normalize();
        } else {
            this.attackStep = 21;
        }
    }

    @Override
    public void tick(EndermanBossEntity boss) {
        if(!boss.level().isClientSide()) {
            if (nearestPlayer != null && nearestPlayer.isAlive()) {
                if (attackCooldown > 0) {
                    attackCooldown--;
                    return;
                }

                double stepDistance = 1.5;
                double offset = (boss.getRandom().nextDouble() - 0.5) * 0.3;
                Vec3 hitPos = startPos.add(
                        attackDirection.scale(stepDistance * attackStep)
                ).add(offset, 0, offset);

                if(attackStep != 0) {
                    spawnSpike(hitPos, boss);
                }

                attackStep++;
                attackCooldown = 2;

                if (attackStep > 20) {
                    boss.setAttack(EndermanBossEntity.ATTACK_NONE);
                }


            }
        }
    }

    private void spawnSpike(Vec3 pos, EndermanBossEntity boss) {
        BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);

        int maxDrop = 10;
        // Adjust to ground level if needed
        while (boss.level().isEmptyBlock(blockPos) && maxDrop-- > 0) {
            blockPos = blockPos.below();
        }

        // Example: damage nearby entities
        AABB area = new AABB(blockPos).inflate(1.0);

        for (LivingEntity entity : boss.level().getEntitiesOfClass(LivingEntity.class, area)) {
            if (entity != boss) {
                entity.hurt(boss.damageSources().magic(), 6.0f);
            }
        }

        // RANDOM SIZE: between 0.8 and 1.5
        SpikeEntity spikeEntity = new SpikeEntity(ModEntities.SPIKE.get(), boss.level());
        float randomScale = 1.2f + boss.getRandom().nextFloat() * 0.3f;
        spikeEntity.setScale(randomScale);

        // RANDOM ROTATION: 0 to 360 degrees
        float randomYaw = boss.getRandom().nextFloat() * 360.0f;
        spikeEntity.setSpikeRotation(randomYaw);

        spikeEntity.setOwner(boss);
        spikeEntity.setPos(blockPos.getX() + 0.5, blockPos.getY()+1.0, blockPos.getZ() + 0.5);

        boss.level().addFreshEntity(spikeEntity);
    }

    @Override
    public void stop(EndermanBossEntity boss) {
        this.attackStep = 0;
        this.attackCooldown = 0;
        this.attackDirection = Vec3.ZERO;
        this.nearestPlayer = null;
    }

    @Override
    public boolean isFinished() {
        return attackStep > 20;
    }

    @Override
    public int getAnimationId() {
        return EndermanBossEntity.SPIKE_ATTACK;
    }

    @Override
    public int getDuration() {
        return 20*2;
    }
}
