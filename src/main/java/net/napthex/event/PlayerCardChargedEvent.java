package net.napthex.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCardChargedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String cardType;
    private final int cardPrice;
    private final int totalCharged;

    public PlayerCardChargedEvent(Player player, String cardType, int cardPrice, int totalCharged) {
        this.player = player;
        this.cardType = cardType;
        this.cardPrice = cardPrice;
        this.totalCharged = totalCharged;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCardType() {
        return cardType;
    }

    public int getCardPrice() {
        return cardPrice;
    }

    public int getTotalCharged() {
        return totalCharged;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
