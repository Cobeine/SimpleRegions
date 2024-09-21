package me.cobeine.regions.bukkit.region.serializers;


import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class LocationSerializer {

    public static String serialize(Location location) {
        return String.format("%s,%s,%s,%s", location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Location deSerialize(String value) {
        String[] vars = value.split(",");
        if (vars.length != 4) {
            return null;
        }
        if (Bukkit.getWorld(vars[0]) == null) {
            return null;
        }
        return new Location(Bukkit.getWorld(vars[0]), Integer.parseInt(vars[1]), Integer.parseInt(vars[2]), Integer.parseInt(vars[3]));
    }

    public static String readable(Location vertex) {
        return String.format("(%d, %d, %d)", vertex.getBlockX(), vertex.getBlockY(), vertex.getBlockZ());
    }
}
