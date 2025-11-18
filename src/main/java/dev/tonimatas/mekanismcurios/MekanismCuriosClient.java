package dev.tonimatas.mekanismcurios;

import dev.tonimatas.mekanismcurios.networking.OpenSlotActionPacket;
import dev.tonimatas.mekanismcurios.networking.QuickTeleportActionPacket;
import dev.tonimatas.mekanismcurios.util.CuriosSlots;
import dev.tonimatas.mekanismcurios.util.KeyBinding;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class MekanismCuriosClient {
    @EventBusSubscriber(modid = MekanismCurios.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            while (KeyBinding.PORTABLE_QIO_MAPPING.get().consumeClick()) {
                PacketDistributor.sendToServer(new OpenSlotActionPacket(CuriosSlots.QIO));
            }

            while (KeyBinding.PORTABLE_TELEPORTER_MAPPING.get().consumeClick()) {
                PacketDistributor.sendToServer(new OpenSlotActionPacket(CuriosSlots.TELEPORTER));
            }

            while (KeyBinding.QUICK_TELEPORT_MAPPING.get().consumeClick()) {
            PacketDistributor.sendToServer(new QuickTeleportActionPacket());
            }
        }
    }
    
    @EventBusSubscriber(modid = MekanismCurios.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.PORTABLE_QIO_MAPPING.get());
            event.register(KeyBinding.PORTABLE_TELEPORTER_MAPPING.get());
            event.register(KeyBinding.QUICK_TELEPORT_MAPPING.get());
        }
    }
}
