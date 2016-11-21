package me.khalit.projectleviathan.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;

public interface InventoryOpenHandler {

    default void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        InventoryView view = event.getView();
    }
}
