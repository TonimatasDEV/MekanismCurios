package dev.tonimatas.mekanismcurios.util;

import java.util.Locale;

public enum CuriosSlots {
    QIO,
    TELEPORTER;

    public String id() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }
}
