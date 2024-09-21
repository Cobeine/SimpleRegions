package me.cobeine.regions.bukkit.region.cuboid;

import lombok.Getter;
import lombok.Setter;
import me.cobeine.regions.api.region.Region;
import me.cobeine.regions.api.flags.FlagSetting;
import me.cobeine.regions.api.flags.RegionFlag;
import me.cobeine.regions.bukkit.region.serializers.FlagsSerializer;
import me.cobeine.regions.bukkit.region.serializers.LocationSerializer;
import me.cobeine.regions.bukkit.region.serializers.WhitelistSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@Getter
public class BukkitRegion implements Region<Location> {
    private final @NonNull Location[] vertices;
    private final @NonNull Set<String> whitelisted;
    private final @NonNull HashMap<String,FlagSetting> flags;
    private final UUID uuid;
    @Setter
    private String name;

    public BukkitRegion(UUID uuid,String name, @NonNull Location[] vertices, @NonNull  HashMap<String,FlagSetting> flags,
                        @NonNull  Set<String> whitelisted) {
        this.vertices = vertices;
        this.name = name;
        this.flags = flags;
        this.whitelisted = whitelisted;
        this.uuid = uuid;
    }

    public BukkitRegion(UUID uuid, String name, Location position1, Location position2) {
        this(uuid,name, new Location[2], new HashMap<>(), new HashSet<>());
        vertices[0] = min(position1,position2);
        vertices[1] = max(position1,position2);
    }

    public BukkitRegion(UUID uuid,String name, Location pos1, Location pos2, HashMap<String, FlagSetting> flags, Set<String> users) {
        this(uuid,name, new Location[]{pos1, pos2}, flags, users);
    }

    @NotNull
    @Override
    public Iterator<Location> iterator() {
        return new LocationIterator(vertices[0],vertices[1]);
    }

    private Location min(Location pos1, Location pos2) {
        return new Location(pos1.getWorld(), Math.min(pos1.getBlockX(), pos2.getBlockX()),
                Math.min(pos1.getBlockY(), pos2.getBlockY()),
                Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    }
    private Location max(Location pos1, Location pos2) {
        return new Location(pos1.getWorld(), Math.max(pos1.getBlockX(), pos2.getBlockX()),
                Math.max(pos1.getBlockY(), pos2.getBlockY()),
                Math.max(pos1.getBlockZ(), pos2.getBlockZ()));

    }

    @Override
    public FlagSetting getFlagSetting(RegionFlag flag) {
        return flags.getOrDefault(flag.getName(), FlagSetting.NOT_SET);
    }

    public static BukkitRegion deserialize(UUID uuid,String name, String pos1, String pos2, String flags, String whitelisted) {
        return new BukkitRegion(uuid,name,
                LocationSerializer.deSerialize(pos1),
                LocationSerializer.deSerialize(pos2),
                FlagsSerializer.deSerialize(flags),
                WhitelistSerializer.deSerialize(whitelisted));
    }

    public void setFlagSetting(RegionFlag flag, FlagSetting value) {
        flags.put(flag.getName(), value);
    }

    public boolean contains(Location location) {
        if ((location == null) || (this.vertices[0].getWorld() == null)) {
            return false;
        }
        World world = location.getWorld();
        return (world != null) && (this.vertices[0].getWorld().getName().equals(location.getWorld().getName()))
                && (contains(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }
    public boolean contains(int x, int y, int z) {
        int x1 = vertices[0].getBlockX();
        int x2 = vertices[1].getBlockX();
        int y1 = vertices[0].getBlockY();
        int y2 = vertices[1].getBlockY();
        int z1 = vertices[0].getBlockZ();
        int z2 = vertices[1].getBlockZ();
        return (x >= x1) && (x <= x2) && (y >= y1) && (y <= y2) && (z >= z1) && (z <= z2);
    }
}
