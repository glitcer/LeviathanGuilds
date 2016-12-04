package me.khalit.projectleviathan.utils;

import me.khalit.projectleviathan.api.builders.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Parser {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

    public static List<ItemStack> getParsedList(List<String> syntaxes) {
        return syntaxes.stream().map(Parser::getParsedItem).collect(Collectors.toList());
    }

    public static ItemStack getParsedItem(String syntax) {
        String[] args = syntax.split(" ");
        // syntax = diamond 5 name=test_test lore=test;test enchantments=sharpness:5;infinity:1
        // untested
        ItemStack itemStack = getParsedItem(args[0], Integer.valueOf(args[1]));
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);

        for (String arg : args) {
            String[] metaInfo = arg.split("=");
            String meta = metaInfo[0];
            String value = metaInfo[1];
            switch (meta) {
                case "name":
                    itemBuilder.setName(value);
                case "lore":
                    itemBuilder.setLore(value.replace("_", " ").split(";"));
                case "enchantments":
                    for (String enchantment : value.split(";"))
                        addEnchant(enchantment, itemBuilder);
            }
        }
        return itemBuilder.getItem();
    }

    private static void addEnchant(String syntax, ItemBuilder is) {
        String[] args = syntax.split(":");
        Enchantment ench = Enchantments.enchants.get(args[0]);
        int power = Integer.valueOf(args[1]);
        is.addEnchantment(ench, power);
    }

    @SuppressWarnings("deprecation")
    private static ItemStack getParsedItem(String item, int amount) {
        ItemStack itemR = null;
        try {
            short data;
            String[] split;

            if (!item.contains(":")) {
                split = new String[]{item};
                data = 0;
            } else {
                split = item.split(":");
                data = split[1].length() < 5 && split[1].length() > 0 ? Short.parseShort(split[1]) : 0;
            }

            String namer = split[0];

            if (StringUtils.isNumeric(namer)) {
                int id = namer.length() < 5 && namer.length() > 0 ? Integer.parseInt(namer) : 0;
                itemR = new ItemStack(Material.getMaterial(id), amount, data);
            } else itemR = new ItemStack(Material.matchMaterial(namer.toUpperCase()), amount, data);

        } catch (Exception e) {
            return null;
        }

        return itemR;
    }

    public static String parseLocation(Location location) {
        return location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
    }

    public static String parseTimeDate(long time) {
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String parseTime(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days > 0) {
            sb.append(days);
            sb.append("d ");
        }
        if (hours > 0) {
            sb.append(hours);
            sb.append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes);
            sb.append("m ");
        }
        if (seconds > 0) {
            sb.append(seconds);
            sb.append("s");
        }

        return (sb.toString());
    }

}
