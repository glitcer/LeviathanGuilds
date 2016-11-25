package me.khalit.projectleviathan.api.gui;

import me.khalit.projectleviathan.api.InventoryClickHandler;
import me.khalit.projectleviathan.api.InventoryOpenHandler;
import me.khalit.projectleviathan.api.builders.ItemBuilder;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class GUICreator {

    public static void initialize() {
        ActionInventory inventory = new ActionInventory(GUISize.SIX_ROWS, "&6Test Inventory").init();
        inventory.setItem(5, new ItemBuilder(Material.APPLE).setName("&8Custom Apple"));
        inventory.setItem(20, new ItemBuilder(Material.BEDROCK).setName("&8Bedrock").setLore("&7Dark bedrock", "&8from the dark sides"));
        inventory.getActionSlot(20).addListener(event -> Bukkit.broadcastMessage("&8&lIt's a trap!"));
        inventory.addOpenListener(event -> Bukkit.broadcastMessage("&8You've opened a trap!"));
        inventory.register();
    }

    public static void initializeUnique(Player player) {
        ActionInventory inventory = new ActionInventory(GUISize.FIVE_ROWS, "&6Test unique inventory").unique(player);
        inventory.setItem(10, new ItemBuilder(Material.GOLDEN_APPLE));
        inventory.register();
        inventory.show(player);
    }

    public static void showExistingUnique(Player player) {
        ActionInventory inventory = GUIHandler.getUniqueActionInventory(player,
                Util.fixColors("&6Test unique inventory"));
        inventory.show(player);
    }

    public static void showExisting(Player player) {
        ActionInventory inventory = GUIHandler.getActionInventory(Util.fixColors("&6Test inventory"));
        inventory.show(player);
    }
}
