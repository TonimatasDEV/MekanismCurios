package dev.tonimatas.mekanismcurios.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY = "key.categories.mekanismcurios";
    public static final String KEY_PORTABLE_QIO = "key.mekanismcurios.protableqio";
    public static final String KEY_PORTABLE_TELEPORTER = "key.mekanismcurios.portableteleporter";
    public static final String KEY_QUICK_TELEPORT = "key.mekanismcurios.quickteleport";
    
    public static final Lazy<KeyMapping> PORTABLE_QIO_MAPPING = Lazy.of(() -> new KeyMapping(KEY_PORTABLE_QIO, 
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, KEY_CATEGORY));
    public static final Lazy<KeyMapping> PORTABLE_TELEPORTER_MAPPING = Lazy.of(() -> new KeyMapping(KEY_PORTABLE_TELEPORTER,
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, KEY_CATEGORY));
    public static final Lazy<KeyMapping> QUICK_TELEPORT_MAPPING = Lazy.of(() -> new KeyMapping(KEY_QUICK_TELEPORT,
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, KEY_CATEGORY));
}
