package me.khalit.projectleviathan.api.gui;

import lombok.Getter;
import me.khalit.projectleviathan.api.InventoryCloseHandler;
import me.khalit.projectleviathan.api.InventoryOpenHandler;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActionInventory {

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

}
