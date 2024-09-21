package me.cobeine.regions.api.region;

import me.cobeine.regions.api.flags.FlagSetting;
import me.cobeine.regions.api.flags.RegionFlag;
import me.cobeine.regions.bukkit.region.flag.impl.BlockBreakFlag;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public interface Region<T> extends Iterable<T>{

    String getName();

    T[] getVertices();

    Set<String> getWhitelisted();

    HashMap<String,FlagSetting> getFlags();

    default FlagSetting getFlagSetting(RegionFlag flag){
        return getFlags().getOrDefault(flag.getName(), FlagSetting.NOT_SET);
    }
}
