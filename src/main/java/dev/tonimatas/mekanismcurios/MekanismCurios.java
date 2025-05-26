package dev.tonimatas.mekanismcurios;

import com.mojang.logging.LogUtils;
import dev.tonimatas.mekanismcurios.bridge.PlayerBridge;
import dev.tonimatas.mekanismcurios.networking.ModMessages;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mod(MekanismCurios.MODID)
public class MekanismCurios {
    public static final String MODID = "mekanismcurios";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MekanismCurios(FMLJavaModLoadingContext context) {
        ModMessages.register();

        LOGGER.info("Mekanism Curios initialized successfully.");
    }

    public static ItemStack getSlot(Player player) {
        return ((PlayerBridge) player).mci$getSlot().getItemStack(player);
    }

    public static ItemStack getHandOrCuriosItem(Player player, InteractionHand hand) {
        if (hand == null) {
            return MekanismCurios.getSlot(player);
        } else {
            return player.getItemInHand(hand);
        }
    }

    public static SlotResult getCuriosSlot(Player player, SlotContext slotContext) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player).resolve();
        return curiosInventory.flatMap(iCuriosItemHandler -> iCuriosItemHandler.findCurio(slotContext.identifier(), slotContext.index())).orElse(null);
    }

    public static SlotContext getFirstCurios(Player player, Item item) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player).resolve();
        Optional<SlotResult> firstCurios = curiosInventory.flatMap(iCuriosItemHandler -> iCuriosItemHandler.findFirstCurio(item));
        return firstCurios.map(SlotResult::slotContext).orElse(null);
    }
}
