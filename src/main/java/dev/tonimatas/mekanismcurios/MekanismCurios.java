package dev.tonimatas.mekanismcurios;

import com.mojang.logging.LogUtils;
import dev.tonimatas.mekanismcurios.networking.OpenPortableQIOPacket;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
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
    
    public static ItemStack getQIO(Player player) {
        return CuriosApi.getCuriosInventory(player).map(iCuriosItemHandler -> 
                iCuriosItemHandler.getCurios().get("qio").getStacks().getStackInSlot(0)).orElse(ItemStack.EMPTY);
    }

    public static void setQIO(Player player, ItemStack stack) {
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> 
                curiosInventory.setEquippedCurio("qio", 0, stack));
    }

    public static ItemStack getHandOrCuriosItem(Player player, InteractionHand hand) {
        if (hand == null) {
            return MekanismCurios.getQIO(player);
        } else {
            return player.getItemInHand(hand);
        }
    }
}
