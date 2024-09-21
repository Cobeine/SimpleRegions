package me.cobeine.regions.bukkit.region.serializers;

import java.util.*;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class WhitelistSerializer {

    public static String serialize(Set<String> user) {
        if (user.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String name : user) {
            builder.append(name).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();

    }

    public static Set<String> deSerialize(String value) {
        String[] vars = value.split(",");
        return new HashSet<>(Arrays.asList(vars));


    }

}
