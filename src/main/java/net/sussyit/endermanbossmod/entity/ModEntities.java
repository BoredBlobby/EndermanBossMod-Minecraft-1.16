package net.sussyit.endermanbossmod.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.sussyit.endermanbossmod.entity.custom.EndermanBossEntity;
import net.sussyit.endermanbossmod.entity.custom.EndermanEyeEntity;
import net.sussyit.endermanbossmod.entity.custom.EyeOfEndEntity;
import net.sussyit.endermanbossmod.entity.custom.SpikeEntity;

import java.util.function.Supplier;

public class ModEntities {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
                DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, EndermanBossMod.MOD_ID);

        public static final Supplier<EntityType<EndermanBossEntity>> ENDERMANBOSS =
                ENTITY_TYPES.register("endermanboss", () -> EntityType.Builder.of(EndermanBossEntity::new, MobCategory.MONSTER)
                        .sized(1.0f, 3.0f).build("endermanboss"));
        public static final Supplier<EntityType<EndermanEyeEntity>> ENDERMANEYE =
                ENTITY_TYPES.register("endermaneye", () -> EntityType.Builder.of(EndermanEyeEntity::new, MobCategory.MISC)
                        .sized(0.5f, 0.5f).build("endermaneye"));
        public static final Supplier<EntityType<EyeOfEndEntity>> EYEOFEND =
                ENTITY_TYPES.register("eyeofend", () -> EntityType.Builder.of(EyeOfEndEntity::new, MobCategory.MISC)
                        .sized(7.0f, 5.0f).build("eyeofend"));
        public static final Supplier<EntityType<SpikeEntity>> SPIKE =
                ENTITY_TYPES.register("spike", () -> EntityType.Builder.of(SpikeEntity::new, MobCategory.MISC)
                        .sized(1.0f, 3.0f).build("spike"));
        public static void register(IEventBus eventBus) { ENTITY_TYPES.register(eventBus); }
}
