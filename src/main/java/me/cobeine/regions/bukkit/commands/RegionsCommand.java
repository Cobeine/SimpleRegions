package me.cobeine.regions.bukkit.commands;

import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import me.cobeine.regions.api.commands.CommandModule;
import me.cobeine.regions.api.commands.Invocation;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.commands.sub.*;
import me.cobeine.regions.bukkit.menu.RegionSettingsMenu;
import me.cobeine.regions.bukkit.menu.pagination.RegionButton;
import me.cobeine.regions.bukkit.menu.pagination.RegionPage;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class RegionsCommand extends CommandModule<Player> {


    public RegionsCommand(JavaPlugin provider) {
        super("region", provider);
        registerSubCommand(new AddSubCommand());
        registerSubCommand(new CreateRegionSubCommand());
        registerSubCommand(new FlagSubCommand());
        registerSubCommand(new ListSubCommand());
        registerSubCommand(new RemoveSubCommand());
        registerSubCommand(new RenameSubCommand());
        registerSubCommand(new WandSubCommand());
    }

    @Override
    public void defaultExecution(Invocation<Player> invocation) {
        Player player = invocation.getCommandSender();
        if (invocation.getAllArgs().length == 0) {
            Pagination pagination = Pagination.auto(SimpleRegions.getInstance().getLotus())
                    .creator(new RegionPage())
                    .componentProvider(() -> SimpleRegions.getInstance().getRegionManager().getRegions().stream().map(RegionButton::new).toList())
                    .build();
            try {
                pagination.open(player);
            } catch (InvalidPageException e) {
                player.sendMessage("You have no regions set!");
            }
            return;
        }
        String name = invocation.getAllArgs()[0];
        var region = SimpleRegions.getInstance().getRegionManager().get(name);
        if (region != null) {
            SimpleRegions.getInstance().getLotus().openMenu(player, new RegionSettingsMenu(region));
        }
        super.defaultExecution(invocation);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation<Player> invocation) {
        if (invocation.getAllArgs().length == 1) {
            List<String> completes = new ArrayList<>(getSubNames());
            completes.addAll(SimpleRegions.getInstance().getRegionManager().getRegions().stream().map(BukkitRegion::getName).toList());
            return CompletableFuture.completedFuture(completes);
        }
        return super.suggestAsync(invocation);
    }
}
