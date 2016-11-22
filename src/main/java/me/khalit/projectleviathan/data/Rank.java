package me.khalit.projectleviathan.data;

import lombok.Data;
import me.khalit.projectleviathan.api.RankType;
import me.khalit.projectleviathan.utils.Util;

@Data
public class Rank implements Comparable<Rank> {

    private User user;
    private Guild guild;
    private RankType type;

    private int points;
    private int kills;
    private int deaths;

    public Rank(Guild guild) {
        this.guild = guild;
        this.type = RankType.GUILD;
        getPoints();
    }

    public Rank(User user) {
        this.user = user;
        this.type = RankType.USER;
    }

    public double getRatio() {
        double d = deaths;
        double k = kills;
        if (d <= 0) {
            return k;
        }
        return Util.round(k / d, 2);
    }

    public int getPoints() {
        if (this.type == RankType.USER) {
            return this.points;
        }

        double points = 0;
        int size = guild.getMembers().size();
        if (size <= 1) return 1000;

        for (User user : guild.getMembers()) {
            points += user.getRank().getPoints();
        }

        double result = points / size;
        if (result != this.points) {
            this.points = (int) result;
        }
        return this.points;
    }

    public void addPoints(int i) {
        this.points += i;
    }

    public void addKill() {
        this.kills += 1;
    }

    public void addDeath() {
        this.deaths += 1;
    }

    public void removePoints(int i) {
        this.points -= i;
        if (this.points < 1)
            this.points = 0;
    }

    @Override
    public int compareTo(Rank rank) {
        return Integer.compare(rank.getPoints(), this.points);
    }
}
