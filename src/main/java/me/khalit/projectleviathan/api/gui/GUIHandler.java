package me.khalit.projectleviathan.api.gui;

import lombok.Getter;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class GUIHandler {

    public static final Map<String, ActionInventory> inventories = new HashMap<>();
    public static final Map<UUID, List<ActionInventory>> uniqueInventories = new HashMap<>();

    public static ActionInventory getUniqueActionInventory(Player player, String title) {
        return uniqueInventories.containsKey(player.getUniqueId()) ?
                uniqueInventories.get(player.getUniqueId())
                        .stream().filter(inventory -> inventory.getInventory().getTitle().startsWith(title))
                        .findFirst()
                        .orElse(null)
                : null;
    }

    public static ActionInventory getActionInventory(String title) {
        return inventories.containsKey(title) ? inventories.get(title) : null;
    }
}
