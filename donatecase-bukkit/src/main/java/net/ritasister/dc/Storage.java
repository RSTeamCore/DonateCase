package net.ritasister.dc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Storage implements StorageDataSource {

    private final DonateCase donateCase;

    private HikariDataSource ds;

    public Storage(DonateCase donateCase) {
        this.donateCase =donateCase;
        this.connect();
        this.initialize();
    }

    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl("jdbc:mariadb://"
                + DonateCase.config.getString("DonatCase.MySql.Host") + ":"
                + "3306/donateCase");
        config.setUsername(DonateCase.config.getString("DonatCase.MySql.User"));
        config.setPassword(DonateCase.config.getString("DonatCase.MySql.Password"));

        // Pool settings
        config.setMaximumPoolSize(10);
        config.setMaxLifetime(1800 * 1000L);
        config.setConnectionTimeout(60000);

        config.setPoolName("MariaDBPool");

        // Encoding
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("encoding", "UTF-8");
        config.addDataSourceProperty("useUnicode", "true");

        // Random stuff
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("jdbcCompliantTruncation", "false");

        // Caching
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "275");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        if (!this.ds.getConnection().isValid(3)) {
            DonateCase.instance.getLogger().severe("Trying to reconnect to database!");
            this.connect();
        }
        return this.ds.getConnection();
    }

    public void initialize() {
        PreparedStatement pst = null;
        try(Connection conn = Storage.this.getConnection()) {
            pst = conn.prepareStatement("CREATE TABLE `donate_cases` (`player` varchar(16) NOT NULL, `case_name` varchar(32) NOT NULL, `keys_count` int(16) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1");
            pst.execute();
            pst.close();
        }catch(SQLException ex){
            donateCase.getLogger().severe("Failed connect to database!");
        }finally{
            this.close(pst);
        }
    }

    @Override
    public boolean load() {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try(Connection conn = this.getConnection()) {
            pst = conn.prepareStatement("SELECT * FROM donate_cases;");
            rs = pst.executeQuery();
            while(rs.next()) {
                String nickName = rs.getString("nickname");
                StorageDataBase dataBase = new StorageDataBase(
                        nickName,
                        rs.getString("case_name"),
                        rs.getInt("keys_count"));
                donateCase.dbLogs.put(nickName, dataBase);
            }
            return true;
        }catch(SQLException ex){
            donateCase.getLogger().severe("Failed connect to database!");
            ex.printStackTrace();
        }finally{
            this.close(rs);
            this.close(pst);
        }return false;
    }

    public void loadAsync() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(donateCase, () -> {
                    ConcurrentHashMap<String, StorageDataBase> tempDataBase = new ConcurrentHashMap<>();
                    PreparedStatement pst = null;
                    ResultSet rs = null;
                    try (Connection conn = Storage.this.getConnection()) {
                        pst = conn.prepareStatement("SELECT * FROM donate_cases;");
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            String nickName = rs.getString("nickname");
                            StorageDataBase dataBase = new StorageDataBase(
                                    nickName,
                                    rs.getString("case_name"),
                                    rs.getInt("keys_count"));
                            tempDataBase.put(nickName, dataBase);
                        }
                        donateCase.dbLogs = new ConcurrentHashMap<>(tempDataBase);
                    } catch (SQLException ex) {
                        donateCase.getLogger().severe("Failed load database in async!");
                        ex.printStackTrace();
                    } finally {
                        Storage.this.close(rs);
                        Storage.this.close(pst);
                    }
                }, 60 * 20L,
                60 * 20L);
    }

    @Override
    public void setKey(final String name, String player, final int keys) {
        PreparedStatement pst = null;
        try(Connection conn = Storage.this.getConnection()) {
            player = player.toLowerCase();
            if (!this.hasField("donate_cases", "player='" + player + "' AND case_name='" + name + "'")) {
                pst = conn.prepareStatement(DonateCase.t.rt("INSERT INTO `donate_cases` (`player`, `case_name`, `keys_count`) VALUES ('%player', '%case', '%keys')", "%player:" + player, "%keys:" + keys, "%case:" + name));
            } else {
                pst = conn.prepareStatement(DonateCase.t.rt("UPDATE `donate_cases` SET keys_count='%keys' WHERE player='%player' AND case_name='%case'", "%player:" + player, "%keys:" + keys, "%case:" + name));
            }
        } catch (SQLException ex) {
            donateCase.getLogger().severe("Failed connect to database!");
        } finally {
            this.close(pst);
        }
    }

    @Override
    public int getKey(final String name, String player) {
        PreparedStatement pst;
        try(Connection conn = Storage.this.getConnection()) {
            player = player.toLowerCase();
            pst = conn.prepareStatement(String.format("SELECT * FROM `donate_cases` WHERE `player`='%s' AND case_name='%s'", player, name));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(3);
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean hasField(final String table, final String t) {
        PreparedStatement pst;
        try(Connection conn = Storage.this.getConnection()) {
            pst = conn.prepareStatement(String.format("SELECT * FROM `%s` WHERE %s", table, t));
            ResultSet rs = pst.executeQuery();
            rs.next();
            rs.getString(1);
            return true;
        } catch (SQLException var4) {
            return false;
        }
    }

    @Override
    public boolean hasTable(final String table) {
        PreparedStatement pst;
        try(Connection conn = Storage.this.getConnection()) {
            pst = conn.prepareStatement(String.format("SELECT * FROM %s", table));
            ResultSet rs = pst.executeQuery();
            rs.next();
            return true;
        } catch (SQLException var3) {
            return false;
        }
    }

    public void close(final PreparedStatement pst) {
        try{
            if(pst != null) {
                pst.close();
            }
        } catch(SQLException ex) {
            DonateCase.instance.getLogger().severe("Failed to close PreparedStatement!");
        }
    }

    public void close(final ResultSet rs) {
        try{
            if (rs != null) {
                rs.close();
            }
        } catch(SQLException ex) {
            DonateCase.instance.getLogger().severe("Failed to close ResultSet!");
        }
    }

    public void reload() {
        if (ds != null) {
            ds.close();
        }
        connect();
        DonateCase.instance.getLogger().severe("Successfully reloaded!");
    }

    @Override
    public void close() {
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }
}
