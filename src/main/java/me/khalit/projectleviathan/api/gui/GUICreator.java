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

    /**
     * This GUI system actually sucks, but i dont have much time to do it better ;/
     */

    public static void initialize() {
        // example for static inventories
        Inventory inv = Bukkit.createInventory(null, 54, Util.fixColors("&6Test inventory"));
        ActionInventory inventory = new ActionInventory(inv).init();

        inv.setItem(5, new ItemBuilder(Material.APPLE).setName("&8Custom Apple").getItem());

        ActionSlot slot = new ActionSlot(inventory, 5).init();
        slot.addListener(new InventoryClickHandler() {
            @Override
            public void onInventoryClick(InventoryClickEvent event) {
                // something
                // in global InventoryClickListener will be a loop which is looping all inventories and checking ;p
            }
        });

        inventory.addListener(new InventoryOpenHandler() {
            @Override
            public void onInventoryOpen(InventoryOpenEvent event) {
                // something too
            }
        });

        // and thats it, you can show it by
        // player.openInventory(inventory.getInventory()); or player.openInventory(inv);
    }

    public static void initializeUnique() {
        // unique inventories (like stats?)
        // i will probably do similar system to above, but with hashmap with uuid and actioninventory ;p
        // and taking a instance from a hashmap every time (creating new inventory each time is easier, but... it hurts optimization ;p)
        // cuming soon kappa
    }
}
