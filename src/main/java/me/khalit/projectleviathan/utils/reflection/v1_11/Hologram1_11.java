package me.khalit.projectleviathan.utils.reflection.v1_11;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.Hologram;
import me.khalit.projectleviathan.utils.KeyPair;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import me.khalit.projectleviathan.utils.reflection.packet.PacketInjector;
import net.minecraft.server.v1_11_R1.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_11_R1.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hologram1_11 implements Hologram {

    @Getter
    private boolean showing = false;

    private List<String> hologramLines = new ArrayList<>();
    private List<ArmorStand> hologramStands = new ArrayList<>();
    private Location location;

    public Hologram1_11() {
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
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), this::destroy, ticks);
    }

    @Override
    public List<ArmorStand> display(Location location, String content) {
        try {
            ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(
                    location,
                    EntityType.ARMOR_STAND);
            stand.setCustomName(content);
            stand.setCustomNameVisible(true);
            stand.setVisible(false);
            stand.setRemoveWhenFarAway(false);
            stand.setMarker(true);
            stand.setInvulnerable(true);
            stand.setGravity(false);
            return Collections.singletonList(stand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroy() {
        if (!showing) return;

        hologramStands.forEach(Entity::remove);
        hologramStands.clear();
        showing = false;
    }
}
