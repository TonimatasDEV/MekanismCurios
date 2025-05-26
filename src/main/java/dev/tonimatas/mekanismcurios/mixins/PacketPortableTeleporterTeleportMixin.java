package dev.tonimatas.mekanismcurios.mixins;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import mekanism.common.network.to_server.PacketPortableTeleporterTeleport;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PacketPortableTeleporterTeleport.class)
public class PacketPortableTeleporterTeleportMixin {
    @Redirect(method = "handle", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack mci$handle$getItemInHand(ServerPlayer instance, InteractionHand interactionHand) {
        return MekanismCurios.getHandOrCuriosItem(instance, interactionHand);
    }

    @Redirect(method = "decode", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readEnum(Ljava/lang/Class;)Ljava/lang/Enum;"))
    private static <T extends Enum<T>> T redirectReadEnum(FriendlyByteBuf buf, Class<T> enumClass) {
        return buf.readNullable(friendlyByteBuf -> friendlyByteBuf.readEnum(enumClass));
    }
    
    @Redirect(method = "encode", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeEnum(Ljava/lang/Enum;)Lnet/minecraft/network/FriendlyByteBuf;"))
    private FriendlyByteBuf mci$decode(FriendlyByteBuf instance, Enum<?> pValue) {
        instance.writeNullable(pValue, FriendlyByteBuf::writeEnum);
        return null;
    }
}
