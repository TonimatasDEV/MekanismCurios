package dev.tonimatas.mekanismcurios.bridge;

import dev.tonimatas.mekanismcurios.util.CuriosSlots;

public interface PlayerBridge {
    void mci$setSlot(CuriosSlots slot);
    CuriosSlots mci$getSlot();
}
