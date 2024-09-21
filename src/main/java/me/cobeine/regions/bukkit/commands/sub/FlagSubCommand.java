package me.cobeine.regions.bukkit.commands.sub;

import me.cobeine.regions.api.commands.Invocation;
import me.cobeine.regions.api.commands.SubCommand;
import me.cobeine.regions.api.commands.SubCommandInfo;
import me.cobeine.regions.api.flags.FlagSetting;
import me.cobeine.regions.api.flags.RegionFlag;
import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import me.cobeine.regions.bukkit.region.flag.BasicRegionFlag;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@SubCommandInfo(
        name = "flag",
        requiredLength = 2,
        syntax = "<region> <flag> <state>",
        description = "Change a flag state"
)
public class FlagSubCommand implements SubCommand<Player> {

    @Override
    public void execute(Player player, String[] args) {
        String name = args[0];
        if (SimpleRegions.getInstance().getRegionManager().getRegions().stream().noneMatch(e -> e.getName().equalsIgnoreCase(name))) {
            player.sendMessage("Region with that name does not exist");
            return;
        }
        BukkitRegion region = SimpleRegions.getInstance().getRegionManager().get(name);
        String flag = args[1];
        String state = args[2];
        RegionFlag target = SimpleRegions.getInstance().getRegionManager().getFlag(flag);
        if (target == null) {
            player.sendMessage("Invalid flag");
            return;
        }
        try {
            FlagSetting setting = FlagSetting.valueOf(state);
            region.getFlags().put(target.getName(), setting);
        } catch (Exception e) {
            player.sendMessage("Invalid flag state!");
        }
    }

    @Override
    public HashMap<Integer, List<String>> complete(Invocation<Player> invocation, HashMap<Integer, List<String>> map) {
        map.put(1, SimpleRegions.getInstance().getRegionManager().getRegions().stream().map(BukkitRegion::getName).toList());
        map.put(2, SimpleRegions.getInstance().getRegionManager().getFlags().stream().map(BasicRegionFlag::getName).toList());
        map.put(3, Arrays.stream(FlagSetting.values()).map(Enum::name).toList());
        return map;
    }
}
