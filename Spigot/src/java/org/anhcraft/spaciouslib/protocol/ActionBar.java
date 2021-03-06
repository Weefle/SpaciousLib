package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.*;

/**
 * A class helps you to send action bar message packets
 */
public class ActionBar {
    /**
     * Creates an action bar message packet<br>
     * Supports raw JSON text.<br>
     * @param text the message
     * @return PacketSender object
     */
    public static PacketSender create(String text) {
        return create(text, 20, 60, 20);
    }

    /**
     * Creates an action bar message packet<br>
     * Supports raw JSON text.
     * @param text the message
     * @param fadeIn the fading in time
     * @param stay the staying time
     * @param fadeOut the fading out time
     * @return PacketSender object
     */
    public static PacketSender create(String text, int fadeIn, int stay, int fadeOut) {
        if(CommonUtils.isValidJSON(text)){
            text = Chat.color(text);
        } else {
            text = "{\"text\": \"" + Chat.color(text) + "\"}";
        }
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v.toString() + "." + (v.equals(GameVersion.v1_8_R1) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v.toString() + ".IChatBaseComponent");
            Object chatBaseComponent = ReflectionUtils.getStaticMethod("a", chatSerializerClass,
                    new Group<>(
                            new Class<?>[]{String.class},
                            new String[]{text}
                    ));

            // 1.11 - 1.12
            if(v.equals(GameVersion.v1_11_R1) || v.equals(GameVersion.v1_12_R1)){
                Class<?> packetPlayOutTitleClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutTitle");
                Class<?> enumTitleActionClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutTitle$EnumTitleAction");
                Object enumActionBar = ReflectionUtils.getEnum("ACTIONBAR", enumTitleActionClass);
                return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutTitleClass, new Group<>(
                        new Class<?>[]{enumTitleActionClass, chatBaseComponentClass,
                                int.class, int.class, int.class},
                        new Object[]{enumActionBar, chatBaseComponent, fadeIn, stay, fadeOut}
                )));
            }
            // 1.8 R1 - 1.10
            else{
                Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutChat");
                return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutChatClass, new Group<>(
                        new Class<?>[]{chatBaseComponentClass, byte.class},
                        new Object[]{chatBaseComponent, (byte) 2}
                )));
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
