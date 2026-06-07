package net.napthex.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.napthex.NapTheX;
import net.napthex.api.Card;
import net.napthex.config.Config;
import net.napthex.config.Language;
import net.napthex.utils.Debug;
import net.napthex.utils.Task;
import net.napthex.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueCardStatus {
    private final LinkedBlockingQueue<Card> queue = new LinkedBlockingQueue<>();
    private final Set<String> uniqueCards = ConcurrentHashMap.newKeySet();
    private boolean isInit = false;

    public void addQueue(Card card) {
        if (!Config.callBack()) return;
        if (Config.customURL()) return;
        
        if (uniqueCards.add(card.internalID())) {
            card.setProcessCooldown(System.currentTimeMillis() + 10000L);
            queue.offer(card);
            if (Config.debug()) Debug.write("Add card to queue waiting for results", card);
        } else {
            if (Config.debug()) Debug.write("Card already in queue", card);
        }
    }

    public void init() {
        if (isInit) return;
        if (!Config.callBack()) return;
        if (Config.customURL()) return;
        
        isInit = true;
        Task.async(() -> {
            while (isInit) {
                try {
                    Card card = queue.poll();
                    if (card == null) {
                        Thread.sleep(1000);
                        continue;
                    }
                    
                    Player player = Bukkit.getPlayer(card.player());
                    if (card.getProcessCooldown() > System.currentTimeMillis()) {
                        queue.offer(card);
                        Thread.sleep(1000);
                        continue;
                    }

                    card.setProcessCooldown(System.currentTimeMillis() + 10000L);
                    if (Config.debug()) Debug.write("Checking card status", card);
                    
                    JsonObject response = fetchCardStatus(Config.APIKey(), Config.APISecret(), card.transID());
                    if (Config.debug()) Debug.write("Status Response: " + (response != null ? response.toString() : "null"), card);
                    
                    if (response == null) {
                        queue.offer(card);
                        continue;
                    }
                    
                    String status = response.get("status").getAsString();
                    if (status.equals("-9")) {
                        if (card.retry() > 30) {
                            if (Config.debug()) Debug.write("Card timed out (retry > 30)", card);
                            uniqueCards.remove(card.internalID());
                        } else {
                            card.retry(card.retry() + 1);
                            queue.offer(card);
                        }
                        continue;
                    }
                    
                    if (status.equals("-10")) {
                        if (player != null) player.sendMessage(Language.get("nap_the_that_bai"));
                        card.callbackMessage("that bai");
                        uniqueCards.remove(card.internalID());
                        continue;
                    }

                    if (status.equals("00")) {
                        if (card.cardPrice() == 0) {
                            Bukkit.getLogger().warning("[NapTheX] Card approved but missing value!");
                            continue;
                        }
                        if (player == null || !player.isOnline()) {
                            NapTheX.getInstance().getOffline().add(card);
                            if (Config.debug()) Debug.write("Success but player offline", card);
                            uniqueCards.remove(card.internalID());
                            continue;
                        }
                        
                        player.sendMessage(MessageFormat.format(Language.get("nap_the_thanh_cong"), card.cardPrice()));
                        chargeSuccess(player, card);
                        uniqueCards.remove(card.internalID());
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 20);
    }

    public void chargeSuccess(Player player, Card card) {
        List<String> commands = NapTheX.getInstance().getConfig().getStringList("card.rewards." + card.cardPrice());
        if (commands.isEmpty()) {
            Bukkit.getLogger().warning("No rewards configured for price: " + card.cardPrice());
        } else {
            Task.sync(() -> {
                for (String cmd : commands) {
                    Utils.dispatchCommand(player, cmd);
                }
            });
        }
        
        if (NapTheX.getDatabase() != null) {
            NapTheX.getDatabase().writeLog(player, card);
            int totalCharged = NapTheX.getDatabase().getPlayerTotalCharged(player);
            Task.sync(() -> {
                Bukkit.getPluginManager().callEvent(new net.napthex.event.PlayerCardChargedEvent(player, card.cardType(), card.cardPrice(), totalCharged));
            });
        }
        
        card.callbackMessage("thanh cong");
    }

    private JsonObject fetchCardStatus(String apiKey, String apiSecret, String transId) {
        String url = MessageFormat.format("https://thesieutoc.net/API/get_status_card.php?APIkey={0}&APIsecret={1}&transaction_id={2}", apiKey, apiSecret, transId);
        return Utils.fetchJsonResponse(url);
    }
}
