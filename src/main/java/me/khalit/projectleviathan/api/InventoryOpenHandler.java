package me.khalit.projectleviathan.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;

public interface InventoryOpenHandler {

    void onInventoryOpen(InventoryOpenEvent event);
}
