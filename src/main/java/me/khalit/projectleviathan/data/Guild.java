package me.khalit.projectleviathan.data;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.Data;
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
            /*
            PreparedStatement statement = Main.getSqlHandler().getConnection().prepareStatement(
                    "UPDATE guilds SET `tag`=?,`name`=?,`owner`=?,`members`=?,`kills`=?,`deaths`=?,`validity`=?,`protection`=?," +
                            "`born`=?,`allies`=?,`home`=?,`treasury`=?,`money`=?,`pvp`=?,`occupied`=?, WHERE tag=?");
            statement.setString(1, tag);
            statement.setString(2, name);
            statement.setString(3, Serializer.serializeList(UserManager.toStrings(members)));
            statement.setInt(4, rank.getKills());
            statement.setInt(5, rank.getDeaths());
            statement.setLong(6, validity);
            statement.setLong(7, protection);
            statement.setLong(8, born);
            statement.setString(9, Serializer.serializeList(GuildManager.toStrings(allies)));
            statement.setString(10, Serializer.serializeLocation(home));
            statement.setString(11, Serializer.serializeInventory(treasury));
            statement.setInt(12, money);
            statement.setBoolean(13, pvp);
            statement.setBoolean(14, occupied);
            statement.setString(15, tag);
            statement.executeUpdate();
            statement.close();
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert() {
        try {
            /*
            PreparedStatement statement = Main.getSqlHandler().getConnection().prepareStatement(
                    "INSERT INTO guilds VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, tag);
            statement.setString(2, name);
            statement.setString(3, Serializer.serializeList(UserManager.toStrings(members)));
            statement.setInt(4, rank.getKills());
            statement.setInt(5, rank.getDeaths());
            statement.setLong(6, validity);
            statement.setLong(7, protection);
            statement.setLong(8, born);
            statement.setString(9, Serializer.serializeList(GuildManager.toStrings(allies)));
            statement.setString(10, Serializer.serializeLocation(home));
            statement.setString(11, Serializer.serializeInventory(treasury));
            statement.setInt(12, money);
            statement.setBoolean(13, pvp);
            statement.setBoolean(14, occupied);
            statement.executeUpdate();
            statement.close();
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            /*
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "DELETE FROM `guilds` WHERE `tag`=?");
            stmt.setString(1, tag);
            stmt.executeUpdate();
            stmt.close();
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
