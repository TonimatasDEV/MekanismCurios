package dev.tonimatas.mekanismcurios.mixins;


import dev.tonimatas.mekanismcurios.MekanismCurios;
import mekanism.common.network.to_server.PacketGuiSetFrequency;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PacketGuiSetFrequency.class)
public class PacketSetItemFrequencyMixin {
    @Redirect(method = "handle", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack getItemInHand(ServerPlayer instance, InteractionHand interactionHand) {
        return MekanismCurios.getHandOrCuriosItem(instance, interactionHand);
    }

    @Redirect(method = "encode", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeEnum(Ljava/lang/Enum;)Lnet/minecraft/network/FriendlyByteBuf;", ordinal = 1))
    private FriendlyByteBuf mci$encode(FriendlyByteBuf instance, Enum<?> pValue) {
        instance.writeNullable(pValue, FriendlyByteBuf::writeEnum);
        return instance;
    }

    @Redirect(method = "decode", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readEnum(Ljava/lang/Class;)Ljava/lang/Enum;", ordinal = 1))
    private static Enum<?> mci$decode(FriendlyByteBuf instance, Class<?> pEnumClass) {
        return instance.readNullable(b -> b.readEnum(InteractionHand.class));
    }
}
