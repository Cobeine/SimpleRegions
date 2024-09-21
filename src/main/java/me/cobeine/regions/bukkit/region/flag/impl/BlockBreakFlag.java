package me.cobeine.regions.bukkit.region.flag.impl;

import me.cobeine.regions.api.flags.FlagSetting;
import me.cobeine.regions.bukkit.region.flag.BasicRegionFlag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;


/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class BlockBreakFlag extends BasicRegionFlag<BlockBreakEvent> {

    public BlockBreakFlag() {
        super("block_break");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHandle(BlockBreakEvent event) {
        getRegion(event.getBlock().getLocation()).ifPresent(region -> {
            FlagSetting setting = region.getFlagSetting(this);
            switch (setting) {
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
