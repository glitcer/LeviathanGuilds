package me.khalit.projectleviathan.data.managers;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.data.Rank;
import me.khalit.projectleviathan.data.User;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserManager {

    @Getter
    private static final List<User> users = new ArrayList<>();

    public static List<String> toStrings(List<User> users) {
        return users.stream().map(User::getName).collect(Collectors.toList());
    }

    public static List<User> fromStrings(List<String> strings) {
        return strings.stream().map(str -> getUser(UUID.fromString(str))).collect(Collectors.toList());
    }

    public static void loadUsers() {
        try {
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "SELECT * FROM users");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("name"), UUID.fromString(rs.getString("uuid")));
                user.setLocale(Locale.fileByLocale(rs.getString("locale")));
                user.setHonor(rs.getInt("honor"));
                Rank rank = new Rank(user);
                rank.setPoints(rs.getInt("points"));
                rank.setKills(rs.getInt("kills"));
                rank.setDeaths(rs.getInt("deaths"));
                user.setRank(rank);
                UserManager.getUsers().add(user);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoaded(Player player) {
        for (User user : users) {
            if (user.getUniqueId().equals(player.getUniqueId())) return true;
        }
        return false;
    }

    public static User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) return user;
        }
        return null;
    }

    public static User getUser(Player player) {
        for (User user : users) {
            if (user.getUniqueId().equals(player.getUniqueId())) return user;
        }
        return null;
    }

    public static User getUser(UUID uuid) {
        for (User user : users) {
            if (user.getUniqueId().toString()
                    .equals(uuid.toString())) return user;
        }
        return null;
    }

    public static User getFreshUser(Player player) {
        return new User(player.getName(), player.getUniqueId());
    }

}
