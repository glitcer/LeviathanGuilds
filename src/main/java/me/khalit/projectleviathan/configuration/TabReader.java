package me.khalit.projectleviathan.configuration;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.utils.KeyPair;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabReader {

    @Getter
    private static HashMap<KeyPair<Integer, Integer>, String> slots = new HashMap<>();
    @Getter
    private static List<KeyPair<Integer, Integer>> updateSlots = new ArrayList<>();

    public static void loadConfiguration() {
        FileConfiguration config = Main.getInstance().getConfig();
        for (int row = 0; row < 20; row++) {
            for (int column = 0; column < 4; column++) {
                String path = config.getString("tab." + (column + 1) + "." + (row + 1));
                String str = path == null ? "" : Util.fixColors(path);

                KeyPair<Integer, Integer> slot = new KeyPair<>();
                slot.put(column, row);

                slots.put(slot, str);
            }
        }
        for (String s : config.getStringList("tab.update")) {
            String[] split = s.split(":");
            int column = Integer.valueOf(split[0]);
            int row = Integer.valueOf(split[1]);

            KeyPair<Integer, Integer> slot = new KeyPair<>();
            slot.put(column, row);

            updateSlots.add(slot);
        }
    }

}
