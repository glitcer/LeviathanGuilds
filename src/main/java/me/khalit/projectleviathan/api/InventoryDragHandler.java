package me.khalit.projectleviathan.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface InventoryDragHandler {

    default void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
    }
}
