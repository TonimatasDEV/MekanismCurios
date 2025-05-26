package dev.tonimatas.mekanismcurios;

import com.mojang.logging.LogUtils;
import dev.tonimatas.mekanismcurios.bridge.PlayerBridge;
import dev.tonimatas.mekanismcurios.networking.OpenPortableQIOPacket;
import dev.tonimatas.mekanismcurios.util.CuriosSlots;
import mekanism.common.registries.MekanismItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mod(MekanismCurios.MODID)
public class MekanismCurios {
    public static final String MODID = "mekanismcurios";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MekanismCurios(final IEventBus modBus) {
        modBus.addListener(this::registerCapabilities);
        modBus.addListener(this::registerNetworking);
        LOGGER.info("Mekanism Curios initialized successfully.");
    }

    public void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerItem(CuriosCapability.ITEM, (stack, context) -> () -> stack, MekanismItems.PORTABLE_QIO_DASHBOARD.get());
    }

    @SubscribeEvent
    public void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.commonToServer(OpenPortableQIOPacket.TYPE, OpenPortableQIOPacket.STREAM_CODEC, new MainThreadPayloadHandler<>(OpenPortableQIOPacket::handle));
    }
    
    public static ItemStack getSlot(Player player) {
        return ((PlayerBridge) player).mci$getSlot().getItemStack(player);
    }

    public static void setSlot(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer) {
            CuriosSlots slot = ((PlayerBridge) player).mci$getSlot();
            SlotContext slotContext = getFirstCurios(player, slot.getItem());
            
            if (slotContext == null) return;

            CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                    curiosInventory.setEquippedCurio(slotContext.identifier(), slotContext.index(), stack));
        }
    }

    public static ItemStack getHandOrCuriosItem(Player player, InteractionHand hand) {
        if (hand == null) {
            return MekanismCurios.getSlot(player);
        } else {
            return player.getItemInHand(hand);
        }
    }

    public static SlotResult getCuriosSlot(Player player, SlotContext slotContext) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
        return curiosInventory.flatMap(iCuriosItemHandler -> iCuriosItemHandler.findCurio(slotContext.identifier(), slotContext.index())).orElse(null);
    }

    public static SlotContext getFirstCurios(Player player, Item item) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
        Optional<SlotResult> firstCurios = curiosInventory.flatMap(iCuriosItemHandler -> iCuriosItemHandler.findFirstCurio(item));
        return firstCurios.map(SlotResult::slotContext).orElse(null);
    }
}
