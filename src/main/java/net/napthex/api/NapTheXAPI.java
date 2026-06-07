package net.napthex.api;

import net.napthex.internal.TSTI;
import org.bukkit.entity.Player;
import java.util.HashMap;

public class NapTheXAPI {
    private static final HashMap<String, Card> promptCard = new HashMap<>();

    public static void processCard(Player player, Card card) {
        TSTI.processCard(player, card);
    }

    public static Card getPromptCard(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!promptCard.containsKey(uuid)) {
            promptCard.put(uuid, new Card().player(player.getName()));
        }
        return promptCard.get(uuid);
    }

    public static void updatePromptCard(Player player, Card card) {
        promptCard.put(player.getUniqueId().toString(), card);
    }

    public static void removePromptCard(Player player) {
        promptCard.remove(player.getUniqueId().toString());
    }
}
