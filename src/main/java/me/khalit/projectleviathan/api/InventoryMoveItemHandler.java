package me.khalit.projectleviathan.api;

import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

public interface InventoryMoveItemHandler {

    default void onInventoryMoveItem(InventoryMoveItemEvent event) {
        ItemStack item = event.getItem();
    }
}
