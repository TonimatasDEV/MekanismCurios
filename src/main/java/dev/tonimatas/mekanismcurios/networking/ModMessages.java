package dev.tonimatas.mekanismcurios.networking;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import dev.tonimatas.mekanismcurios.networking.packet.OpenPortableQIOPacket;
import dev.tonimatas.mekanismcurios.networking.packet.QuickTeleportActionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.tryBuild(MekanismCurios.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(OpenPortableQIOPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(OpenPortableQIOPacket::decode)
                .encoder(OpenPortableQIOPacket::encode)
                .consumerMainThread(OpenPortableQIOPacket::handle)
                .add();

        net.messageBuilder(QuickTeleportActionPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(QuickTeleportActionPacket::decode)
                .encoder(QuickTeleportActionPacket::encode)
                .consumerMainThread(QuickTeleportActionPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
