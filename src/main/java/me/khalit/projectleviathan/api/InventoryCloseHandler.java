package me.khalit.projectleviathan.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

public interface InventoryCloseHandler {

    void onInventoryClose(InventoryCloseEvent event);
}
