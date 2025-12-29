package net.ritasister.dc.storage.repo;

import com.j256.ormlite.stmt.QueryBuilder;
import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.storage.DatabaseConnection;
import net.ritasister.dc.storage.dao.MinecraftAccounts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Optional;

public class MinecraftAccountRepository {

    private final DonateCasePaperPlugin plugin;
    private final DatabaseConnection dbConnection;

    public MinecraftAccountRepository(DonateCasePaperPlugin plugin, DatabaseConnection dbConnection) {
        this.plugin = plugin;
        this.dbConnection = dbConnection;
    }

    public Optional<String> getMinecraftNameByMinecraftUniqueId(@NotNull String uniqueId) {
        try {
            final QueryBuilder<MinecraftAccounts, Integer> queryBuilder = dbConnection.getMinecraftAccounts().queryBuilder();
            queryBuilder.where().eq("uuid", uniqueId);

            final MinecraftAccounts minecraftAccount = queryBuilder.queryForFirst();

            return Optional.ofNullable(minecraftAccount).map(MinecraftAccounts::getNickname);
        } catch (SQLException e) {
            plugin.getLogger().severe("Database error while fetching Minecraft name for Minecraft UUID " + uniqueId, e);
            return Optional.empty();
        }
    }

    public boolean isUniqueIdExists(@NotNull String minecraftUniqueId) {
        try {
            return dbConnection.getMinecraftAccounts()
                    .queryBuilder()
                    .where()
                    .eq("uuid", minecraftUniqueId)
                    .countOf() > 0;
        } catch (SQLException e) {
            plugin.getLogger().severe("Database error while checking minecraftUniqueId: " + minecraftUniqueId, e);
            return false;
        }
    }

    @Nullable MinecraftAccounts getMinecraftAccount(String uniqueId) {
        try {
            return dbConnection.getMinecraftAccounts()
                    .queryBuilder()
                    .where()
                    .eq("uuid", uniqueId)
                    .queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private MinecraftAccounts getCurrentMinecraftAccount(int minecraftAccountId) {
        try {
            final QueryBuilder<MinecraftAccounts, Integer> queryBuilder = dbConnection.getMinecraftAccounts().queryBuilder();
            queryBuilder.where().eq("id", minecraftAccountId);
            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error retrieving Minecraft account for id " + minecraftAccountId + ": " + e.getMessage(), e);
            return null;
        }
    }
}
