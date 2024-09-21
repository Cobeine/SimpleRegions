package me.cobeine.regions.bukkit.menu.pagination;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.ComponentItemBuilder;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import lombok.Getter;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.menu.RegionSettingsMenu;
import me.cobeine.regions.bukkit.region.RegionManager;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import me.cobeine.regions.bukkit.region.serializers.LocationSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@Getter
public class RegionButton implements PageComponent {
    private final BukkitRegion region;
    public RegionButton(BukkitRegion region) {
        this.region = region;
    }
    @Override
    public ItemStack toItem() {
        List<String> lore = new ArrayList<>();
        lore.add("&bPositions &7" + LocationSerializer.readable(region.getVertices()[0]) + " " + LocationSerializer.readable(region.getVertices()[1]));
        lore.add("&bFlags &7");
        if (getRegion().getFlags().isEmpty()) {
            lore.add("&7- No flag set");
        }
        for (String s : getRegion().getFlags().keySet()) {
            lore.add("&7- " + s + ": &b" + getRegion().getFlags().get(s).name());
        }
        return ItemBuilder.legacy(Material.BOOK)
                .setDisplay("&b" + region.getName())
                .setLore(lore)
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);
        SimpleRegions.getInstance().getLotus().openMenu((Player) event.getWhoClicked(), new RegionSettingsMenu(region));
    }
}
