package net.napthex.database;

import net.napthex.api.Card;
import net.napthex.utils.Transaction;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;

public interface DatabaseType {
    void writeLog(Player player, Card card);
    int getPlayerTotalCharged(Player player);
    List<Transaction> transactions();
    List<Transaction> transactions(Date start, Date end);
    void disable();
}
