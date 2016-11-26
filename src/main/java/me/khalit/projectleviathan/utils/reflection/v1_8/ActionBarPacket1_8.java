package me.khalit.projectleviathan.utils.reflection.v1_8;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.ActionBar;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import me.khalit.projectleviathan.utils.reflection.packet.PacketInjector;
import me.khalit.projectleviathan.utils.reflection.v1_11.ActionBarPacket1_11;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionBarPacket1_8 implements ActionBar {

    private static final Class<?> ICHAT_BASE_COMPONENT =
            Reflection.getCraftClass("IChatBaseComponent");
    private static final Class<?> ICHAT_BASE_COMPONENT$CHAT_SERIALIZER =
            Reflection.getCraftClass("IChatBaseComponent$ChatSerializer");
    private static final Class<?> PACKET_PLAY_OUT_CHAT =
            Reflection.getCraftClass("PacketPlayOutChat");

    public ActionBarPacket1_8() {
    }

    @Override
    public void send(Player player, String content) {
        PacketInjector.sendPacket(player, getPacket(content));
    }

    @Override
    public void send(Player player, String content, int ticks) {
        final int[] timeLeft = {ticks};
        new BukkitRunnable() {
            @Override
            public void run() {
                timeLeft[0] -= 20L;
                send(player, content);

                if (timeLeft[0] <= 0) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
    }

    @Override
    public void send(String content) {
        PacketInjector.sendPacket(getPacket(content));
    }

    @Override
    public void send(String content, int ticks) {
        final int[] timeLeft = {ticks};
        new BukkitRunnable() {
            @Override
            public void run() {
                timeLeft[0] -= 20L;
                send(content);

                if (timeLeft[0] <= 0) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
    }

    @Override
    public Object getPacket(String content) {
        try {
            Object baseComponent = ICHAT_BASE_COMPONENT$CHAT_SERIALIZER
                    .getMethod("a", String.class)
                    .invoke(null, "{'text': '" + Util.fixColors(content) + "'}");
            return PACKET_PLAY_OUT_CHAT
                    .getConstructor(ICHAT_BASE_COMPONENT, Byte.TYPE)
                    .newInstance(baseComponent, (byte)2);
        } catch (NoSuchMethodException
                | InstantiationException
                | InvocationTargetException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
