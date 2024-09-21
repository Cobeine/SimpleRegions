package me.cobeine.regions.bukkit.region.flag.impl;

import me.cobeine.regions.api.flags.FlagSetting;
import me.cobeine.regions.bukkit.region.flag.BasicRegionFlag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;


/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class EntityHarmFlag extends BasicRegionFlag<EntityDamageByEntityEvent> {

    public EntityHarmFlag() {
        super("entity_damage");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHandle(EntityDamageByEntityEvent event) {
        var region = getRegion(event.getEntity().getLocation());
        var region2 = getRegion(event.getDamager().getLocation());
        if (region.isPresent() && region2.isPresent()) {
            if (region.get().equals(region2.get())) {
                //same region of action
                FlagSetting setting = region.get().getFlagSetting(this);
                switch (setting) {
                    case NONE -> event.setCancelled(true);
                    case WHITELIST -> {
                        if (!region.get().getWhitelisted().contains(event.getDamager().getName())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
        region.ifPresent(reg -> {
            FlagSetting setting = reg.getFlagSetting(this);
            switch (setting) {
                case NONE -> event.setCancelled(true);
                case WHITELIST -> {
                    if (!reg.getWhitelisted().contains(event.getDamager().getName())) {
                        event.setCancelled(true);
                    }
                }
            }
        });
        region2.ifPresent(reg -> {
            FlagSetting setting = reg.getFlagSetting(this);
            switch (setting) {
                case NONE -> event.setCancelled(true);
                case WHITELIST -> {
                    if (!reg.getWhitelisted().contains(event.getDamager().getName())) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }
}
