package me.khalit.projectleviathan.api;

import me.khalit.projectleviathan.utils.KeyPair;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.List;

public interface Hologram {

    Hologram set(String... lines);
    Hologram change(String... lines);
    void show(Location location);
    void show(Location location, long ticks);
    List<Integer> display(Location location, String content);
    void destroy();
}
