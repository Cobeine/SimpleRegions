package me.cobeine.regions.bukkit.region.serializers;

import it.unimi.dsi.fastutil.Hash;
import me.cobeine.regions.api.flags.FlagSetting;

import java.util.HashMap;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class FlagsSerializer {

    public static String serialize(HashMap<String, FlagSetting> map) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String s : map.keySet()) {
            builder.append(s).append(":").append(map.get(s).name()).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static HashMap<String, FlagSetting> deSerialize(String value) {
        if (value.isEmpty()) {
            return new HashMap<>();
        }
        String[] vars = value.split(",");
        HashMap<String, FlagSetting> map = new HashMap<>();
        for (String s : vars) {
            String[] split = s.split(":");
            map.put(split[0], FlagSetting.valueOf(split[1]));
        }
        return map;

    }

}
