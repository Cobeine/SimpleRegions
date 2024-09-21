package me.cobeine.regions.bukkit.listener;

import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.region.PlayerWand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class RegionListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)
            return;

        if (event.getItem().getType().equals(Material.AIR))
            return;

        if (!event.getItem().isSimilar(PlayerWand.wand()))
            return;

        event.setCancelled(true);
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            SimpleRegions.getInstance().getRegionManager().getPlayerWand(player).setPos1(Objects.requireNonNull(event.getClickedBlock()).getLocation());
            event.getPlayer().sendMessage("Pos1 set!");
        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            SimpleRegions.getInstance().getRegionManager().getPlayerWand(player).setPos2(Objects.requireNonNull(event.getClickedBlock()).getLocation());
            event.getPlayer().sendMessage("Pos2 set!");
        }
    }

}
