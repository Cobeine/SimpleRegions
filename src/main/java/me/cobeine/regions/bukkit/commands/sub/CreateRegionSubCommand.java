package me.cobeine.regions.bukkit.commands.sub;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import me.cobeine.regions.api.commands.Invocation;
import me.cobeine.regions.api.commands.SubCommand;
import me.cobeine.regions.api.commands.SubCommandInfo;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.region.PlayerWand;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@SubCommandInfo(
        name = "create",
        requiredLength = 1,
        syntax = "Create a new region"
)
public class CreateRegionSubCommand implements SubCommand<Player> {
    private final String REGEX = "^[a-zA-Z0-9]+$";

    @Override
    public void execute(Player player, String[] args) {
        String name = args[0];
        if (SimpleRegions.getInstance().getRegionManager().getRegions().stream().anyMatch(e -> e.getName().equalsIgnoreCase(name))) {
            player.sendMessage("Region with that name already exists");
            return;
        }
        if (!name.matches(REGEX)) {
            player.sendMessage("Invalid region name!");
            return;
        }
        PlayerWand wand = SimpleRegions.getInstance().getRegionManager().getPlayerWand(player);
        if (wand.getPos1() == null || wand.getPos2() == null) {
            player.sendMessage("One or more positions is not set!");
            return;
        }
        if (!wand.getPos1().getWorld().equals(wand.getPos2().getWorld())) {
            player.sendMessage("Both wand positions must be in the same world!");
            return;
        }
        BukkitRegion region = new BukkitRegion(UUID.randomUUID(), name, wand.getPos1(), wand.getPos2());
        SimpleRegions.getInstance().getRegionManager().addRegion(region);
        player.sendMessage("Region created!");
    }


}
