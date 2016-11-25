package me.khalit.projectleviathan.api.gui;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.InventoryCloseHandler;
import me.khalit.projectleviathan.api.InventoryOpenHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActionInventory implements Listener {

    private final Inventory inventory;
    private List<ActionSlot> actionSlots = new ArrayList<>();

    private InventoryOpenHandler openHandler;
    private InventoryCloseHandler closeHandler;

    public ActionInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public ActionInventory init() {
        GUIHandler.getInventories().add(this);
        return this;
    }

    public void addListener(InventoryOpenHandler openHandler) {
        this.openHandler = openHandler;
    }

    public void addListener(InventoryCloseHandler closeHandler) {
        this.closeHandler = closeHandler;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        openHandler.onInventoryOpen(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        closeHandler.onInventoryClose(event);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        int slotNumber = event.getSlot();
        actionSlots.stream().filter(slot -> event.getSlot() == slot.getSlot())
                .forEach(slot -> slot.getClickHandler().onInventoryClick(event));
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        actionSlots.stream().filter(slot -> event.get == slot.getSlot())
                .forEach(slot -> slot.getDragHandler().onInventoryDrag(event));
    }

}
