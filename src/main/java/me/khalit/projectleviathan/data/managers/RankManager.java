package me.khalit.projectleviathan.data.managers;

import lombok.Getter;
import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.Rank;
import me.khalit.projectleviathan.data.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankManager {

    @Getter
    private static final List<Rank> users = new ArrayList<>();
    @Getter
    private static final List<Rank> guilds = new ArrayList<>();

    public static int getPosition(User user) {
        return users.indexOf(user.getRank()) + 1;
    }

    public static int getPosition(Guild guild) {
        return guilds.indexOf(guild.getRank()) + 1;
    }

    public static User getUser(int i) {
        if (i - 1 < users.size()) {
            return (users.get(i - 1).getUser());
        }
        return null;
    }

    public static Guild getGuild(int i) {
        if (i - 1 < guilds.size()) {
            return (guilds.get(i - 1).getGuild());
        }
        return null;
    }

    public static void update(User user) {
        if (!users.contains(user.getRank())) {
            users.add(user.getRank());
        }
        Collections.sort(users);
        if (user.hasGuild()) {
            update(user.getGuild());
        }
    }

    public static void update(Guild guild) {
        if (!guilds.contains(guild.getRank())) {
            guilds.add(guild.getRank());
        }
        else {
            Collections.sort(guilds);
        }
    }

    public void remove(User user) {
        users.remove(user.getRank());
        Collections.sort(users);
    }

    public void remove(Guild guild) {
        guilds.remove(guild.getRank());
        Collections.sort(guilds);
    }

}
