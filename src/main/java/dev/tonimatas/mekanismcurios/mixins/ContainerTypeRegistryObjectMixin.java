package dev.tonimatas.mekanismcurios.mixins;

import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ContainerTypeRegistryObject.class)
public class ContainerTypeRegistryObjectMixin {
    @Redirect(method = "lambda$tryOpenGui$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeEnum(Ljava/lang/Enum;)Lnet/minecraft/network/FriendlyByteBuf;"))
    private static FriendlyByteBuf mci$tryOpenGui(FriendlyByteBuf instance, Enum<?> pValue) {
        instance.writeNullable(pValue, FriendlyByteBuf::writeEnum);
        return instance;
    }
}
