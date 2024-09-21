package me.cobeine.regions.bukkit.chat.impl;

import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.chat.TextCallback;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.entity.Player;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class AddWhitelistPrompt implements TextCallback {
    private final String REGEX = "^[a-zA-Z0-9]+$";
    @Override
    public void call(Player player, BukkitRegion region, String text) {
        var manager = SimpleRegions.getInstance().getRegionManager();
        if (!text.matches(REGEX) || text.contains(" ")) {
            player.sendMessage("Invalid username!");
            return;
        }
        if (region.getWhitelisted().contains(text)) {
            player.sendMessage("This member is already whitelisted!");
            return;
        }
        region.getWhitelisted().add(text);
        player.sendMessage("User " + text + " has been added");
    }
}
