package dev.tonimatas.mekanismcurios.util;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import io.netty.buffer.ByteBuf;
import mekanism.common.network.PacketUtils;
import mekanism.common.registries.MekanismItems;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public enum CuriosSlots {
    QIO,
    TELEPORTER;
    
    public Item getItem() {
        return switch (this) {
            case QIO -> MekanismItems.PORTABLE_QIO_DASHBOARD.get();
            case TELEPORTER -> MekanismItems.PORTABLE_TELEPORTER.get();
        };
    }
    
    public ItemStack getItemStack(Player player) {
        SlotContext slotContext = MekanismCurios.getFirstCurios(player, getItem());
        if (slotContext == null) return ItemStack.EMPTY;
        return MekanismCurios.getCuriosSlot(player, slotContext).stack();
    }

    public static final StreamCodec<ByteBuf, CuriosSlots> CURIOS_SLOT_STREAM_CODEC = PacketUtils.enumCodec(CuriosSlots.class);
}
