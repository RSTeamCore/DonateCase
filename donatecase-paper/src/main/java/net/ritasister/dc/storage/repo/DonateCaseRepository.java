package net.ritasister.dc.storage.repo;

import com.j256.ormlite.dao.Dao;
import net.ritasister.dc.storage.DatabaseConnection;
import net.ritasister.dc.storage.dao.DonateCaseEntity;
import org.jspecify.annotations.NonNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DonateCaseRepository {

    private final Dao<DonateCaseEntity, String> dao;

    public DonateCaseRepository(@NonNull DatabaseConnection dbConnection) {
        this.dao = dbConnection.getDonateCaseEntities();
    }

    public Optional<DonateCaseEntity> find(@NonNull String player, @NonNull String caseName) {
        try {
            String id = player.toLowerCase() + ":" + caseName;
            return Optional.ofNullable(dao.queryForId(id));
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<DonateCaseEntity> findAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void save(@NonNull DonateCaseEntity entity) {
        try {
            dao.createIfNotExists(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(@NonNull DonateCaseEntity entity) {
        try {
            dao.update(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NonNull DonateCaseEntity entity) {
        try {
            dao.delete(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
