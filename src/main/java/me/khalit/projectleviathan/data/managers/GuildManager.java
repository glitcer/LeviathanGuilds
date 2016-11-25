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
            Main.getSqlHandler().query("SELECT * FROM guilds", result -> {
                while (result.next()) {
                    Guild guild = new Guild(result.getString("tag"), result.getString("name"));
                    Rank rank = new Rank(guild);
                    rank.setKills(result.getInt("kills"));
                    rank.setDeaths(result.getInt("deaths"));
                    guild.setAllies(GuildManager.fromStrings(Serializer.deserializeList(result.getString("allies"))));
                    guild.setProtection(result.getLong("attacked"));
                    guild.setRank(rank);
                    guild.setAllyInvites(new ArrayList<>());
                    guild.setBorn(result.getLong("born"));
                    guild.setHome(Serializer.deserializeLocation(result.getString("home")));
                    guild.setMembers(UserManager.fromStrings(Serializer.deserializeList(result.getString("members"))));
                    guild.setOwner(UserManager.getUser(result.getString("owner")));
                    guild.setPvp(result.getBoolean("pvp"));
                    guild.setValidity(result.getLong("validity"));
                    guild.setMoney(result.getInt("money"));
                    guild.setOccupied(result.getBoolean("occupied"));
                    guild.setTreasury(Serializer.deserializeInventory(result.getString("treasury")));
                    RankManager.update(guild);
                    guilds.add(guild);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
