package net.napthex.database.a;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.napthex.config.Config;
import net.napthex.api.Card;
import net.napthex.database.DatabaseType;
import net.napthex.utils.Transaction;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQL implements DatabaseType {
    private HikariDataSource dataSource;

    public void init() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + Config.SQLHost() + ":" + Config.SQLPort() + "/" + Config.SQLDatabase());
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername(Config.SQLUser());
        hikariConfig.setPassword(Config.SQLPassword());
        hikariConfig.addProperty("cachePrepStmts", "true");
        hikariConfig.addProperty("prepStmtCacheSize", "250");
        hikariConfig.addProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(hikariConfig);

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS napthex_logs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player VARCHAR(64)," +
                    "amount INT," +
                    "type VARCHAR(32)," +
                    "serial VARCHAR(64)," +
                    "pin VARCHAR(64)," +
                    "status VARCHAR(32)," +
                    "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeLog(Player player, Card card) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO napthex_logs (player, amount, type, serial, pin, status) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, player.getName());
            pstmt.setInt(2, card.cardPrice());
            pstmt.setString(3, card.cardType());
            pstmt.setString(4, card.cardSerial());
            pstmt.setString(5, card.cardPin());
            pstmt.setString(6, "SUCCESS");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPlayerTotalCharged(Player player) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT SUM(amount) FROM napthex_logs WHERE player = ?")) {
            pstmt.setString(1, player.getName());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Transaction> transactions() {
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT player, amount, date FROM napthex_logs")) {
            while (rs.next()) {
                list.add(new Transaction(rs.getString("player"), rs.getInt("amount"), rs.getTimestamp("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Transaction> transactions(Date start, Date end) {
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT player, amount, date FROM napthex_logs WHERE date BETWEEN ? AND ?")) {
            pstmt.setTimestamp(1, new Timestamp(start.getTime()));
            pstmt.setTimestamp(2, new Timestamp(end.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Transaction(rs.getString("player"), rs.getInt("amount"), rs.getTimestamp("date")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void disable() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
