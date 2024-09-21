package me.cobeine.regions.bukkit.region.flag.impl;

import me.cobeine.regions.api.flags.FlagSetting;
import me.cobeine.regions.bukkit.region.flag.BasicRegionFlag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;


/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class InteractFlag extends BasicRegionFlag<PlayerInteractEvent> {

    public InteractFlag() {
        super("interact");
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHandle(PlayerInteractEvent event) {
        getRegion(event.getInteractionPoint()).ifPresent(region -> {

            FlagSetting setting = region.getFlagSetting(this);
        switch (setting){
            case NONE -> event.setCancelled(true);
            case WHITELIST -> {
                if (!region.getWhitelisted().contains(event.getPlayer().getName())) {
                    event.setCancelled(true);
                }
            }
        }
        });

    }
}
