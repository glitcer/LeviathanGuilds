package me.khalit.projectleviathan.utils.reflection.v1_9;

import lombok.Data;
import me.khalit.projectleviathan.api.Title;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import me.khalit.projectleviathan.utils.reflection.packet.PacketInjector;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

@Data
public class Title1_9 implements Title {

    private static final Class<?> PACKET_PLAY_OUT_TITLE = Reflection.getCraftClass("PacketPlayOutTitle");
    private static final Class<?> PACKET_PLAY_OUT_TITLE$ENUM_TITLE_ACTION = Reflection.getCraftClass("PacketPlayOutTitle$EnumTitleAction");
    private static final Class<?> ICHAT_BASE_COMPONENT = Reflection.getCraftClass("IChatBaseComponent");
    private static final Class<?> CHAT_COMPONENT_TEXT = Reflection.getCraftClass("ChatComponentText");

    private String title;
    private String subtitle;
    private int fadeInTime;
    private int stayTime;
    private int fadeOutTime;

    public Title1_9() {
    }

    @Override
    public Title setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public Title setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    @Override
    public Title setFadeIn(int ticks) {
        this.fadeInTime = ticks;
        return this;
    }

    @Override
    public Title setStay(int ticks) {
        this.stayTime = ticks;
        return this;
    }

    @Override
    public Title setFadeOut(int ticks) {
        this.fadeOutTime = ticks;
        return this;
    }

    @Override
    public void show(Player player) {
        System.out.println("execute but null");
        if (title == null) return;
        System.out.println("not null");

        try {
            Object[] actions = PACKET_PLAY_OUT_TITLE$ENUM_TITLE_ACTION.getEnumConstants();
            Object packet = PACKET_PLAY_OUT_TITLE
                    .getConstructor(PACKET_PLAY_OUT_TITLE$ENUM_TITLE_ACTION, ICHAT_BASE_COMPONENT, Integer.TYPE, Integer.TYPE, Integer.TYPE)
                    .newInstance(actions[2], null, fadeInTime, stayTime, fadeOutTime);

            PacketInjector.sendPacket(player, packet);

            Object serializer = CHAT_COMPONENT_TEXT.getConstructor(
                    String.class).newInstance(Util.fixColors(title));
            packet = PACKET_PLAY_OUT_TITLE.getConstructor(PACKET_PLAY_OUT_TITLE$ENUM_TITLE_ACTION,
                    ICHAT_BASE_COMPONENT).newInstance(actions[0], serializer);
            PacketInjector.sendPacket(player, packet);
            if (subtitle != null) {
                serializer = CHAT_COMPONENT_TEXT.getConstructor(
                        String.class).newInstance(Util.fixColors(subtitle));
                packet = PACKET_PLAY_OUT_TITLE.getConstructor(PACKET_PLAY_OUT_TITLE$ENUM_TITLE_ACTION,
                        ICHAT_BASE_COMPONENT).newInstance(actions[1],
                        serializer);
                PacketInjector.sendPacket(player, packet);
            }
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
