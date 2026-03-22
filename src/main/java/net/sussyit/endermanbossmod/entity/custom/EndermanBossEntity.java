package net.sussyit.endermanbossmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.sussyit.endermanbossmod.entity.client.*;
import net.sussyit.endermanbossmod.event.ModClientEvents;

import java.util.List;

public class EndermanBossEntity extends Monster {
    public final AnimationState chargeAttackAnimationState = new AnimationState();
    public final AnimationState summonAttackAnimationState = new AnimationState();
    public final AnimationState dashAttackAnimationState = new AnimationState();
    public final AnimationState eyeOfEndAttackAnimationState = new AnimationState();
    public final AnimationState knockBackAnimationState = new AnimationState();

    private static final EntityDataAccessor<Integer> BOSS_PHASE =
            SynchedEntityData.defineId(EndermanBossEntity.class, EntityDataSerializers.INT);
    public final static int PHASE_NONE = 0;
    public final static int PHASE_1 = 1;
    public final static int PHASE_2 = 2;
    public final static int PHASE_3 = 3;

    private static final EntityDataAccessor<Integer> BOSS_ATTACKS =
            SynchedEntityData.defineId(EndermanBossEntity.class, EntityDataSerializers.INT);
    public final static int ATTACK_NONE = 0;
    public final static int CHARGE_RADIAL_ATTACK = 1;
    public final static int SUMMON_ATTACK = 2;
    public final static int DASH_ATTACK = 3;
    public final static int EYES_OF_END = 4;
    public final static int KNOCKBACK_ATTACK = 5;
    public final static int SPIKE_ATTACK = 6;


    private IBossAttack activeAttack = null;
    private int attackCooldown = 0;

    private final ServerBossEvent bossEvent= new ServerBossEvent(Component.literal("Enderman Mage"),
            BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_20);

    /* --------Boss stats--------- */

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
            .add(Attributes.MAX_HEALTH, 200.0)
            .add(Attributes.ATTACK_DAMAGE, 15.0)
            .add(Attributes.MOVEMENT_SPEED, 0.25)
            .add(Attributes.KNOCKBACK_RESISTANCE, 6.0)
            .add(Attributes.FOLLOW_RANGE, 32.0);

    }

    public EndermanBossEntity(EntityType<? extends Monster> entityType, Level level) {

        super(entityType, level);
        this.setNoGravity(true);

    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    /* -------Server Save/Setup------- */

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BOSS_PHASE, PHASE_1);
        builder.define(BOSS_ATTACKS, ATTACK_NONE);
        builder.define(TIMER_STAGE_EYE_OF_END_ATTACK, CLOSED);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if(BOSS_PHASE.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Phase", this.getBossPhase());
        compound.putInt("Attack", this.getAttack());
        compound.putInt("EyeOfEndAttack", this.getTimerStageEyeOfEndAttack());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(compound.contains("Phase")) {
            this.setBossPhase(compound.getInt("Phase"));
        }
        this.setAttack(compound.getInt("Attack"));
        this.setTimerStageEyeOfEndAttack(compound.getInt("EyeOfEndAttack"));
    }

    @Override
    public void startSeenByPlayer(ServerPlayer pServerLevel) {
        super.startSeenByPlayer(pServerLevel);
        this.bossEvent.addPlayer(pServerLevel);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer pServerLevel) {
        super.stopSeenByPlayer(pServerLevel);
        this.bossEvent.removePlayer(pServerLevel);
    }



    /* --------Boss actions and animations----------- */

    @Override
    public void aiStep() { //Server
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth()/this.getMaxHealth());
        if(!this.level().isClientSide()) {
            Player nearestPlayer = this.level().getNearestPlayer(this, 7.0D);
            Player range = this.level().getNearestPlayer(this, 60.0D);
            Player far = this.level().getNearestPlayer(this, 50.0D);
            if(range != null) {
                if (this.activeAttack != null) {
                    this.activeAttack.tick(this);

                    if (this.activeAttack.isFinished()) {
                        this.activeAttack.stop(this);
                        this.activeAttack = null;
                        this.setAttack(ATTACK_NONE);
                        attackCooldown = 10;
                    }
                    return;
                } else {
                    if (nearestPlayer != null) {
                        this.teleportRandom();
                    }
                    if (far == null) {
                        this.teleportNearPlayer(range);
                    }
                }

                if (this.attackCooldown > 0) {
                    this.attackCooldown--;
                } else {
                    startNewAttack();
                }
            }



            // discards all summon enderman //
            List<Player> players = this.level().getEntitiesOfClass(
                    Player.class,
                    this.getBoundingBox().inflate(64),
                    p -> p.isAlive()
            );

            boolean noPlayersNearby = players.isEmpty();

            if (noPlayersNearby || this.isDeadOrDying()) {
                cleanupMinions();
            }

            // ----------------------------- //
        }
    }

    private int flexibleTriggerTimer = 0;
    @Override
    public void tick() { //Client(animations)
        super.tick();

        if(this.getBossPhase() == PHASE_1) {
            if(this.getAttack() == CHARGE_RADIAL_ATTACK) {
                flexibleTriggerTimer++;
                this.chargeAttackAnimationState.startIfStopped(this.tickCount);
                if(flexibleTriggerTimer == 100) {
                    ModClientEvents.triggerFlash();
                }
            } else if (this.getAttack() == SUMMON_ATTACK) {
                this.summonAttackAnimationState.startIfStopped(this.tickCount);
            }
        }
        if(this.getAttack() == ATTACK_NONE) {
            this.chargeAttackAnimationState.stop();
            this.summonAttackAnimationState.stop();
            flexibleTriggerTimer = 0;
        }
    }

    /* --------Teleportation--------- */

    protected boolean teleportRandom() {
        if(!this.level().isClientSide() && this.isAlive()) {
            //Try multiple times(16 times total)
            for(int i = 0; i < 16; ++i) {
                double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 11.0;
                double d1 = this.getY() + (double) (this.random.nextInt(64) - 32); // down and up
                double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 11.0;

                if (this.teleport(d0, d1, d2)) {
                    Player player = this.level().getNearestPlayer(this, 50); // range tweakable

                    if(player != null) {
                        double dx = player.getX() - this.getX();
                        double dz = player.getZ() - this.getZ();

                        float yaw = (float) (Math.atan2(dz, dx) * (180F / Math.PI)) - 90F;

                        this.setYRot(yaw);
                        this.setYHeadRot(yaw);
                        this.setYBodyRot(yaw);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean teleportNearPlayer(Player player) {
        if(!this.level().isClientSide() && this.isAlive()) {
            //Try multiple times(16 times total)
            for(int i = 0; i < 16; ++i) {
                if(player != null) {
                    double d0 = player.getX() + (this.random.nextDouble() - 0.5) * 15.0;
                    double d1 = player.getY() + (double) (this.random.nextInt(64) - 32); // down and up
                    double d2 = player.getZ() + (this.random.nextDouble() - 0.5) * 15.0;

                    if (this.teleport(d0, d1, d2)) {
                        // Direction to player
                        double dx = player.getX() - this.getX();
                        double dz = player.getZ() - this.getZ();

                        // Convert to yaw
                        float yaw = (float)(Math.atan2(dz, dx) * (180F / Math.PI)) - 90F;

                        // Apply rotation
                        this.setYRot(yaw);
                        this.setYHeadRot(yaw);
                        this.setYBodyRot(yaw);

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
            boolean success = this.randomTeleport(event.getTargetX(), event.getTargetY()+2, event.getTargetZ(), true);

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

    /* ----------ATTACKS--------- */

    private void startNewAttack() {
        Player close = this.level().getNearestPlayer(this, 9.0D);
        System.out.println("Attack is being chosem");
        int phase = this.getBossPhase();
        System.out.println("Phase" + phase);
        if(phase == PHASE_1) {
            if(close == null) {
                int randomPick = this.random.nextInt(1, 5);
                System.out.println("Random number: " + randomPick);
                if (randomPick == 1) {
                    System.out.println("Charge Attack is being chosen");
                    this.activeAttack = new ChargeRadialAttack();
                } else if (randomPick == 2) {
                    System.out.println("Summon Attack is being chose");
                    this.activeAttack = new SummonAttack();
                } else if (randomPick == 3) {
                    System.out.println("Eye Of End Attack is being chosen");
                    this.activeAttack = new EyeOfEndAttack();
                } else if (randomPick == 4) {
                    this.activeAttack = new SpikeAttack();
                }
            } else {
                this.activeAttack = new KnockbackAttack();
            }
        }
        if(this.activeAttack != null) {
            this.activeAttack.start(this);
        }
    }

    public final void setAttack(int currAttack) {
        this.entityData.set(BOSS_ATTACKS, currAttack);
    }

    public final int getAttack() {
        return this.entityData.get(BOSS_ATTACKS);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!this.level().isClientSide() && this.isAlive()) {
            if(this.getAttack() == CHARGE_RADIAL_ATTACK) {
                this.setAttack(ATTACK_NONE);
                this.chargeAttackAnimationState.stop();
            }
            this.teleportRandom();
        }
        return super.hurt(source, amount);
    }

    /* -------PHASES------- */

    public final void setBossPhase(int currBossPhase) {
        this.entityData.set(BOSS_PHASE, currBossPhase);
    }

    public final int getBossPhase() {
        return this.entityData.get(BOSS_PHASE);
    }

    /* -----EYE OF END ATTACK SYSTEM DATA------ */

    private static final EntityDataAccessor<Integer> TIMER_STAGE_EYE_OF_END_ATTACK =
            SynchedEntityData.defineId(EndermanBossEntity.class, EntityDataSerializers.INT);
    public final static int CLOSED = 1;
    public final static int WARNING = 2;
    public final static int OPEN = 3;
    public final static int HIDDEN = 0;

    public final void setTimerStageEyeOfEndAttack(int timerStage) {
        this.entityData.set(TIMER_STAGE_EYE_OF_END_ATTACK, timerStage);

    }

    public final int getTimerStageEyeOfEndAttack() {
        return this.entityData.get(TIMER_STAGE_EYE_OF_END_ATTACK);
    }

    /* --------CLEANUP/SAFETY MEASUREMENTS-------- */
    public void cleanupMinions() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        List<EnderMan> minions = serverLevel.getEntitiesOfClass(
                EnderMan.class,
                this.getBoundingBox().inflate(64), // radius
                e -> e.getTags().contains("boss_minion")
        );

        for (EnderMan enderman : minions) {
            enderman.discard();
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if(!this.level().isClientSide) {
            cleanupMinions();
        }
    }
}
