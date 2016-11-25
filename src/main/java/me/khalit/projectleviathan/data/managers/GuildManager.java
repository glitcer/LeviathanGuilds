package me.khalit.projectleviathan.data.managers;

import lombok.Getter;
import lombok.NonNull;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.Rank;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.utils.Serializer;

import java.util.*;
import java.util.stream.Collectors;

public class GuildManager {

    @Getter
    private static final Map<String, Guild> guilds = new WeakHashMap<>();

    @NonNull
    public static Guild getGuild(String tag) {
        return guilds.values().stream().filter(g -> g.getTag().toUpperCase().equals(tag.toLowerCase())).findFirst().orElse(null);
    }

    @NonNull
    public static boolean existsTag(String tag) {
        return guilds.values().stream().filter(g -> g.getTag().toUpperCase().equals(tag.toLowerCase())).findFirst().orElse(null) != null;
    }

    @NonNull
    public static boolean existsName(String name) {
        return guilds.values().stream().filter(g -> g.getName().toUpperCase().equals(name.toLowerCase())).findFirst().orElse(null) != null;
    }

    @NonNull
    public static List<String> toStrings(List<Guild> guilds) {
        return guilds.stream().map(Guild::getTag).collect(Collectors.toList());
    }

    @NonNull
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
<<<<<<< HEAD
                    guilds.put(result.getString("tag"), guild);
                    guild.getMembers().forEach(member -> member.setGuild(guild));
=======

                    guild.getMembers().forEach(member -> member.setGuild(guild));

                    guilds.add(guild);
>>>>>>> 1152ca226e94eee49085dbd22ad571bded37e1a6
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
