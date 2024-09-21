package me.cobeine.regions.bukkit.region;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@Getter
@Setter
public class PlayerWand {
    private Location pos1, pos2;

    public static ItemStack wand() {
        ItemStack wand = new ItemStack(Material.WOODEN_AXE);
        wand.editMeta(meta -> {
            meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize("&aRegion wand"));
            meta.lore(List.of(
                    LegacyComponentSerializer.legacyAmpersand()
                            .deserialize("&7Left click to set pos1, right click to set pos2")));
        });
        return wand;
    }

}
