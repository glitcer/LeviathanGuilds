package me.khalit.projectleviathan.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface InventoryDragHandler {

    void onInventoryDrag(InventoryDragEvent event);
}
