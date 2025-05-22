package dev.tonimatas.mekanismcurios;

import com.mojang.logging.LogUtils;
import dev.tonimatas.mekanismcurios.bridge.PlayerBridge;
import dev.tonimatas.mekanismcurios.networking.ModMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(MekanismCurios.MODID)
public class MekanismCurios {
    public static final String MODID = "mekanismcurios";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MekanismCurios(FMLJavaModLoadingContext context) {
        ModMessages.register();

        LOGGER.info("Mekanism Curios initialized successfully.");
    }

    public static ItemStack getSlot(Player player) {
        String slot = ((PlayerBridge) player).mci$getSlot().id();
        return CuriosApi.getCuriosInventory(player).map(iCuriosItemHandler -> 
                iCuriosItemHandler.getCurios().get(slot).getStacks().getStackInSlot(0)).orElse(ItemStack.EMPTY);
    }

    public static void setSlot(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer) {
            String slot = ((PlayerBridge) player).mci$getSlot().id();

            CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                    curiosInventory.setEquippedCurio(slot, 0, stack));
        }
    }

    public static ItemStack getHandOrCuriosItem(Player player, InteractionHand hand) {
        if (hand == null) {
            return MekanismCurios.getSlot(player);
        } else {
            return player.getItemInHand(hand);
        }
    }
}
