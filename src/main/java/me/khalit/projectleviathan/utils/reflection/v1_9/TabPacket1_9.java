package me.khalit.projectleviathan.utils.reflection.v1_9;

import com.mojang.authlib.GameProfile;
import me.khalit.projectleviathan.api.TabPacket;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import me.khalit.projectleviathan.utils.reflection.packet.PacketInjector;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TabPacket1_9 implements TabPacket {

    private static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO = Reflection.getCraftClass("PacketPlayOutPlayerInfo");
    private static final Class<?> PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER = Reflection.getCraftClass("PacketPlayOutPlayerListHeaderFooter");
    private static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO$ENUM_PLAYER_INFO_ACTION = Reflection.getCraftClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
    private static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO$PLAYER_INFO_DATA = Reflection.getCraftClass("PacketPlayOutPlayerInfo$PlayerInfoData");
    private static final Class<?> ICHAT_BASE_COMPONENT = Reflection.getCraftClass("IChatBaseComponent");
    private static final Class<?> ICHAT_BASE_COMPONENT$CHAT_SERIALIZER = Reflection.getCraftClass("IChatBaseComponent$ChatSerializer");
    private static Class<?> WORLD_SETTINGS$ENUM_GAMEMODE = Reflection.getCraftClass("WorldSettings$EnumGamemode");
    private static final Object PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR =
            Reflection.getConstructor(PACKET_PLAY_OUT_PLAYER_INFO);

    public TabPacket1_9() { }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void sendPacket(Player player, GameProfile gp, String slot, String mode) {
        Object cons = Reflection.getConstructor(PACKET_PLAY_OUT_PLAYER_INFO);
        Field a = Reflection.getField(PACKET_PLAY_OUT_PLAYER_INFO, "a");
        Field b = Reflection.getField(PACKET_PLAY_OUT_PLAYER_INFO, "b");

        try {
            if (a != null) {
                a.setAccessible(true);
                if (PACKET_PLAY_OUT_PLAYER_INFO$ENUM_PLAYER_INFO_ACTION != null) {
                    a.set(cons, Enum.valueOf((Class<Enum>) PACKET_PLAY_OUT_PLAYER_INFO$ENUM_PLAYER_INFO_ACTION, mode));
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        List<Object> ppi = new ArrayList<>();
        ppi.add(getPlayerInfo(gp, slot));

        try {
            if (b != null) {
                b.setAccessible(true);
                b.set(cons, ppi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PacketInjector.sendPacket(player, cons);
    }


    public void sendPacketHeaderFooter(Player player, String header, String footer) {
        Object cons = Reflection.getConstructor(PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER);
        Field a = Reflection.getField(PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER, "a");
        Field b = Reflection.getField(PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER, "b");
        assert a != null;
        a.setAccessible(true);
        assert b != null;
        b.setAccessible(true);

        Object headerContent = null;
        Object footerContent = null;
        try {
            headerContent = build("{\"text\": \"" + Util.fixColors(header) + "\"}");
            footerContent = build("{\"text\": \"" + Util.fixColors(footer) + "\"}");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


        try {
            a.set(cons, headerContent);
            b.set(cons, footerContent);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        PacketInjector.sendPacket(player, cons);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object getPlayerInfo(GameProfile gp, String slot) {
        try {
            Constructor<?> cons = null;
            if (PACKET_PLAY_OUT_PLAYER_INFO$PLAYER_INFO_DATA != null) {
                cons = PACKET_PLAY_OUT_PLAYER_INFO$PLAYER_INFO_DATA
                        .getDeclaredConstructor(
                                PACKET_PLAY_OUT_PLAYER_INFO,
                                GameProfile.class,
                                int.class,
                                WORLD_SETTINGS$ENUM_GAMEMODE, ICHAT_BASE_COMPONENT);
            }
            if (cons != null) {
                return cons.newInstance(PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR,
                        gp,
                        35,
                        Enum.valueOf((Class<Enum>) WORLD_SETTINGS$ENUM_GAMEMODE, "SURVIVAL"),
                        build("{\"text\": \"" + Util.fixColors(slot) + "\"}"));
            }
        } catch (InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object build(String content) {
        Method method = Reflection.getTypedMethod(
                ICHAT_BASE_COMPONENT$CHAT_SERIALIZER, "a",
                ICHAT_BASE_COMPONENT, String.class);

        try {
            assert method != null;
            return method.invoke(null, content);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
