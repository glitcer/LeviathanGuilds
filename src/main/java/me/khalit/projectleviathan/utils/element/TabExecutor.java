package me.khalit.projectleviathan.utils.element;

import com.mojang.authlib.GameProfile;
import lombok.Data;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
public class TabExecutor {

    private static final GameProfile[][] gameProfiles = new GameProfile[20][4];
    private static String[][] slots = new String[20][4];

    public static void execute(Player player) {
        for (int columns = 0; columns < 4; columns++) {
            for (int rows = 0; rows < 20; rows++) {
                String slot = slots[rows][columns];
                GameProfile gp = gameProfiles[rows][columns];
                TabPacket.sendPacket(player, gp, slot, "ADD_PLAYER");
            }
        }
    }

    public static void load() {
        for (int columns = 0; columns < 4; columns++) {
            for (int rows = 0; rows < 20; rows++) {
                gameProfiles[rows][columns] = new GameProfile(UUID.randomUUID(),
                        "#invalid.ver#" + columns + rows);
            }
        }
    }

    public static void clear() {
        for (int columns = 0; columns < 4; columns++) {
            for (int rows = 0; rows < 20; rows++) {
                slots[rows][columns] = "";
            }
        }
    }

    public static void update(Player player, int row, int column, String content) {
        slots[row][column] = Util.fixColors(content);
        TabPacket.sendPacket(player, gameProfiles[row][column], slots[row][column], "UPDATE_DISPLAY_NAME");
    }

    public static void set(Player player, int row, int column, String content) {
        slots[row][column] = Util.fixColors(content);
    }
}
