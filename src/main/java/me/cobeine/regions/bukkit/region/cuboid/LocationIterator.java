package me.cobeine.regions.bukkit.region.cuboid;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class LocationIterator implements Iterator<Location> {
    private final World world;
    private final int baseX, baseY, baseZ, sizeX, sizeY, sizeZ;
    private int x,y,z;
    public LocationIterator(Location pos1, Location pos2) {
        this.world = pos1.getWorld();
        this.baseX = Math.min(pos1.getBlockX(),  pos2.getBlockX());
        this.baseY = Math.min(pos1.getBlockY(),  pos2.getBlockY());
        this.baseZ = Math.min(pos1.getBlockZ(),  pos2.getBlockZ());
        this.sizeX = Math.abs(pos1.getBlockX() - pos2.getBlockX()) + 1;
        this.sizeY = Math.abs(pos1.getBlockY() - pos2.getBlockY()) + 1;
        this.sizeZ = Math.abs(pos1.getBlockZ() - pos2.getBlockZ()) + 1;
        this.z = 0;
        this.y = 0;
        this.x = 0;
    }
    @Override
    public boolean hasNext() {
        return (this.x < this.sizeX) && (this.y < this.sizeY) && (this.z < this.sizeZ);
    }

    @Override
    public Location next() {
        final Block block = this.world.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
        if (++this.x >= this.sizeX) {
            this.x = 0;
            if (++this.y >= this.sizeY) {
                this.y = 0;
                this.z += 1;
            }
        }
        return block.getLocation();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
