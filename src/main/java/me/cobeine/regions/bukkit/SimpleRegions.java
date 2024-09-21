package me.cobeine.regions.bukkit;

import io.github.mqzen.menus.Lotus;
import lombok.Getter;
import me.cobeine.regions.bukkit.commands.RegionsCommand;
import me.cobeine.regions.bukkit.database.DatabaseManager;
import me.cobeine.regions.bukkit.listener.RegionListener;
import me.cobeine.regions.bukkit.region.RegionManager;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import me.cobeine.sqlava.connection.auth.BasicMySQLCredentials;
import me.cobeine.sqlava.connection.auth.CredentialsRecord;
import me.cobeine.sqlava.connection.util.JdbcUrlBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class SimpleRegions extends JavaPlugin {

    private static @Getter SimpleRegions instance;
    private DatabaseManager databaseManager;
    private @Getter RegionManager regionManager;
    private @Getter Lotus lotus;
    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        regionManager = new RegionManager();
        databaseManager = new DatabaseManager(CredentialsRecord.builder()
                .add(BasicMySQLCredentials.USERNAME, getConfig().getString("mysql.username"))
                .add(BasicMySQLCredentials.PASSWORD, getConfig().getString("mysql.password"))
                .add(BasicMySQLCredentials.PORT, getConfig().getInt("mysql.port"))
                .add(BasicMySQLCredentials.DATABASE, getConfig().getString("mysql.database"))
                .add(BasicMySQLCredentials.POOL_SIZE, getConfig().getInt("mysql.poolSize"))
                .add(BasicMySQLCredentials.JDBC_URL, JdbcUrlBuilder.newBuilder()
                        .database(getConfig().getString("mysql.database"))
                        .port(getConfig().getInt("mysql.port"))
                        .host(getConfig().getString("mysql.host"))
                        .setAuto_reconnect(true).build())
                .build());
        databaseManager.loadRegions(regionManager);
        Bukkit.getPluginManager().registerEvents(new RegionListener(), this);
        this.lotus = Lotus.load(this);
        new RegionsCommand(this);
    }

    @Override
    public void onDisable() {
        try {
            databaseManager.saveRegions(regionManager);
        } catch (Exception ignored) {

        }
        instance = null;
    }
}
