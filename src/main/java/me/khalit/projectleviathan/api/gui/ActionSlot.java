package me.khalit.projectleviathan.api.gui;

import lombok.Getter;
import me.khalit.projectleviathan.api.InventoryClickHandler;

@Getter
public class ActionSlot {

    private InventoryClickHandler clickHandler;

    private final int slot;
    private final ActionInventory inventory;

    public ActionSlot(ActionInventory inventory, int slot) {
        this.slot = slot;
        this.inventory = inventory;
    }

    public ActionSlot init() {
        this.inventory.getActionSlots().put(slot, this);
        return this;
    }

    public void addListener(InventoryClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

}
