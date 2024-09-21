package me.cobeine.regions.bukkit.region;

import lombok.Getter;
import me.cobeine.regions.api.flags.RegionFlag;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.database.DatabaseManager;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import me.cobeine.regions.bukkit.region.flag.BasicRegionFlag;
import me.cobeine.regions.bukkit.region.flag.impl.BlockBreakFlag;
import me.cobeine.regions.bukkit.region.flag.impl.BlockPlaceFlag;
import me.cobeine.regions.bukkit.region.flag.impl.EntityHarmFlag;
import me.cobeine.regions.bukkit.region.flag.impl.InteractFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

@Getter
public class RegionManager {
    private final List<BukkitRegion> regions;
    private final List<BasicRegionFlag<?>> flags;
    private HashMap<UUID,PlayerWand> wands;

    public RegionManager() {
        this.regions = new ArrayList<>();
        this.wands = new HashMap<>();
        this.flags = new ArrayList<>();
        registerFlag(new BlockBreakFlag());
        registerFlag(new BlockPlaceFlag());
        registerFlag(new InteractFlag());
        registerFlag(new EntityHarmFlag());
    }

    public void registerFlag(BasicRegionFlag<?> blockBreakFlag) {
        flags.add(blockBreakFlag);
        SimpleRegions.getInstance().getServer().getPluginManager().registerEvents(blockBreakFlag, SimpleRegions.getInstance());
    }


    public void addRegion(BukkitRegion region) {
        regions.add(region);
    }

    public PlayerWand getPlayerWand(Player player) {
        return wands.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerWand());
    }

    public BukkitRegion get(Location location) {
        return regions.stream().filter(e -> e.contains(location)).findFirst().orElse(null);
    }

    public BukkitRegion get(String name) {
        return regions.stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public RegionFlag getFlag(String flag) {
        return flags.stream().filter(e -> e.getName().equalsIgnoreCase(flag)).findFirst().orElse(null);
    }
}
