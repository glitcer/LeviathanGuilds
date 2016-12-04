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
        return true; // TODO: 04.12.2016
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
            Main.getSqlHandler().execute("DELETE FROM `regions` WHERE `guild`=" + guild.getTag() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
           // Main.getSqlHandler().execute("UPDATE `regions` SET `guild`=" + guild.getTag() + ", `center`=" + Serializer.serializeLocation(center) + ", `size`=" + size + ", `world`=" + world.getName() + ", `parent`=" + parent + " WHERE `guild`=" + guild + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert() {
        try { // TODO: 04.12.2016
            /*
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "INSERT INTO `regions` (`guild`, `center`, `size`, `world`, `parent`) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, guild.getTag());
            stmt.setString(2, Serializer.serializeLocation(center));
            stmt.setInt(3, size);
            stmt.setString(4, world.getName());
            stmt.setBoolean(5, parent);
            stmt.executeUpdate();
            stmt.close();
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
