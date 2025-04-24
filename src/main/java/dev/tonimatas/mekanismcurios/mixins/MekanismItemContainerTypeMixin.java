package dev.tonimatas.mekanismcurios.mixins;

import mekanism.common.inventory.container.type.MekanismItemContainerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MekanismItemContainerType.class)
public class MekanismItemContainerTypeMixin {
    @Redirect(method = "lambda$item$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/RegistryFriendlyByteBuf;readEnum(Ljava/lang/Class;)Ljava/lang/Enum;"))
    private static Enum<?> mci$item$0(RegistryFriendlyByteBuf instance, Class<FriendlyByteBuf> aClass) {
        return instance.readNullable(b -> b.readEnum(InteractionHand.class));
    }

    @Redirect(method = "lambda$item$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/RegistryFriendlyByteBuf;readEnum(Ljava/lang/Class;)Ljava/lang/Enum;"))
    private static Enum<?> mci$item$1(RegistryFriendlyByteBuf instance, Class<FriendlyByteBuf> aClass) {
        return instance.readNullable(b -> b.readEnum(InteractionHand.class));
    }
    
    @Redirect(method = "lambda$qioDashboard$3", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/RegistryFriendlyByteBuf;readEnum(Ljava/lang/Class;)Ljava/lang/Enum;"))
    private static Enum<?> mci$qioDashboard(RegistryFriendlyByteBuf instance, Class<FriendlyByteBuf> aClass) {
        return instance.readNullable(b -> b.readEnum(InteractionHand.class));
    }
}
