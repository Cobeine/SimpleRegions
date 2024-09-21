package me.cobeine.regions.bukkit.commands.sub;

import me.cobeine.regions.api.commands.Invocation;
import me.cobeine.regions.api.commands.SubCommand;
import me.cobeine.regions.api.commands.SubCommandInfo;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.chat.ChatPrompts;
import me.cobeine.regions.bukkit.chat.impl.AddWhitelistPrompt;
import me.cobeine.regions.bukkit.chat.impl.RemoveWhitelistPrompt;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@SubCommandInfo(
        name = "remove",
        requiredLength = 2,
        syntax = "<region> <name>",
        description = "de-whitelist a user from the region"
)
public class RemoveSubCommand implements SubCommand<Player> {

    @Override
    public void execute(Player player, String[] args) {
        String name = args[0];
        if (SimpleRegions.getInstance().getRegionManager().getRegions().stream().noneMatch(e -> e.getName().equalsIgnoreCase(name))) {
            player.sendMessage("Region with that name does not exist");
            return;
        }
        BukkitRegion region = SimpleRegions.getInstance().getRegionManager().get(name);
        new RemoveWhitelistPrompt().call(player, region, name);
    }
    @Override
    public HashMap<Integer, List<String>> complete(Invocation<Player> invocation, HashMap<Integer, List<String>> map) {
        map.put(1, SimpleRegions.getInstance().getRegionManager().getRegions().stream().map(BukkitRegion::getName).toList());
        map.put(2, Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        return map;
    }

}