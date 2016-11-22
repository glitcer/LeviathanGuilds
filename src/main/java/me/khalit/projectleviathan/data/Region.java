package me.khalit.projectleviathan.data;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.Data;
import me.khalit.projectleviathan.api.Removable;
import me.khalit.projectleviathan.data.managers.GuildManager;
import me.khalit.projectleviathan.data.managers.RegionManager;
import me.khalit.projectleviathan.utils.Serializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@lombok.Data
public class Region implements Data, Removable {

    private Guild guild;
    private Location center;
    private int size;
    private World world;
    private Location lower;
    private Location upper;
    private boolean parent;

    public Region(String tag, Location center, int size) {
        this.guild = GuildManager.getGuild(tag);
        this.world = center.getWorld();
        this.center = center;
        this.size = size;
        calculate();
    }

    public void calculate() {
        Vector low = new Vector(center.getBlockX() - this.size, 0, center.getBlockZ() - this.size);
        Vector up = new Vector(center.getBlockX() + this.size, 256, center.getBlockZ() + this.size);
        this.lower = low.toLocation(center.getWorld());
        this.upper = up.toLocation(center.getWorld());
    }

    public boolean isNear(Location to) {
        if (center == null) return false;
        for (Region region : RegionManager.getRegions()) {
            if (region.getCenter() == null) return false;
            if (!center.getWorld().equals(region.getCenter().getWorld())) return false;

            double dis = region.getCenter().distance(to);
            int i = region.getSize();
            return dis < (2 * i + 15);
        }
        return false;
    }

    public boolean isIn(Location location) {
        calculate();
        return !(this.lower == null || this.upper == null || location == null)
                && (location.getBlockX() > this.lower.getBlockX())
                && (location.getBlockX() < this.upper.getBlockX())
                && (location.getBlockY() > this.lower.getBlockY())
                && (location.getBlockY() < this.upper.getBlockY())
                && (location.getBlockZ() > this.lower.getBlockZ())
                && (location.getBlockZ() < this.upper.getBlockZ());
    }

    @Override
    public void delete() {
        try {
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "DELETE FROM `regions` WHERE `guild`=?");
            stmt.setString(1, guild.getTag());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "UPDATE `regions` SET `guild`=?,`center`=?,`size`=?,`world`=?,`parent`=? WHERE `guild`=?");
            stmt.setString(1, guild.getTag());
            stmt.setString(2, Serializer.serializeLocation(center));
            stmt.setInt(3, size);
            stmt.setString(4, world.getName());
            stmt.setString(5, guild.getTag());
            stmt.setBoolean(6, parent);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert() {
        try {
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "INSERT INTO `regions` (`guild`, `center`, `size`, `world`, `parent`) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, guild.getTag());
            stmt.setString(2, Serializer.serializeLocation(center));
            stmt.setInt(3, size);
            stmt.setString(4, world.getName());
            stmt.setBoolean(5, parent);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
