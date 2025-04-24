package dev.tonimatas.mekanismcurios.networking;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import mekanism.api.security.IItemSecurityUtils;
import mekanism.common.item.ItemPortableQIODashboard;
import mekanism.common.lib.security.ItemSecurityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.Optional;

public record OpenPortableQIOPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenPortableQIOPacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MekanismCurios.MODID, "portable_qio_packet"));

    public static final StreamCodec<FriendlyByteBuf, OpenPortableQIOPacket> STREAM_CODEC = StreamCodec.unit(new OpenPortableQIOPacket());
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    @SuppressWarnings("DataFlowIssue")
    public static void handle(final OpenPortableQIOPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            ItemStack stack = MekanismCurios.getQIO(player);
            
            if (stack.getItem() instanceof ItemPortableQIODashboard itemPortableQIODashboard) {
                
            }
            
            stack.use(level, player, null);
            // Mekanism start - Modified code of ItemSecurityUtils#claimOrOpenGui
            //if (!stack.isEmpty() && stack.getItem() instanceof ItemPortableQIODashboard portableQIO) {
            //    if (!ItemSecurityUtils.get().tryClaimItem(level, player, stack)) {
            //        if (IItemSecurityUtils.INSTANCE.canAccessOrDisplayError(player, stack) && stack.getCount() == 1 && !level.isClientSide) {
            //            portableQIO.getContainerType().tryOpenGui((ServerPlayer) player, null, stack);
            //        }
            //    }
            //}
            // Mekanism stop
        });
    }
}
