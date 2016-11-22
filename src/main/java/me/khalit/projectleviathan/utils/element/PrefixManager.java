package me.khalit.projectleviathan.utils.element;

import me.khalit.projectleviathan.configuration.Settings;
import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.data.managers.GuildManager;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PrefixManager {

    private static String parse(String color, Guild g) {
        return Util.fixColors(color.replace("%tag%", g.getTag()));
    }

    public static void register(Player p) {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        User u = UserManager.getUser(p);
        Guild g = u.getGuild();
        for (Guild o : GuildManager.getGuilds()) {
            Team t = sb.getTeam(o.getTag());
            if (t == null) {
                t = sb.registerNewTeam(o.getTag());
            }
            if (g == null) {
                t.setPrefix(parse(Settings.getString("prefixes.enemy"), o));
            }
            else if (g.getTag().equals(o.getTag())) {
                t.setPrefix(parse(Settings.getString("prefixes.friendly"), o));
            }
            else if (o.getAllies().contains(g.getTag())) {
                t.setPrefix(parse(Settings.getString("prefixes.ally"), o));
            }
            else {
                t.setPrefix(parse(Settings.getString("prefixes.enemy"), o));
            }
        }
        Team noGuild = sb.getTeam("noguild");
        if (noGuild == null) {
            noGuild = sb.registerNewTeam("noguild");
            noGuild.setAllowFriendlyFire(true);
            noGuild.setCanSeeFriendlyInvisibles(false);
            noGuild.setPrefix(Util.fixColors(Settings.getString("prefixes.other")));
        }
        p.setScoreboard(sb);
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.getScoreboard()
                    .getTeam(g != null ? g.getTag() : "noguild").addPlayer(p);

            User onlineUser = UserManager.getUser(online);
            Guild onlineGuild = onlineUser.getGuild();

            p.getScoreboard().getTeam(onlineGuild != null ? onlineGuild.getTag() : "noguild")
                    .addPlayer(online);
        }
    }

    public static void createGuild(Guild g, Player p) {
        for (Player o : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = o.getScoreboard();
            Team t = sb.registerNewTeam(g.getTag());
            if (o == p) {
                t.setPrefix(parse(Settings.getString("prefixes.friendly"), g));
            }
            else {
                t.setPrefix(parse(Settings.getString("prefixes.enemy"), g));
            }
            t.addPlayer(p);
        }
    }

    public static void removeGuild(Guild g) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = p.getScoreboard();
            sb.getTeam(g.getTag()).unregister();

            Team noguild = sb.getTeam("noguild");
            for (User player : g.getMembers()) {
                noguild.addPlayer(
                        player.getOfflinePlayer());
            }
        }
    }

    public static void join(Guild g, Player p) {
        for (Player o : Bukkit.getOnlinePlayers()) {
            o.getScoreboard().getTeam(g.getTag()).addPlayer(p);
        }
        p.getScoreboard().getTeam(g.getTag())
                .setPrefix(parse(Settings.getString("prefixes.friendly"), g));
    }

    public static void leave(Guild g, OfflinePlayer p) {
        for (Player o : Bukkit.getOnlinePlayers()) {
            o.getScoreboard().getTeam("noguild").addPlayer(p);
        }
        if (p.isOnline()) {
            p.getPlayer().getScoreboard().getTeam(g.getTag())
                    .setPrefix(parse(Settings.getString("prefixes.enemy"), g));
        }
    }

    public static void createAlliance(Guild g, Guild o) {
        for (User p : g.getMembers()) {
            if (!p.isOnline()) return;
            Team t = p.getPlayer().getScoreboard().getTeam(o.getTag());
            if (t != null) {
                t.setPrefix(parse(
                        Settings.getString("prefixes.ally"), o));
            }
        }
        for (User p : o.getMembers()) {
            if (!p.isOnline()) return;
            Team t = p.getPlayer().getScoreboard().getTeam(g.getTag());
            if (t != null) {
                t.setPrefix(parse(
                        Settings.getString("prefixes.ally"), g));
            }
        }
    }

    public static void removeAlliance(Guild g, Guild o) {
        for (User p : g.getMembers()) {
            if (!p.isOnline()) return;
            Team t = p.getPlayer().getScoreboard().getTeam(o.getTag());
            if (t != null) {
                t.setPrefix(parse(
                        Settings.getString("prefixes.enemy"), o));
            }
        }
        for (User p : o.getMembers()) {
            if (!p.isOnline()) return;
            Team t = p.getPlayer().getScoreboard().getTeam(g.getTag());
            if (t != null) {
                t.setPrefix(parse(
                        Settings.getString("prefixes.enemy"), g));
            }
        }
    }

}
