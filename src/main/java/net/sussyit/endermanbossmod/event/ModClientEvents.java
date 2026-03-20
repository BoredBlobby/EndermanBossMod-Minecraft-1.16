package net.sussyit.endermanbossmod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.sussyit.endermanbossmod.EndermanBossMod;
import net.sussyit.endermanbossmod.util.CameraShakeUtils;

@EventBusSubscriber(modid = EndermanBossMod.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        // whne the shake and zoom are called, it counts down from the tick so the event actually happens

        // This runs every tick on the client side
        CameraShakeUtils.applyShake(Minecraft.getInstance());
        // Handle the shaking logic

        // Handle the FOV zooming logic
        CameraShakeUtils.tickFov();
    }

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (CameraShakeUtils.isPlayerFrozen()) {
            // This clears all keyboard input (WASD, Jump, Sneak)
            event.getInput().forwardImpulse = 0;
            event.getInput().leftImpulse = 0;
            event.getInput().up = false;
            event.getInput().down = false;
            event.getInput().left = false;
            event.getInput().right = false;
            event.getInput().jumping = false;
            event.getInput().shiftKeyDown = false;
        }
    }

    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        // Add your custom offset to whatever the current FOV is
        // (this keeps it compatible with sprinting/potions)
        float currentModifier = event.getFovModifier();
        event.setNewFovModifier(currentModifier + CameraShakeUtils.getFovModifier());
    }


    private static int flashTime = 60;
    private final static int MAX_FLASH_TIME = 60;

    public static void triggerFlash() {
        flashTime = MAX_FLASH_TIME;
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if(flashTime > 0) {
            Minecraft mc = Minecraft.getInstance();

            float alpha = (float) flashTime/ MAX_FLASH_TIME;

            GuiGraphics gui = event.getGuiGraphics();
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            int color = ((int) (alpha * 255) << 24) | 0xFFFFFF;

            gui.fill(0,0,width, height, color);

            flashTime--;
        }
    }
}
