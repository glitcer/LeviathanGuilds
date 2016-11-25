package me.khalit.projectleviathan.api.gui;

import me.khalit.projectleviathan.api.InventoryClickHandler;
import me.khalit.projectleviathan.api.InventoryOpenHandler;
import me.khalit.projectleviathan.api.builders.ItemBuilder;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class GUICreator {

    public static void initialize() {
        Inventory inv = Bukkit.createInventory(null, 54, Util.fixColors("&6Test inventory"));
        ActionInventory inventory = new ActionInventory(inv).init();

        inv.setItem(5, new ItemBuilder(Material.APPLE).setName("&8Custom Apple").getItem());

        ActionSlot slot = new ActionSlot(inventory, 5).init();
        slot.addListener(new InventoryClickHandler() {
            @Override
            public void onInventoryClick(InventoryClickEvent event) {
            }
        });

        inventory.addListener(new InventoryOpenHandler() {
            @Override
            public void onInventoryOpen(InventoryOpenEvent event) {
            }
        });
    }

    public static void initializeUnique() {

    }
}
