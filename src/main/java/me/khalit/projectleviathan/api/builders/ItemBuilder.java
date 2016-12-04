package me.khalit.projectleviathan.api.builders;

import lombok.Getter;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {

    @Getter
    private final ItemStack item;

    private ItemMeta meta;

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount, int data) {
        this.item = new ItemStack(material, amount, (short)data);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.item = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    private void updateMeta() {
        this.item.setItemMeta(meta);
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(Util.fixColors(name));
        updateMeta();
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        meta.setLore(Util.fixColors(Arrays.asList(lore)));
        updateMeta();
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        updateMeta();
        return this;
    }
}
