package net.sussyit.endermanbossmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

public class EndermanBossEntity extends Monster {
    private static final EntityDataAccessor<Integer> BOSS_PHASE =
            SynchedEntityData.defineId(EndermanBossEntity.class, EntityDataSerializers.INT);
    public final static int PHASE_1 = 1;
    public final static int PHASE_2 = 2;
    public final static int PHASE_3 = 3;

    private static final EntityDataAccessor<Integer> BOSS_ATTACKS =
            SynchedEntityData.defineId(EndermanBossEntity.class, EntityDataSerializers.INT);
    public final static int CHARGE_RADIAL_ATTACK = 1;
    public final static int SUMMON_ATTACK = 2;
    public final static int DASH_ATTACK = 3;
    public final static int EYES_OF_END = 4;
    public final static int LIGHTING_STRIKE = 5;



    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
            .add(Attributes.MAX_HEALTH, 200.0)
            .add(Attributes.ATTACK_DAMAGE, 15.0)
            .add(Attributes.MOVEMENT_SPEED, 0.25)
            .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    public EndermanBossEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!this.level().isClientSide()) {
            Player nearestPlayer = this.level().getNearestPlayer(this, 7.0D);

            if(nearestPlayer != null) {
                this.teleportRandom();
            }
        }
    }

    protected boolean teleportRandom() {
        if(!this.level().isClientSide() && this.isAlive()) {
            //Try multiple times(16 times total)
            for(int i = 0; i < 16; ++i) {
                double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 11.0;
                double d1 = this.getY() + (double) (this.random.nextInt(64) - 32); // down and up
                double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 11.0;

                BlockPos targetPos = new BlockPos((int)d0, (int)d1, (int)d2);

                if (!this.level().getBlockState(targetPos).blocksMotion() && d1 + d2 > 7) {
                    if (this.teleport(d0, d1, d2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, y, z);

        // Scan down to find the floor (prevents spawning in mid-air)
        while (mutablePos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(mutablePos).blocksMotion()) {
            mutablePos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level().getBlockState(mutablePos);
        if (blockstate.blocksMotion() && !blockstate.getFluidState().is(FluidTags.WATER)) {

            // Move Y up by 1 so the entity stands ON the block, not IN it
            double finalY = mutablePos.getY() + 1;

            EntityTeleportEvent.EnderEntity event = EventHooks.onEnderTeleport(this, x, finalY, z);
            if (event.isCanceled()) return false;

            Vec3 oldPos = this.position();

            // Use randomTeleport with the specific coordinates from the event
            boolean success = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);

            if (success) {
                // Sound at the old position
                this.level().playSound(null, oldPos.x, oldPos.y, oldPos.z, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                // Sound at the new position
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }
}
