package dev.tonimatas.mekanismcurios.util;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public enum CuriosSlots {
    QIO,
    TELEPORTER,
    QUICK_TELEPORT;

    public Item getItem() {
        return switch (this) {
            case QIO -> MekanismItems.PORTABLE_QIO_DASHBOARD.get();
            case TELEPORTER -> MekanismItems.PORTABLE_TELEPORTER.get();
            default -> ItemStack.EMPTY.getItem();
        };
    }

    public ItemStack getItemStack(Player player) {
        SlotContext slotContext = MekanismCurios.getFirstCurios(player, getItem());
        if (slotContext == null) return ItemStack.EMPTY;
        return MekanismCurios.getCuriosSlot(player, slotContext).stack();
    }
}
