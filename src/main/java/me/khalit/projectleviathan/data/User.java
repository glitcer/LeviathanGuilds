package me.khalit.projectleviathan.data;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.Data;
import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.configuration.LocaleFile;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;

@lombok.Data
public class User implements Data {

    private final UUID uniqueId;
    private final String name;
    private Guild guild;
    private Rank rank;
    private int honor;
    private LocaleFile locale;

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

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public boolean hasGuild() {
        return guild != null;
    }

    public int getPing() {
        try {
            Class<?> craftPlayerClass = Reflection.getBukkitClass("entity.CraftPlayer");
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
        return UserManager.getUsers().contains(this);
    }

    @Override
    public void save() {
        try {
            Main.getSqlHandler().execute("UPDATE users SET `locale`=" + Locale.localeByConfigurableFile(locale) + ", `honor`=" + honor + ", `points`=" + rank.getPoints() + ", `kills`=" + rank.getKills() + ", `deaths`=" + rank.getDeaths() + " WHERE `uuid`=" + uniqueId.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert() {
        try {
            Main.getSqlHandler().execute("INSERT INTO users VALUES (" + uniqueId.toString() + "), " + name + ", " + Locale.localeByConfigurableFile(locale) + ", " + honor + ", " + rank.getPoints() + ", " + rank.getKills() + ", " + rank.getDeaths() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
