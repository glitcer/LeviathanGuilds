package me.khalit.projectleviathan.data;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.Data;
import me.khalit.projectleviathan.configuration.ConcurrentConfigurableFile;
import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@lombok.Data
public class User implements Data {

    private final UUID uniqueId;
    private final String name;
    private ConcurrentConfigurableFile locale;

    public User(String name, UUID uuid) {
        this.uniqueId = uuid;
        this.name = name;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.getUniqueId());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.getUniqueId());
    }

    public int getPing() {
        try {
            Class<?> craftPlayerClass = Reflection.getOBCClass("entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(getPlayer());
            Object handle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
            Field ping = handle.getClass().getField("ping");
            return (int) ping.get(handle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean hasPlayedBefore() {
        try {
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "SELECT uuid FROM users WHERE uuid=?");
            stmt.setString(1, uniqueId.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void save() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            PreparedStatement statement;
            try {
                statement = Main.getSqlHandler().getConnection().prepareStatement(
                        "UPDATE users SET `locale`=? WHERE uuid=?");
                statement.setString(1, Locale.localeByConfigurableFile(locale));
                statement.setString(2, uniqueId.toString());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void insert() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            PreparedStatement statement;
            try {
                statement = Main.getSqlHandler().getConnection().prepareStatement(
                        "INSERT INTO users VALUES (?, ?, ?)");
                statement.setString(1, uniqueId.toString());
                statement.setString(2, name);
                statement.setString(3, Locale.localeByConfigurableFile(locale));
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
