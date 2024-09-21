package me.cobeine.regions.bukkit.chat;

import me.cobeine.regions.bukkit.chat.impl.AddWhitelistPrompt;
import me.cobeine.regions.bukkit.chat.impl.RemoveWhitelistPrompt;
import me.cobeine.regions.bukkit.chat.impl.RenameChatPrompt;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public interface ChatPrompts {
    Set<UUID> ONGOING_PROMPTS = new HashSet<>();

    TextCallback RENAME = new RenameChatPrompt();
    TextCallback REMOVE_WHITELIST = new RemoveWhitelistPrompt();
    TextCallback ADD_WHITELIST = new AddWhitelistPrompt();

    static void create(Player player, BukkitRegion region, TextCallback call) {
        if (ONGOING_PROMPTS.contains(player.getUniqueId())) {
            player.sendMessage("You have an ongoing prompt! type \"cancel\" to cancel first!");
            return;
        }
        ONGOING_PROMPTS.add(player.getUniqueId());
        new ChatPrompt(player,region, call);
    }
}
