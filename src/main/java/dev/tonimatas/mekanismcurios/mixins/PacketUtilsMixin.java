package dev.tonimatas.mekanismcurios.mixins;

import io.netty.buffer.ByteBuf;
import mekanism.common.network.PacketUtils;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PacketUtils.class)
public class PacketUtilsMixin {
    @Inject(method = "enumCodec", at = @At("HEAD"), cancellable = true)
    private static <V extends Enum<V>> void mci$enumCodec(Class<V> enumClass, CallbackInfoReturnable<StreamCodec<ByteBuf, V>> cir) {
        cir.setReturnValue(ByteBufCodecs.optional(
                ByteBufCodecs.idMapper(ByIdMap.continuous(Enum::ordinal, enumClass.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.WRAP), Enum::ordinal)
                ).map(opt -> opt.orElse(null), Optional::ofNullable));
    }
}
