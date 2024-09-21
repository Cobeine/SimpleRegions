package me.cobeine.regions.bukkit.commands.sub;

import me.cobeine.regions.api.commands.SubCommand;
import me.cobeine.regions.api.commands.SubCommandInfo;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.region.PlayerWand;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@SubCommandInfo(
        name = "wand",
        syntax = "Gives you the wand"
)
public class WandSubCommand implements SubCommand<Player> {

    @Override
    public void execute(Player player, String[] args) {
        player.getInventory().addItem(PlayerWand.wand());
        player.sendMessage("Left click to set pos1, right click to set pos2");
    }


}
