package me.khalit.projectleviathan.utils.reflection.v1_11;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.Hologram;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.reflection.ModernReflection;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import me.khalit.projectleviathan.utils.reflection.packet.PacketInjector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.lang.invoke.MethodHandle;
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
    private static final MethodHandle setLocationMethod = ModernReflection.getMethod(entityClassPath, "setLocation", int.class, int.class, int.class, int.class, int.class);
    private static final MethodHandle setCustomNameVisibleMethod = ModernReflection.getMethod(entityClassPath, "setCustomNameVisible", boolean.class);
    private static final MethodHandle setCustomNameMethod = ModernReflection.getMethod(entityClassPath, "setCustomName", String.class);
    private static final MethodHandle setGravityMethod = ModernReflection.getMethod(entityClassPath, "setGravity", boolean.class);
    private static final MethodHandle setRemoveWhenFarawayMethod = ModernReflection.getMethod(entityClassPath, "setRemoveWhenFarAway", boolean.class);
    private static final MethodHandle setInvulnerableMethod = ModernReflection.getMethod(entityClassPath, "setInvulnerable", boolean.class);
    private static final MethodHandle setMarkerMethod = ModernReflection.getMethod(entityClassPath, "setMarker", boolean.class);
    private static final MethodHandle setVisibleMethod = ModernReflection.getMethod(entityClassPath, "setVisible", boolean.class);
    private static final MethodHandle getIdMethod = ModernReflection.getMethod(entityClassPath, int.class, "getId", false);
    private static final MethodHandle destroyConstructor = ModernReflection.getConstructor(packetPlayOutEntityDestroyClass, int[].class);

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

            // TODO: 04.12.2016
            setLocationMethod.invokeExact(location.getBlockX(),
                    location.getBlockY() + 0.23,
                    location.getBlockZ(),
                    0, 0);
            setCustomNameMethod.invokeExact(content);
            setCustomNameVisibleMethod.invokeExact(true);
            setVisibleMethod.invokeExact(false);
            setRemoveWhenFarawayMethod.invokeExact(false);
            setMarkerMethod.invokeExact(true);
            setInvulnerableMethod.invokeExact(true);
            setGravityMethod.invokeExact(false);

            Object packet = packetPlayOutSpawn.newInstance(armorStand, 30);
            PacketInjector.sendPacket(packet);

            int id = (int) getIdMethod.invokeExact();
            return Collections.singletonList(id);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroy() {
        if (!showing) return;

        try {
            Object packet = destroyConstructor.invoke(hologramStands.toArray());
            PacketInjector.sendPacket(packet);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        hologramStands.clear();
        showing = false;
    }
}
