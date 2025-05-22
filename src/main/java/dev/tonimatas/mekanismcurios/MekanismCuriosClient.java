package dev.tonimatas.mekanismcurios;

import dev.tonimatas.mekanismcurios.networking.ModMessages;
import dev.tonimatas.mekanismcurios.networking.packet.OpenPortableQIOPacket;
import dev.tonimatas.mekanismcurios.util.CuriosSlots;
import dev.tonimatas.mekanismcurios.util.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class MekanismCuriosClient {
    @Mod.EventBusSubscriber(modid = MekanismCurios.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(InputEvent event) {
            while (KeyBinding.PORTABLE_QIO_MAPPING.get().consumeClick()) {
                ModMessages.sendToServer(new OpenPortableQIOPacket(CuriosSlots.QIO));
            }

            while (KeyBinding.PORTABLE_TELEPORTER_MAPPING.get().consumeClick()) {
                ModMessages.sendToServer(new OpenPortableQIOPacket(CuriosSlots.TELEPORTER));
            }
        }
    }
    
    @Mod.EventBusSubscriber(modid = MekanismCurios.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.PORTABLE_QIO_MAPPING.get());
            event.register(KeyBinding.PORTABLE_TELEPORTER_MAPPING.get());
        }
    }
}
