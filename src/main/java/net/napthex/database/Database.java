package net.napthex.database;

import net.napthex.api.Card;
import net.napthex.utils.Transaction;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;

public class Database {
    private final DatabaseType database;

    public Database(DatabaseType database) {
        this.database = database;
    }

    public void writeLog(Player player, Card card) {
        database.writeLog(player, card);
    }

    public int getPlayerTotalCharged(Player player) {
        return database.getPlayerTotalCharged(player);
    }

    public List<Transaction> transactions() {
        return database.transactions();
    }

    public List<Transaction> transactions(Date start, Date end) {
        return database.transactions(start, end);
    }

    public void disable() {
        database.disable();
    }
}
