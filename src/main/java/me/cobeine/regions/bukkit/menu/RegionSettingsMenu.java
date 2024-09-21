package me.cobeine.regions.bukkit.menu;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.chat.ChatPrompt;
import me.cobeine.regions.bukkit.chat.ChatPrompts;
import me.cobeine.regions.bukkit.menu.pagination.FlagButton;
import me.cobeine.regions.bukkit.menu.pagination.FlagsPage;
import me.cobeine.regions.bukkit.menu.pagination.RegionButton;
import me.cobeine.regions.bukkit.menu.pagination.RegionPage;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class RegionSettingsMenu implements Menu {
    private  final BukkitRegion region;
    public RegionSettingsMenu(BukkitRegion region) {
        this.region = region;
    }

    @Override
    public String getName() {
        return "Region-Settings";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        return MenuTitles.createLegacy("Region: " + region.getName());
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(1);
    }

    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player, Capacity capacity) {
        return Content.builder(capacity)
                .setButton(0,Button.clickable(ItemBuilder.legacy(Material.PAPER).setDisplay("&bRename").build(),ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    player.sendMessage("Please type the new name in chat");
                    ChatPrompts.create(player, region, ChatPrompts.RENAME);
                })))
                .setButton(2,Button.clickable(ItemBuilder.legacy(Material.GREEN_TERRACOTTA).setDisplay("&bAdd Whitelist").build(),ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    player.sendMessage("Please type username in chat");
                    ChatPrompts.create(player, region, ChatPrompts.ADD_WHITELIST);

                })))
                .setButton(4,Button.clickable(ItemBuilder.legacy(Material.RED_TERRACOTTA).setDisplay("&bRemove Whitelist").build(),ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    player.sendMessage("Please type username in chat");
                    ChatPrompts.create(player, region, ChatPrompts.REMOVE_WHITELIST);
                })))
                .setButton(6,Button.clickable(ItemBuilder.legacy(Material.WOODEN_AXE).setDisplay("&bRe-set positions").build(),ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    inventoryClickEvent.getWhoClicked().closeInventory();
                    player.sendMessage("Please set the new positions");
                })))
                .setButton(8, Button.clickable(ItemBuilder.legacy(Material.BOOK).setDisplay("&bFlags").build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                    inventoryClickEvent.setCancelled(true);
                    inventoryClickEvent.getWhoClicked().closeInventory();
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
                })))
                .build();
    }
}
