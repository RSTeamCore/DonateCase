package net.ritasister.dc.storage;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariDataSource;
import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.storage.dao.DonateCaseEntity;
import net.ritasister.dc.storage.dao.MinecraftAccounts;
import net.ritasister.dc.util.file.config.field.ConfigFields;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {

    private final DonateCasePaperPlugin plugin;
    private final String dataBaseUri;
    private HikariDataSource ds;

    //Minecraft related DAOs
    private Dao<MinecraftAccounts, Integer> minecraftAccounts;
    private Dao<DonateCaseEntity, String> donateCaseEntities;

    public DatabaseConnection(@NotNull DonateCasePaperPlugin plugin) {
        this.plugin = plugin;
        dataBaseUri = ConfigFields.DATA_SOURCE_URL.asString(plugin);
        this.connect();
        this.initialize();
    }

    private void connect() {
        ds = new HikariDataSource();
        ds.setDriverClassName(ConfigFields.DATA_SOURCE_DRIVER.asString(plugin));
        ds.setJdbcUrl(dataBaseUri);
        ds.setUsername(ConfigFields.DATA_SOURCE_USER.asString(plugin));
        ds.setPassword(ConfigFields.DATA_SOURCE_PASSWORD.asString(plugin));

        // Pool settings
        ds.setMaximumPoolSize(ConfigFields.DATA_SOURCE_MAX_POOL_SIZE.asInt(plugin));
        ds.setMaxLifetime(ConfigFields.DATA_SOURCE_MAX_LIFE_TIME.asInt(plugin));
        ds.setConnectionTimeout(ConfigFields.DATA_SOURCE_CONNECTION_TIMEOUT.asInt(plugin));

        ds.setPoolName("TailWindSea-Bot-Pool");

        // Encoding
        ds.addDataSourceProperty("characterEncoding", "utf8");
        ds.addDataSourceProperty("encoding", "UTF-8");
        ds.addDataSourceProperty("useUnicode", "true");


        // Random stuff
        ds.addDataSourceProperty("rewriteBatchedStatements", "true");
        ds.addDataSourceProperty("jdbcCompliantTruncation", "false");

        // Caching
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "275");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    private void initialize() {
        try {
            final ConnectionSource connectionSource = new DataSourceConnectionSource(ds, ds.getJdbcUrl());

            TableUtils.createTableIfNotExists(connectionSource, MinecraftAccounts.class);
            TableUtils.createTableIfNotExists(connectionSource, DonateCaseEntity.class);

            minecraftAccounts = DaoManager.createDao(connectionSource, MinecraftAccounts.class);
            donateCaseEntities = DaoManager.createDao(connectionSource, DonateCaseEntity.class);

        } catch (SQLException exception) {
            plugin.getLogger().severe("Error during database initialization: " + exception);
            throw new RuntimeException("Failed to initialize the database", exception);
        }
    }

    public Dao<MinecraftAccounts, Integer> getMinecraftAccounts() {
        return minecraftAccounts;
    }

    public Dao<DonateCaseEntity, String> getDonateCaseEntities() {
        return donateCaseEntities;
    }

    public void close(final PreparedStatement pst) {
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException exception) {
            plugin.getLogger().severe("Failed to close prepared statement", exception);
        }
    }

    public void close(final ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException exception) {
            plugin.getLogger().severe("Failed to close result set", exception);
        }
    }

    public void close() {
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }

    public void reload() {
        this.connect();
        plugin.getLogger().info("Successfully reloaded!");
    }

}
