package me.khalit.projectleviathan.utils;

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
        ItemStack itemstack = getParsedItem(args[0], Integer.valueOf(args[1]));
        assert itemstack != null;
        ItemMeta itemmeta = itemstack.getItemMeta();
        if ((args.length >= 3) && (!args[2].equalsIgnoreCase("*"))) {
            itemmeta.setDisplayName(Util.fixColors(args[2].replace("_", " ")));
        }
        if ((args.length >= 4) && (!args[3].equalsIgnoreCase("*"))) {
            String[] loreLines = args[3].split(";");
            List<String> lore = new ArrayList<>();
            for (String s : loreLines) {
                lore.add(s.replace("_", " "));
            }
            itemmeta.setLore(Util.fixColors(lore));
        }
        itemstack.setItemMeta(itemmeta);
        if ((args.length >= 5) && (!args[4].equalsIgnoreCase("*"))) {
            String[] enchantments = args[4].split(";");
            for (String s : enchantments) {
                addEnchant(s, itemstack);
            }
        }
        return itemstack;
    }

    private static void addEnchant(String syntax, ItemStack is) {
        String[] args = syntax.split(":");
        Enchantment ench = Enchantments.enchants.get(args[0]);
        int power = Integer.valueOf(args[1]);
        is.addUnsafeEnchantment(ench, power);
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
