package dev.tonimatas.mekanismcurios.networking.packet;


import dev.tonimatas.mekanismcurios.MekanismCurios;
import dev.tonimatas.mekanismcurios.bridge.PlayerBridge;
import dev.tonimatas.mekanismcurios.util.CuriosSlots;
import mekanism.common.item.interfaces.IGuiItem;
import mekanism.common.util.SecurityUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenPortableQIOPacket {
    private final CuriosSlots slot;
    
    public OpenPortableQIOPacket(CuriosSlots slot) {
        this.slot = slot;
    }

    public void encode(FriendlyByteBuf buf) {
            buf.writeEnum(slot);
    }
    
    public static OpenPortableQIOPacket decode(FriendlyByteBuf buf) {
        return new OpenPortableQIOPacket(buf.readEnum(CuriosSlots.class));
    }
    
    @SuppressWarnings({"DataFlowIssue", "unused", "UnusedReturnValue"})
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level level = player.level();

            ((PlayerBridge) player).mci$setSlot(this.slot);

            ItemStack stack = this.slot.getItemStack(player);;

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof IGuiItem item) {
                    SecurityUtils.get().claimOrOpenGui(level, player, null, item.getContainerType()::tryOpenGui);
                }
            }
        });

        return true;
    }
}
