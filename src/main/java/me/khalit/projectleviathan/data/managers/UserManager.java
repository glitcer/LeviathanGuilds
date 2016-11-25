package me.khalit.projectleviathan.data.managers;

import lombok.Getter;
import lombok.NonNull;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.data.Rank;
import me.khalit.projectleviathan.data.User;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class UserManager {

    @Getter
    private static final Map<UUID, User> users = new WeakHashMap<>();

    public static List<String> toStrings(List<User> users) {
        return users.stream().map(User::getName).collect(Collectors.toList());
    }

    public static List<User> fromStrings(List<String> strings) {
        return strings.stream().map(str -> getUser(UUID.fromString(str))).collect(Collectors.toList());
    }

    public static void loadUsers() {
        try {
            Main.getSqlHandler().query("SELECT * FROM users", result -> {
                while (result.next()) {
                    User user = new User(result.getString("name"), UUID.fromString(result.getString("uuid")));
                    user.setLocale(Locale.fileByLocale(result.getString("locale")));
                    user.setHonor(result.getInt("honor"));
                    Rank rank = new Rank(user);
                    rank.setPoints(result.getInt("points"));
                    rank.setKills(result.getInt("kills"));
                    rank.setDeaths(result.getInt("deaths"));
                    user.setRank(rank);
                    UserManager.getUsers().put(UUID.fromString(result.getString("uuid")), user);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public static boolean isLoaded(Player player) {
        return users.get(player.getUniqueId()) != null;
    }

    @NonNull
    public static User getUser(String name) {
        return users.values().stream().filter(u -> u.getName().equals(name)).findFirst().orElse(null);
    }

    @NonNull
    public static User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    @NonNull
    public static User getUser(UUID uuid) {
        return users.get(uuid);
    }

    @NonNull
    public static User getFreshUser(Player player) {
        return new User(player.getName(), player.getUniqueId());
    }

}
