package me.khalit.projectleviathan.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface InventoryClickHandler {

    default void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getClickedInventory();
        int slot = event.getSlot();
    }

}
