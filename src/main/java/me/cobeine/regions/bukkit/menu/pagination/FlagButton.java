package me.cobeine.regions.bukkit.menu.pagination;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import lombok.Getter;
import me.cobeine.regions.api.flags.FlagSetting;
import me.cobeine.regions.api.flags.RegionFlag;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.menu.RegionSettingsMenu;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import me.cobeine.regions.bukkit.region.serializers.LocationSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@Getter
public class FlagButton implements PageComponent {
    private  final RegionFlag flag;
    private final BukkitRegion region;
    public FlagButton(RegionFlag flag, BukkitRegion region) {
       this.flag = flag;
       this.region = region;
    }
    @Override
    public ItemStack toItem() {
        return ItemBuilder.legacy(Material.BOOK)
                .setDisplay("&b" + flag.getName())
                .setLore("&7State: " + region.getFlagSetting(flag))
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);
        FlagSetting flagSetting = region.getFlagSetting(flag);
        int index = flagSetting.ordinal();
        int next = index + 1;
        if (next >= FlagSetting.values().length) {
            next = 0;
        }
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        region.setFlagSetting(flag, FlagSetting.values()[next]);
        Pagination pagination = Pagination.auto(SimpleRegions.getInstance().getLotus())
                .creator(new FlagsPage())
                .componentProvider(() -> SimpleRegions.getInstance()
                        .getRegionManager().getFlags().stream().map(e -> new FlagButton(e, region)).toList())
                .build();
        try {
            pagination.open(player);
        } catch (InvalidPageException e) {
            player.sendMessage("You have no flags registered!");
        }
    }
}
