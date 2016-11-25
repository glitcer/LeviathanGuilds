package me.khalit.projectleviathan.api.gui;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.InventoryCloseHandler;
import me.khalit.projectleviathan.api.InventoryOpenHandler;
import me.khalit.projectleviathan.api.builders.ItemBuilder;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ActionInventory implements Listener {

    private final Inventory inventory;
    private Map<Integer, ActionSlot> actionSlots = new HashMap<>();

    private InventoryOpenHandler openHandler;
    private InventoryCloseHandler closeHandler;

    public ActionInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public ActionInventory(GUISize size, String title) {
        this.inventory = Bukkit.createInventory(null, size.getSize(), Util.fixColors(title));
    }

    public ActionInventory init() {
        GUIHandler.inventories.put(
                inventory.getTitle(), this);
        return this;
    }

    public ActionInventory unique(Player player) {
        if (!GUIHandler.uniqueInventories.containsKey(player.getUniqueId())) {
            GUIHandler.uniqueInventories.put(player.getUniqueId(), new ArrayList<>());
        }
        GUIHandler.uniqueInventories.get(player.getUniqueId()).add(this);
        return this;
    }

    public ItemBuilder setItem(int slot, ItemBuilder itemBuilder) {
        inventory.setItem(slot, itemBuilder.getItem());
        return itemBuilder;
    }

    public ActionSlot addActionSlot(int slot) {
        return new ActionSlot(this, slot).init();
    }

    public ActionSlot getActionSlot(int slot) {
        return actionSlots.containsKey(slot) ? actionSlots.get(slot) : addActionSlot(slot);
    }

    public void addOpenListener(InventoryOpenHandler openHandler) {
        this.openHandler = openHandler;
    }

    public void addCloseListener(InventoryCloseHandler closeHandler) {
        this.closeHandler = closeHandler;
    }

    public void show(Player player) {
        player.openInventory(inventory);
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
        InventoryView view = event.getView();
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(view.getTopInventory())) return;

        int slotNumber = event.getSlot();
        if (actionSlots.containsKey(slotNumber)) {
            ActionSlot actionSlot = actionSlots.get(slotNumber);
            actionSlot.getClickHandler().onInventoryClick(event);
        }
    }


}
