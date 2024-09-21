package me.cobeine.regions.bukkit.chat.impl;

import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.chat.TextCallback;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.entity.Player;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class RenameChatPrompt implements TextCallback {
    private final String REGEX = "^[a-zA-Z0-9]+$";
    @Override
    public void call(Player player, BukkitRegion region, String text) {
        var manager = SimpleRegions.getInstance().getRegionManager();
        if (!text.matches(REGEX) || text.contains(" ")) {
            player.sendMessage("Invalid region name!");
            return;
        }
        if (manager.getRegions().stream().anyMatch(e -> e.getName().equalsIgnoreCase(text))) {
            player.sendMessage("A region with that name already exists!");
            return;
        }
        region.setName(text);
        player.sendMessage("Region renamed to " + text);
    }
}
