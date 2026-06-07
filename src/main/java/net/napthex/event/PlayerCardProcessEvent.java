package net.napthex.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCardProcessEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String serial;
    private final String pin;
    private final String cardType;
    private final int cardPrice;
    private final String randomMD5;
    private final long timestamp;

    public PlayerCardProcessEvent(Player player, String serial, String pin, String cardType, int cardPrice, String randomMD5, long timestamp) {
        this.player = player;
        this.serial = serial;
        this.pin = pin;
        this.cardType = cardType;
        this.cardPrice = cardPrice;
        this.randomMD5 = randomMD5;
        this.timestamp = timestamp;
    }

    public Player getPlayer() {
        return player;
    }

    public String getSerial() {
        return serial;
    }

    public String getPin() {
        return pin;
    }

    public String getCardType() {
        return cardType;
    }

    public int getCardPrice() {
        return cardPrice;
    }

    public String getRandomMD5() {
        return randomMD5;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
