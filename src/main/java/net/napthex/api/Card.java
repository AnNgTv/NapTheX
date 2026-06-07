package net.napthex.api;

import net.napthex.utils.FNum;
import org.bukkit.entity.Player;

public class Card {
    private String player;
    private String internalID;
    private String transID = "0";
    private String cardType = "";
    private int cardPrice;
    private String SERIAL = "";
    private String PIN = "";
    private int retry = 0;
    private String callbackMessage = "";
    private long processCooldown = 0L;

    public Card() {
        this.internalID = String.valueOf(FNum.randomInt(100000000, 999999999));
    }

    public Card(Player player, String cardType, int cardPrice, String SERIAL, String PIN) {
        this();
        this.player = player.getName();
        this.cardType = cardType;
        this.cardPrice = cardPrice;
        this.SERIAL = SERIAL;
        this.PIN = PIN;
    }

    public String internalID() { return internalID; }
    public String player() { return player; }
    public String transID() { return transID; }
    public String cardType() { return cardType; }
    public int cardPrice() { return cardPrice; }
    public String SERIAL() { return SERIAL; }
    public String PIN() { return PIN; }
    public int retry() { return retry; }
    public String callbackMessage() { return callbackMessage; }

    public Card player(String player) { this.player = player; return this; }
    public Card transID(String transID) { this.transID = transID; return this; }
    public Card cardType(String cardType) { this.cardType = cardType; return this; }
    public Card cardPrice(int cardPrice) { this.cardPrice = cardPrice; return this; }
    public Card SERIAL(String SERIAL) { this.SERIAL = SERIAL; return this; }
    public Card PIN(String PIN) { this.PIN = PIN; return this; }
    public Card retry(int retry) { this.retry = retry; return this; }
    public Card callbackMessage(String callbackMessage) { this.callbackMessage = callbackMessage; return this; }

    public long getProcessCooldown() { return processCooldown; }
    public Card setProcessCooldown(long processCooldown) { this.processCooldown = processCooldown; return this; }
}
