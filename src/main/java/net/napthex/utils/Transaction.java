package net.napthex.utils;

import java.util.Date;

public class Transaction {
    private final String playerName;
    private final int amount;
    private final Date date;

    public Transaction(String playerName, int amount, Date date) {
        this.playerName = playerName;
        this.amount = amount;
        this.date = date;
    }

    public String getPlayerName() { return playerName; }
    public int getAmount() { return amount; }
    public Date getDate() { return date; }
}
