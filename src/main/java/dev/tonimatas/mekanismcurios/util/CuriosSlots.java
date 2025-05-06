package dev.tonimatas.mekanismcurios.util;

import io.netty.buffer.ByteBuf;
import mekanism.common.network.PacketUtils;
import net.minecraft.network.codec.StreamCodec;

import java.util.Locale;

public enum CuriosSlots {
    QIO,
    TELEPORTER;
    
    public String id() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }

    public static final StreamCodec<ByteBuf, CuriosSlots> CURIOS_SLOT_STREAM_CODEC = PacketUtils.enumCodec(CuriosSlots.class);
}
