package dev.tonimatas.mekanismcurios.networking;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import mekanism.common.item.ItemPortableQIODashboard;
import mekanism.common.lib.security.ItemSecurityUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenPortableQIOPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenPortableQIOPacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MekanismCurios.MODID, "portable_qio_packet"));

    public static final StreamCodec<FriendlyByteBuf, OpenPortableQIOPacket> STREAM_CODEC = StreamCodec.unit(new OpenPortableQIOPacket());
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    @SuppressWarnings({"DataFlowIssue", "unused"})
    public static void handle(final OpenPortableQIOPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            ItemStack stack = MekanismCurios.getQIO(player);
            
            if (!stack.isEmpty() && stack.getItem() instanceof ItemPortableQIODashboard item) {
                ItemSecurityUtils.get().claimOrOpenGui(level, player, null, item.getContainerType()::tryOpenGui);
            }
        });
    }
}
