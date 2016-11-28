package me.khalit.projectleviathan.utils.reflection.v1_11;

import com.mojang.authlib.GameProfile;
import me.khalit.projectleviathan.api.TabPacket;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.reflection.ModernReflection;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import me.khalit.projectleviathan.utils.reflection.packet.PacketInjector;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TabPacket1_11 implements TabPacket {

    private static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO = Reflection.getCraftClass("PacketPlayOutPlayerInfo");
    private static final Class<?> PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER = Reflection.getCraftClass("PacketPlayOutPlayerListHeaderFooter");
    private static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO$ENUM_PLAYER_INFO_ACTION = Reflection.getCraftClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
    private static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO$PLAYER_INFO_DATA = Reflection.getCraftClass("PacketPlayOutPlayerInfo$PlayerInfoData");
    private static final Class<?> ICHAT_BASE_COMPONENT = Reflection.getCraftClass("IChatBaseComponent");
    private static final Class<?> ICHAT_BASE_COMPONENT$CHAT_SERIALIZER = Reflection.getCraftClass("IChatBaseComponent$ChatSerializer");
    private static Class<?> ENUM_GAMEMODE = Reflection.getCraftClass("EnumGamemode");
    private static final Object PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR =
            ModernReflection.getConstructor(PACKET_PLAY_OUT_PLAYER_INFO);
    private static final Field aRaw = Reflection.getField(PACKET_PLAY_OUT_PLAYER_INFO, "a");
    private static final Field bRaw = Reflection.getField(PACKET_PLAY_OUT_PLAYER_INFO, "b");
    private static final MethodHandle aBuild = ModernReflection.getMethod(
            ICHAT_BASE_COMPONENT$CHAT_SERIALIZER, ICHAT_BASE_COMPONENT, "a", false, String.class);

    private MethodHandle a;
    private MethodHandle b;

    public TabPacket1_11() { }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void sendPacket(Player player, GameProfile gp, String slot, String mode) {
        if (a == null) {
            aRaw.setAccessible(true);
            a = ModernReflection.getField(aRaw, false);
        }
        if (b == null) {
            bRaw.setAccessible(true);
            b = ModernReflection.getField(bRaw, false);
        }

        try {
            a.invokeExact(Enum.valueOf((Class<Enum>) PACKET_PLAY_OUT_PLAYER_INFO$ENUM_PLAYER_INFO_ACTION, mode));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<Object> ppi = new ArrayList<>();
        ppi.add(getPlayerInfo(gp, slot));

        try {
            b.invokeExact(ppi);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        PacketInjector.sendPacket(player, PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR);
    }


    public void sendPacketHeaderFooter(Player player, String header, String footer) {
        if (a == null) {
            aRaw.setAccessible(true);
            a = ModernReflection.getField(aRaw, false);
        }
        if (b == null) {
            bRaw.setAccessible(true);
            b = ModernReflection.getField(bRaw, false);
        }

        Object headerContent = null;
        Object footerContent = null;
        try {
            headerContent = build("{\"text\": \"" + Util.fixColors(header) + "\"}");
            footerContent = build("{\"text\": \"" + Util.fixColors(footer) + "\"}");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            a.invokeExact(headerContent);
            b.invokeExact(footerContent);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        PacketInjector.sendPacket(player, PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR);
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
                                ENUM_GAMEMODE, ICHAT_BASE_COMPONENT);
            }
            if (cons != null) {
                return cons.newInstance(PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR,
                        gp,
                        35,
                        Enum.valueOf((Class<Enum>) ENUM_GAMEMODE, "SURVIVAL"),
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
        try {
            return aBuild.invokeExact(content);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


}
