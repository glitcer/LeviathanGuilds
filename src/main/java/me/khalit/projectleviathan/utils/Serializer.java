package me.khalit.projectleviathan.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Serializer {

    private static final StringBuilder strb = new StringBuilder();

    public static String serializeList(List<String> stringList) {
        List<String> list = new ArrayList<>();
        String finalString = "";
        int index = 0;
        for (String str : stringList) {
            finalString += str + (index < stringList.size() ? ";" : "");
            index++;
        }
        return finalString;
    }

    public static List<String> deserializeList(String stringList) {
        String[] split = stringList.split(";");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, split);
        return list;
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + " "
                + location.getBlockX() + " "
                + location.getBlockY() + " "
                + location.getBlockZ() + " "
                + Util.round(location.getYaw(), 2) + " "
                + Util.round(location.getPitch(), 2) + " ";
    }

    public static Location deserializeLocation(String parsedLoc) {
        String[] split = parsedLoc.split(" ");
        World world = Bukkit.getServer().getWorld(split[0]);
        if (world == null) {
            return null;
        }
        return new Location(world,
                Double.valueOf(split[1]), Double.valueOf(split[2]),
                Double.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5]));
    }

    public static String serializeInventory(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save itemstack array", e);
        }
    }

    public static ItemStack[] deserializeInventory(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to read class type", e);
        }
    }

}
