package me.cobeine.regions.bukkit.database;

import lombok.Getter;
import me.cobeine.regions.bukkit.region.RegionManager;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import me.cobeine.regions.bukkit.region.serializers.FlagsSerializer;
import me.cobeine.regions.bukkit.region.serializers.LocationSerializer;
import me.cobeine.regions.bukkit.region.serializers.WhitelistSerializer;
import me.cobeine.sqlava.connection.auth.CredentialsRecord;
import me.cobeine.sqlava.connection.database.MySQLConnection;
import me.cobeine.sqlava.connection.database.query.PreparedQuery;
import me.cobeine.sqlava.connection.database.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */
@Getter
public class DatabaseManager {
    private MySQLConnection connection;
    private String tableName = "regions";
    public DatabaseManager(CredentialsRecord record) {
        this.connection = new MySQLConnection(record);
        connection.connect(queryHandler -> {
            if (queryHandler.getException().isPresent()) {
                queryHandler.getException().get().printStackTrace();;
                return;
            }
            connection.getTableCommands().createTable(new RegionTable(tableName));
        });
    }

    public void loadRegions(RegionManager manager) {
        getConnection().prepareStatement(Query.select(getTableName())).executeQueryAsync(queryHandler -> {
            if (queryHandler.getException().isPresent()) {
                queryHandler.getException().get().printStackTrace();;
                return;
            }
            queryHandler.getResult().ifPresent(resultSet -> {
                try {
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
                    e.printStackTrace();
                }
            });
        });
    }

    public void saveRegions(RegionManager regionManager) throws SQLException {
        List<BukkitRegion> regions = regionManager.getRegions();
        if (regions.isEmpty()) {
            return;
        }
        final BukkitRegion first = regions.get(0);
        regions.remove(0);
        String queryText = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE name = ?, pos1 = ?, pos2 = ?, flags = ?, whitelisted = ?";
        PreparedQuery query = getConnection().prepareStatement(queryText)
                .setParameter(1, first.getUuid().toString())
                .setParameter(2, first.getName())
                .setParameter(3, LocationSerializer.serialize(first.getVertices()[0]))
                .setParameter(4, LocationSerializer.serialize(first.getVertices()[1]))
                .setParameter(5, FlagsSerializer.serialize(first.getFlags()))
                .setParameter(6, WhitelistSerializer.serialize(first.getWhitelisted()))
                .setParameter(7, first.getName())
                .setParameter(8, LocationSerializer.serialize(first.getVertices()[0]))
                .setParameter(9, LocationSerializer.serialize(first.getVertices()[1]))
                .setParameter(10, FlagsSerializer.serialize(first.getFlags()))
                .setParameter(11, WhitelistSerializer.serialize(first.getWhitelisted()));

        if (regions.isEmpty()) {
            query.executeUpdate();
            getConnection().closeConnection();
            return;
        }
        for (BukkitRegion region : regions) {
            Query batchQuery = Query.insert(getTableName())
                    .value("id", region.getUuid().toString())
                    .value("name", region.getName())
                    .value("pos1", LocationSerializer.serialize(region.getVertices()[0]))
                    .value("pos2", LocationSerializer.serialize(region.getVertices()[1]))
                    .value("flags", FlagsSerializer.serialize(region.getFlags()))
                    .value("whitelisted", WhitelistSerializer.serialize(region.getWhitelisted()))
                    .onDuplicateKeyUpdate();
            query.addBatch(batchQuery.build());

        }
        query.executeBatch();
    }
}
