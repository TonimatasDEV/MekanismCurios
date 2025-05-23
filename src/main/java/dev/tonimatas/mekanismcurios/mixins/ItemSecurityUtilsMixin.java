package dev.tonimatas.mekanismcurios.mixins;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import mekanism.common.lib.security.ItemSecurityUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemSecurityUtils.class)
public class ItemSecurityUtilsMixin {
    @Redirect(method = "claimOrOpenGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack mci$claimOrOpenGui$getItemInHand(Player instance, InteractionHand interactionHand) {
        return MekanismCurios.getHandOrCuriosItem(instance, interactionHand);
    }
}
