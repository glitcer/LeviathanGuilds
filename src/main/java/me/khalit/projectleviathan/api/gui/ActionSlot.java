package me.khalit.projectleviathan.api.gui;

import lombok.Data;
import lombok.Getter;
import me.khalit.projectleviathan.api.InventoryClickHandler;
import me.khalit.projectleviathan.api.InventoryDragHandler;
import me.khalit.projectleviathan.api.InventoryMoveItemHandler;

@Getter
public class ActionSlot {

    private InventoryClickHandler clickHandler;
    private InventoryDragHandler dragHandler;
    private InventoryMoveItemHandler moveItemHandler;

    private final int slot;
    private final ActionInventory inventory;

    public ActionSlot(ActionInventory inventory, int slot) {
        this.slot = slot;
        this.inventory = inventory;
    }

    public ActionSlot init() {
        this.inventory.getActionSlots().add(this);
        return this;
    }

    public void addListener(InventoryClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void addListener(InventoryDragHandler dragHandler) {
        this.dragHandler = dragHandler;
    }

    public void addListener(InventoryMoveItemHandler moveItemHandler) {
        this.moveItemHandler = moveItemHandler;
    }

}
