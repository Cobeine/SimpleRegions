package me.cobeine.regions.bukkit.chat;

import me.cobeine.regions.api.region.Region;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.entity.Player;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@FunctionalInterface
public interface TextCallback {
    void call(Player player, BukkitRegion region, String text);
}
