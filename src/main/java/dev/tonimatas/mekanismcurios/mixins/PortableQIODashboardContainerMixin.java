package dev.tonimatas.mekanismcurios.mixins;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import mekanism.common.inventory.container.item.PortableQIODashboardContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortableQIODashboardContainer.class)
public abstract class PortableQIODashboardContainerMixin {
    @Shadow @Final protected InteractionHand hand;

    @Final
    @Shadow protected ItemStack stack;

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    private void mci$stillValid(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this.hand == null) {
            ItemStack curiosStack = MekanismCurios.getQIO(player);
            boolean validCurios = !curiosStack.isEmpty() && (curiosStack.is(this.stack.getItem()));
            cir.setReturnValue(validCurios);
        }
    }
}
