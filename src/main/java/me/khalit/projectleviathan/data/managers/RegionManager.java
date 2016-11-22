package me.khalit.projectleviathan.data.managers;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.Region;
import me.khalit.projectleviathan.utils.Serializer;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionManager {

    @Getter
    private static final List<Region> regions = new ArrayList<>();

    public static void loadRegions() {
        try {
            PreparedStatement stmt = Main.getSqlHandler().getConnection().prepareStatement(
                    "SELECT * FROM regions");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Region region = new Region(rs.getString("guild"),
                        Serializer.deserializeLocation(rs.getString("center")), rs.getInt("size"));
                region.setParent(rs.getBoolean("parent"));
                regions.add(region);

                if (region.isParent()) {
                    region.getGuild().getConqueredRegions().add(region);
                }
                else {
                    region.getGuild().setRegion(region);
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Region getRegion(String tag) {
        for (Region region : regions) {
            if (region != null && region.getGuild() != null && region.getGuild()
                    .getTag().equalsIgnoreCase(tag)) {
                return region;
            }
        }
        return null;
    }

    public static boolean isIn(Location loc) {
        for (Region region : regions) {
            if (region.isIn(loc)) {
                return true;
            }
        }
        return false;
    }

    public static Region getAt(Location loc) {
        for (Region region : regions) {
            if (region.isIn(loc)) {
                return region;
            }
        }
        return null;
    }

    public static boolean isNear(Location center) {
        if (center == null) {
            return false;
        }
        for (Region region : regions) {
            if (region.getCenter() == null) {
                return false;
            }
            if (!center.getWorld().equals(region.getCenter().getWorld())) {
                return false;
            }
            double distance = center.distance(region.getCenter());
            int i = region.getSize();
            return distance < (2 * i + 70);
        }
        return false;
    }

}
