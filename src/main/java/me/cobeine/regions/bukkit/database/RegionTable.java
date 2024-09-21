package me.cobeine.regions.bukkit.database;

import me.cobeine.sqlava.connection.database.table.Table;
import me.cobeine.sqlava.connection.database.table.column.Column;
import me.cobeine.sqlava.connection.database.table.column.ColumnSettings;
import me.cobeine.sqlava.connection.database.table.column.ColumnType;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class RegionTable extends Table {

    public RegionTable(String name) {
        super(name);
        addColumns(
                Column.of("id", ColumnType.VARCHAR).size(128).settings(ColumnSettings.UNIQUE),
                Column.of("name", ColumnType.TEXT),
                Column.of("pos1", ColumnType.TEXT),
                Column.of("pos2", ColumnType.TEXT),
                Column.of("flags", ColumnType.LONGTEXT),
                Column.of("whitelisted", ColumnType.LONGTEXT)

        );
        setPrimaryKey("id");
    }
}
