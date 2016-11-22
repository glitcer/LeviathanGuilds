package me.khalit.projectleviathan.data.managers;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.Rank;
import me.khalit.projectleviathan.utils.Serializer;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuildManager {

    @Getter
    private static final List<Guild> guilds = new ArrayList<>();

    public static Guild getGuild(String tag) {
        for (Guild guild : guilds) {
            if (guild.getTag().toUpperCase().equals(tag.toUpperCase())) return guild;
        }
        return null;
    }

    public static boolean existsTag(String tag) {
        for (Guild guild : guilds) {
            if (guild.getTag().toUpperCase().equals(tag.toUpperCase())) return true;
        }
        return false;
    }

    public static boolean existsName(String name) {
        for (Guild guild : guilds) {
            if (guild.getName().toUpperCase().equals(name.toUpperCase())) return true;
        }
        return false;
    }

    public static List<String> toStrings(List<Guild> guilds) {
        return guilds.stream().map(Guild::getTag).collect(Collectors.toList());
    }

    public static List<Guild> fromStrings(List<String> strings) {
        return strings.stream().map(GuildManager::getGuild).collect(Collectors.toList());
    }

    public static void loadGuilds() {
        try {
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "SELECT * FROM guilds");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Guild guild = new Guild(rs.getString("tag"), rs.getString("name"));
                Rank rank = new Rank(guild);
                rank.setKills(rs.getInt("kills"));
                rank.setDeaths(rs.getInt("deaths"));
                guild.setAllies(GuildManager.fromStrings(Serializer.deserializeList(rs.getString("allies"))));
                guild.setProtection(rs.getLong("attacked"));
                guild.setRank(rank);
                guild.setAllyInvites(new ArrayList<>());
                guild.setBorn(rs.getLong("born"));
                guild.setHome(Serializer.deserializeLocation(rs.getString("home")));
                guild.setMembers(UserManager.fromStrings(Serializer.deserializeList(rs.getString("members"))));
                guild.setOwner(UserManager.getUser(rs.getString("owner")));
                guild.setPvp(rs.getBoolean("pvp"));
                guild.setValidity(rs.getLong("validity"));
                guild.setMoney(rs.getInt("money"));
                guild.setOccupied(rs.getBoolean("occupied"));
                guild.setTreasury(Serializer.deserializeInventory(rs.getString("treasury")));
                RankManager.update(guild);
                guilds.add(guild);
            }
            rs.close();
            stmt.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
