package net.sussyit.endermanbossmod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.sussyit.endermanbossmod.EndermanBossMod;

@EventBusSubscriber(modid = EndermanBossMod.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {
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
