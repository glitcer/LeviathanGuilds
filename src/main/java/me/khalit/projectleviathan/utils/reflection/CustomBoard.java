package me.khalit.projectleviathan.utils.reflection;

import com.google.common.base.Splitter;
import lombok.Data;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

@Data
public class CustomBoard {

    private final Scoreboard scoreboard;
    private final String title;
    private List<Team> teams = new ArrayList<>();
    private Map<String, Integer> cells = new HashMap<>();

    public CustomBoard(String title) {
        this.title = Util.fixColors(title);
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public CustomBoard addCell(String string, int score) {
        cells.put(fixScoreboardCell(string), score);
        return this;
    }

    public CustomBoard addCell(String string) {
        cells.put(fixScoreboardCell(string), null);
        return this;
    }

    public void reset() {
        cells.clear();
        teams.forEach(Team::unregister);
        teams.clear();
    }

    public void send(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    public CustomBoard build() {
        Objective obj = scoreboard
                .registerNewObjective((title.length() > 16 ? title.substring(0, 15) : title), "dummy");
        obj.setDisplayName(title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int size = cells.size();

        for (Map.Entry<String, Integer> text : cells.entrySet()) {
            Map.Entry<Team, String> team =
                    createTeam(text.getKey());
            Integer score =
                    text.getValue() != null ? text.getValue() : size;
            OfflinePlayer player =
                    Bukkit.getOfflinePlayer(team.getValue());
            if (team.getKey() != null) team.getKey().addPlayer(player);
            obj.getScore(player).setScore(score);
            size -= 1;
        }
        return this;
    }

    private String fixScoreboardCell(String text) {
        while (cells.containsKey(text))
            text += "§r";
        if (text.length() > 48)
            text = text.substring(0, 47);
        return Util.fixColors(text);
    }

    private Map.Entry<Team, String> createTeam(String content) {
        if (content.length() <= 16)
            return new AbstractMap.SimpleEntry<>(null, content);
        Team team = scoreboard.registerNewTeam("cell/" + scoreboard.getTeams().size());
        Iterator<String> iterator = Splitter.fixedLength(16).split(content).iterator();
        team.setPrefix(iterator.next());
        content = iterator.next();
        if (content.length() > 32)
            team.setSuffix(iterator.next());
        teams.add(team);
        return new AbstractMap.SimpleEntry<>(team, content);
    }
}
