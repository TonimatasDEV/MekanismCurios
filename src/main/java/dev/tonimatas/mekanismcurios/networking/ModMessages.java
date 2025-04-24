package dev.tonimatas.mekanismcurios.networking;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import dev.tonimatas.mekanismcurios.networking.packet.OpenPortableQIOPacket;
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
                .decoder(OpenPortableQIOPacket::new)
                .encoder(OpenPortableQIOPacket::toBytes)
                .consumerMainThread(OpenPortableQIOPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
