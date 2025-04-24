package dev.tonimatas.mekanismcurios.mixins;

import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ContainerTypeRegistryObject.class)
public class ContainerTypeRegistryObjectMixin {
    @Redirect(method = "lambda$tryOpenGui$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/RegistryFriendlyByteBuf;writeEnum(Ljava/lang/Enum;)Lnet/minecraft/network/FriendlyByteBuf;"))
    private static FriendlyByteBuf mci$tryOpenGui(RegistryFriendlyByteBuf instance, Enum<?> anEnum) {
        instance.writeNullable(anEnum, FriendlyByteBuf::writeEnum);
        return instance;
    }
}
