package me.khalit.projectleviathan.utils.reflection.v1_11;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.Hologram;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import me.khalit.projectleviathan.utils.reflection.packet.PacketInjector;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HologramPacket1_11 implements Hologram {

    private static final Class<?> entityClass = Reflection.getCraftClass("Entity");
    private static final Class<?> packetPlayOutSpawnEntityClass = Reflection.getCraftClass("PacketPlayOutSpawnEntity");
    private static final Class<?> packetPlayOutEntityDestroyClass = Reflection.getCraftClass("PacketPlayOutEntityDestroy");
    private static final Class<?> worldClass = Reflection.getCraftClass("World");
    private static final Class<?> entityClassPath = Reflection.getCraftClass("EntityArmorStand");
    private static final Method setLocationMethod = Reflection.getMethod(entityClassPath, "setLocation");
    private static final Method setCustomNameVisibleMethod = Reflection.getMethod(entityClassPath, "setCustomNameVisible");
    private static final Method setCustomNameMethod = Reflection.getMethod(entityClassPath, "setCustomName");
    private static final Method setGravityMethod = Reflection.getMethod(entityClassPath, "setGravity");
    private static final Method setRemoveWhenFarawayMethod = Reflection.getMethod(entityClassPath, "setRemoveWhenFarAway");
    private static final Method setInvulnerableMethod = Reflection.getMethod(entityClassPath, "setInvulnerable");
    private static final Method setMarkerMethod = Reflection.getMethod(entityClassPath, "setMarker");
    private static final Method setVisibleMethod = Reflection.getMethod(entityClassPath, "setVisible");
    private static final Method getIdMethod = Reflection.getMethod(entityClassPath, "getId");

    @Getter
    private boolean showing = false;

    private List<String> hologramLines = new ArrayList<>();
    private List<Integer> hologramStands = new ArrayList<>();
    private Location location;

    public HologramPacket1_11() {
    }

    @Override
    public Hologram set(String... lines) {
        hologramLines.addAll(Arrays.asList(lines));
        return this;
    }

    @Override
    public Hologram change(String... lines) {
        destroy();
        hologramLines = Arrays.asList(lines);
        show(location);
        return this;
    }

    @Override
    public void show(Location location) {
        Location hologramLocation = location.clone().add(0, (hologramLines.size() / 2) * 0.24, 0);
        for (String line : hologramLines) {
            hologramStands.addAll(display(hologramLocation.clone(), Util.fixColors(line)));
            hologramLocation.subtract(0, 0.24, 0);
        }
        showing = true;
        this.location = hologramLocation;
    }

    @Override
    public void show(Location location, long ticks) {
        show(location);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), this::destroy, ticks);
    }

    @Override
    public List<Integer> display(Location location, String content) {
        try {
            Object world = Reflection.getHandle(location.getWorld());
            Object armorStand = entityClassPath.getConstructor(worldClass).newInstance(world);
            Constructor packetPlayOutSpawn = packetPlayOutSpawnEntityClass.getConstructor(entityClass, int.class);

            // under construction ;/
            setLocationMethod.invoke(armorStand,
                    location.getBlockX(),
                    location.getBlockY() + 0.23,
                    location.getBlockZ(),
                    0, 0);
            setCustomNameMethod.invoke(armorStand, content);
            setCustomNameVisibleMethod.invoke(armorStand, true);
            setVisibleMethod.invoke(armorStand, false);
            setRemoveWhenFarawayMethod.invoke(armorStand, false);
            setMarkerMethod.invoke(armorStand, true);
            setInvulnerableMethod.invoke(armorStand, true);
            setGravityMethod.invoke(armorStand, false);

            Object packet = packetPlayOutSpawn.newInstance(armorStand, 30);
            PacketInjector.sendPacket(packet);

            int id = (int) getIdMethod.invoke(armorStand);
            return Collections.singletonList(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroy() {
        if (!showing) return;

        try {
            Object packet = packetPlayOutEntityDestroyClass.getConstructor(int[].class)
                    .newInstance(hologramStands.toArray());
            PacketInjector.sendPacket(packet);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        hologramStands.clear();
        showing = false;
    }
}
