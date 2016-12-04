package me.khalit.projectleviathan.data.managers;

import lombok.Getter;
import lombok.NonNull;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.Region;
import me.khalit.projectleviathan.utils.Serializer;
import org.bukkit.Location;

import java.util.Map;
import java.util.WeakHashMap;

public class RegionManager {

    @Getter
    private static final Map<String, Region> regions = new WeakHashMap<>();

    /*
    public static void loadRegions() {
        try {
            Main.getSqlHandler().query("SELECT * FROM regions", result -> {
                while (result.next()) {
                    Region region = new Region(result.getString("guild"),
                            Serializer.deserializeLocation(result.getString("center")), result.getInt("size"));
                    region.setParent(result.getBoolean("parent"));
                    regions.put(result.getString("guild"), region);

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
    */

    @NonNull
    public static Region getRegion(Guild guild) {
        return getRegion(guild.getTag());
    }

    @NonNull
    public static Region getRegion(String tag) {
        return regions.get(tag);
    }

    @NonNull
    public static boolean isIn(Location loc) {
        return regions.values().stream().filter(u -> u.isIn(loc)).findFirst().orElse(null) != null;
    }

    @NonNull
    public static Region getAt(Location loc) {
        return regions.values().stream().filter(u -> u.isIn(loc)).findFirst().orElse(null);
    }

    @NonNull
    public static boolean isNear(Location center) {
        if (center == null) {
            return false;
        }
        for (Region region : regions.values()) {
            if (region.getCenter() == null || !center.getWorld().equals(region.getCenter().getWorld())) {
                return false;
            }
            double distance = center.distance(region.getCenter());
            int i = region.getSize();
            return distance < (2 * i + 70);
        }
        return false;
    }

}
