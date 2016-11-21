package me.khalit.projectleviathan.utils.element;

import me.khalit.projectleviathan.configuration.Settings;
import me.khalit.projectleviathan.configuration.TabReader;
import me.khalit.projectleviathan.utils.KeyPair;
import org.bukkit.entity.Player;

public class TabManager {

    public static void updateLight(Player player) {
        TabPacket.sendPacketHeaderFooter(player,
                Settings.getString("tab.header"),
                Settings.getString("tab.footer"));

        for (KeyPair<Integer, Integer> entry : TabReader.getUpdateSlots()) {
            int column = entry.getValueFirst() - 1;
            int row = entry.getValueSecond() - 1;

            KeyPair<Integer, Integer> slot = new KeyPair<>();
            slot.put(column, row);

            String str = TabReader.getSlots().get(slot) == null ? "" : TabReader.getSlots().get(slot);
            TabExecutor.update(player, row, column,
                    TabVariableChanger.replaceVariables(player, str));
        }
        TabExecutor.execute(player);
    }

    public static void updateHeavy(Player player) {
        TabPacket.sendPacketHeaderFooter(player,
                Settings.getString("tab.header"),
                Settings.getString("tab.footer"));

        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 20; row++) {
                String str = "";
                for (KeyPair<Integer, Integer> entry : TabReader.getSlots().keySet()) {
                    if (entry.getValueFirst().equals(column)
                            && entry.getValueSecond().equals(row)) {
                        str = TabReader.getSlots().get(entry);
                    }
                }
                TabExecutor.update(player, row, column,
                        TabVariableChanger.replaceVariables(player, str));
            }
        }
    }


    public static void show(Player player) {
        TabPacket.sendPacketHeaderFooter(player,
                Settings.getString("tab.header"),
                Settings.getString("tab.footer"));

        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 20; row++) {
                String str = "";
                for (KeyPair<Integer, Integer> entry : TabReader.getSlots().keySet()) {
                    if (entry.getValueFirst().equals(column)
                            && entry.getValueSecond().equals(row)) {
                        str = TabReader.getSlots().get(entry);
                    }
                }
                TabExecutor.set(player, row, column,
                        TabVariableChanger.replaceVariables(player, str));
            }
        }

        TabExecutor.execute(player);
    }

}
