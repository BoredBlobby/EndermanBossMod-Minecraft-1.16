package net.sussyit.endermanbossmod.entity.client;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;
import net.sussyit.endermanbossmod.sounds.ModSounds;

public class ChargeRadialSound extends AbstractTickableSoundInstance {
    private final EndermanBossEntity endermanBossEntity;

    public ChargeRadialSound(EndermanBossEntity endermanBossEntity) {
        super(ModSounds.CHARGE_RADIAL.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.endermanBossEntity = endermanBossEntity;
        this.looping = true;
    }

    @Override
    public void tick() {
        if(endermanBossEntity.getAttack() == EndermanBossEntity.ATTACK_NONE) {
            this.stop();
        }

        this.x = endermanBossEntity.getX();
        this.y = endermanBossEntity.getY();
        this.z = endermanBossEntity.getZ();
    }
}
