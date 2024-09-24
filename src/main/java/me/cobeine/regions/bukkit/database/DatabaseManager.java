package me.cobeine.regions.bukkit.database;

import lombok.Getter;
import me.cobeine.regions.bukkit.region.RegionManager;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import me.cobeine.regions.bukkit.region.serializers.FlagsSerializer;
import me.cobeine.regions.bukkit.region.serializers.LocationSerializer;
import me.cobeine.regions.bukkit.region.serializers.WhitelistSerializer;
import me.cobeine.sqlava.connection.auth.CredentialsRecord;
import me.cobeine.sqlava.connection.database.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@Getter
public class DatabaseManager {
    private final MySQLConnection connection;
    private final String tableName = "regions";
    public DatabaseManager(CredentialsRecord record) {
        this.connection = new MySQLConnection(record);
        connection.connect(queryHandler -> {
            if (queryHandler.getException().isPresent()) {
                connection.getLogger().severe(queryHandler.getException().get().getMessage());
                return;
            }
            try (PreparedStatement statement = getHikariConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS `"+ tableName +"` " +
                            "(`id` VARCHAR(128) UNIQUE , `name` TEXT, `pos1` TEXT, " +
                            "`pos2` TEXT, `flags` LONGTEXT, `whitelisted` LONGTEXT, PRIMARY KEY (`id`))")) {
                statement.executeUpdate();
            } catch (Exception e) {
                connection.getLogger().severe(e.getMessage());
            }
        });
    }

    public Connection getHikariConnection() {
        try {
            return connection.getConnection().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadRegions(RegionManager manager) {
        getConnection().getPool().submit(() -> {
            try (PreparedStatement statement = getHikariConnection().prepareStatement("SELECT * FROM " + getTableName())) {
               ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String pos1 = resultSet.getString("pos1");
                    String pos2 = resultSet.getString("pos2");
                    String flags = resultSet.getString("flags");
                    String whitelisted = resultSet.getString("whitelisted");
                    BukkitRegion region = BukkitRegion.deserialize(UUID.fromString(id),name, pos1, pos2, flags, whitelisted);
                    manager.addRegion(region);
                }
            } catch (Exception e) {
                connection.getLogger().severe(e.getMessage());
            }
        });
    }

    public void saveRegions(RegionManager regionManager) {
        List<BukkitRegion> regions = regionManager.getRegions();
        if (regions.isEmpty()) {
            return;
        }
        String queryText = "INSERT INTO " + tableName + " (id, name, pos1, pos2, flags, whitelisted) VALUES (?, ?, ?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE " +
                "name= VALUES(name), pos1 = VALUES(pos1), pos2 = VALUES(pos2), flags = VALUES(flags), whitelisted = VALUES(whitelisted)";
            try(PreparedStatement statement = getHikariConnection().prepareStatement(queryText)) {
                for (BukkitRegion region : regions) {
                    String uuid = region.getUuid().toString();
                    String name = region.getName();
                    String pos1 = LocationSerializer.serialize(region.getVertices()[0]);
                    String pos2 = LocationSerializer.serialize(region.getVertices()[1]);
                    String flags = FlagsSerializer.serialize(region.getFlags());
                    String whitelisted = WhitelistSerializer.serialize(region.getWhitelisted());
                    statement.setString(1, uuid);
                    statement.setString(2, name);
                    statement.setString(3, pos1);
                    statement.setString(4, pos2);
                    statement.setString(5, flags);
                    statement.setString(6, whitelisted);

                    statement.addBatch();
                }
                statement.executeBatch();
                getConnection().closeConnection();
            } catch (Exception e) {
               getConnection().getLogger().severe(e.getMessage());
            }
    }
}
