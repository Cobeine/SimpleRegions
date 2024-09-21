package me.cobeine.regions.bukkit.region.flag;

import me.cobeine.regions.api.flags.RegionFlag;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Optional;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public abstract class BasicRegionFlag<T extends Event> implements RegionFlag, Listener {
    private final String name;
    public BasicRegionFlag(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    public abstract void onHandle(T event);

    public Optional<BukkitRegion> getRegion(Location location){
        return Optional.ofNullable(SimpleRegions.getInstance().getRegionManager().get(location));
    }
}
