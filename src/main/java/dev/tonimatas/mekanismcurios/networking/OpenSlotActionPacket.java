package dev.tonimatas.mekanismcurios.networking;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import dev.tonimatas.mekanismcurios.bridge.PlayerBridge;
import dev.tonimatas.mekanismcurios.util.CuriosSlots;
import mekanism.common.item.interfaces.IGuiItem;
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

public record OpenSlotActionPacket(CuriosSlots slot) implements CustomPacketPayload {
    public static final Type<OpenSlotActionPacket> TYPE = 
            new Type<>(ResourceLocation.fromNamespaceAndPath(MekanismCurios.MODID, "portable_qio_packet"));

    public static final StreamCodec<FriendlyByteBuf, OpenSlotActionPacket> STREAM_CODEC = StreamCodec.composite(
            CuriosSlots.CURIOS_SLOT_STREAM_CODEC, OpenSlotActionPacket::slot,
            OpenSlotActionPacket::new
    );
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    @SuppressWarnings({"DataFlowIssue", "unused"})
    public static void handle(final OpenSlotActionPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();

            ((PlayerBridge) player).mci$setSlot(packet.slot);

            ItemStack stack = packet.slot.getItemStack(player);
            
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof IGuiItem item) {
                    ItemSecurityUtils.get().claimOrOpenGui(level, player, null, item.getContainerType()::tryOpenGui);
                }
            }
        });
    }
}
