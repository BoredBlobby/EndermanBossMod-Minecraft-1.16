package net.sussyit.endermanbossmod.event;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;

@EventBusSubscriber(modid = EndermanBossMod.MOD_ID)
public class ModServerEvents {
    @SubscribeEvent
    public static void onLivingHurt(Pre event) {
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();

        // Check for lightning damage
        if (source.is(DamageTypes.LIGHTNING_BOLT)) {

            if (entity instanceof EndermanBossEntity) {
                event.setNewDamage(0.0f);
                return;
            }

            if (entity.getTags().contains("boss_minion")) {
                event.setNewDamage(0.0f);
            }
        }
    }
}
