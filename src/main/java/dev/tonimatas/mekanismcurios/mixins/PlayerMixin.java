package dev.tonimatas.mekanismcurios.mixins;

import dev.tonimatas.mekanismcurios.bridge.PlayerBridge;
import dev.tonimatas.mekanismcurios.util.CuriosSlots;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements PlayerBridge {
    @Unique
    private CuriosSlots mci$slot = null;
    
    @Override
    public void mci$setSlot(CuriosSlots slot) {
        this.mci$slot = slot;
    }

    @Override
    public CuriosSlots mci$getSlot() {
        return mci$slot;
    }
}
