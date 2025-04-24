package dev.tonimatas.mekanismcurios.networking.packet;


import dev.tonimatas.mekanismcurios.MekanismCurios;
import mekanism.common.item.ItemPortableQIODashboard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenPortableQIOPacket {
    public OpenPortableQIOPacket() {}

    public OpenPortableQIOPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}
    
    @SuppressWarnings({"DataFlowIssue", "unused", "UnusedReturnValue"})
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level level = player.level();
            ItemStack stack = MekanismCurios.getQIO(player);
            
            if (!stack.isEmpty() && stack.getItem() instanceof ItemPortableQIODashboard) {
                stack.use(level, player, null);
            }
        });

        return true;
    }
}
