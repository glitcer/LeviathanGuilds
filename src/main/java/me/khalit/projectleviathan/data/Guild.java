package me.khalit.projectleviathan.data;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.Data;
import me.khalit.projectleviathan.api.Hologram;
import me.khalit.projectleviathan.api.Removable;
import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.data.managers.GuildManager;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.Serializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
public class Guild implements Data, Removable {

    private String tag, name;
    private User owner;
    private List<User> members;
    private Region region;
    private List<Region> conqueredRegions;
    private Rank rank;
    private long validity, protection, born;
    private List<Guild> allies, allyInvites;
    private Location home;
    private ItemStack[] treasury;
    private int money;
    private boolean pvp, occupied;

    public Guild(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    @Override
    public void save() {
        try {
            Main.getSqlHandler().execute("UPDATE guilds SET `tag`=" + tag
                    + ", `name`=" + name +
                    ", `owner`=" + owner +
                    ", `members`=" + Serializer.serializeList(UserManager.toStrings(members)) +
                    ", `kills`=" + rank.getKills() + "" +
                    ", `deaths`=" + rank.getDeaths() + "" +
                    ", `validity`=" + validity + "" +
                    ", `protection`=" + protection + "" +
                    ", `born`=" + born + "" +
                    ", `allies`=" + Serializer.serializeList(GuildManager.toStrings(allies)) + "" +
                    ", `home`=" + Serializer.serializeLocation(home) + "" +
                    ", `treasury`=" + Serializer.serializeInventory(treasury) + "" +
                    ", `money`=" + money + "," +
                    ", `pvp`=" + pvp + "" +
                    ", `occupied`=" + occupied + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert() {
        try {
            Main.getSqlHandler().execute("INSERT INTO guilds VALUES (`tag`=" + tag
                    + ", `name`=" + name +
                    ", `owner`=" + owner +
                    ", `members`=" + Serializer.serializeList(UserManager.toStrings(members)) +
                    ", `kills`=" + rank.getKills() + "" +
                    ", `deaths`=" + rank.getDeaths() + "" +
                    ", `validity`=" + validity + "" +
                    ", `protection`=" + protection + "" +
                    ", `born`=" + born + "" +
                    ", `allies`=" + Serializer.serializeList(GuildManager.toStrings(allies)) + "" +
                    ", `home`=" + Serializer.serializeLocation(home) + "" +
                    ", `treasury`=" + Serializer.serializeInventory(treasury) + "" +
                    ", `money`=" + money + "," +
                    ", `pvp`=" + pvp + "" +
                    ", `occupied`=" + occupied + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            Main.getSqlHandler().execute("DELETE FROM `guilds` WHERE `tag`=" + tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
