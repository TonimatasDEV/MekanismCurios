package dev.tonimatas.mekanismcurios;

import com.mojang.logging.LogUtils;
import dev.tonimatas.mekanismcurios.networking.ModMessages;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.util.Optional;

@Mod(MekanismCurios.MODID)
public class MekanismCurios {
    public static final String MODID = "mekanismcurios";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MekanismCurios(FMLJavaModLoadingContext context) {
        ModMessages.register();
        context.getModEventBus().addListener(this::enqueueIMC);

        LOGGER.info("Mekanism Curios initialized successfully.");
    }

    public void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, 
                () -> new SlotTypeMessage.Builder("qio")
                        .size(1)
                        .icon(ResourceLocation.tryBuild(CuriosApi.MODID, "slot/empty_qio_slot"))
                        .build());
    }
    
    public static ItemStack getQIO(Player player) {
        Optional<SlotResult> result = CuriosApi.getCuriosHelper().findCurio(player, "qio", 0);
        
        if (result.isPresent()) {
            return result.get().stack();
        }
        
        return ItemStack.EMPTY;
    }

    public static ItemStack getHandOrCuriosItem(Player player, InteractionHand hand) {
        if (hand == null) {
            return MekanismCurios.getQIO(player);
        } else {
            return player.getItemInHand(hand);
        }
    }
}
