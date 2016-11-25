package me.khalit.projectleviathan.data.managers;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.data.Region;
import me.khalit.projectleviathan.utils.Serializer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class RegionManager {

    @Getter
    private static final List<Region> regions = new ArrayList<>();

    public static void loadRegions() {
        try {
            Main.getSqlHandler().query("SELECT * FROM regions", result -> {
                while (result.next()) {
                    Region region = new Region(result.getString("guild"),
                            Serializer.deserializeLocation(result.getString("center")), result.getInt("size"));
                    region.setParent(result.getBoolean("parent"));
                    regions.add(region);

                    if (region.isParent()) {
                        region.getGuild().getConqueredRegions().add(region);
                    }
                    else {
                        region.getGuild().setRegion(region);
                    }
                }
            });
        } catch (Exception e) {
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
