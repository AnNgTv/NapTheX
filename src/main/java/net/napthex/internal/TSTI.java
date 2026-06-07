package net.napthex.internal;

import com.google.gson.JsonObject;
import net.napthex.NapTheX;
import net.napthex.api.Card;
import net.napthex.api.CardPrice;
import net.napthex.api.NapTheXAPI;
import net.napthex.config.Config;
import net.napthex.config.Language;
import net.napthex.utils.Debug;
import net.napthex.utils.Task;
import net.napthex.utils.Utils;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.HashMap;

public class TSTI {
    private static final HashMap<String, Long> trongHeThong = new HashMap<>();

    public static void processCard(Player player, Card card) {
        if (card.cardPrice() <= 0 || card.cardType().isEmpty() || card.SERIAL().isEmpty() || card.PIN().isEmpty()) {
            String errorMsg = "";
            if (card.cardPrice() <= 0) errorMsg += " (Price <= 0)";
            if (card.cardType().isEmpty()) errorMsg += " (Type empty)";
            if (card.SERIAL().isEmpty()) errorMsg += " (Serial empty)";
            if (card.PIN().isEmpty()) errorMsg += " (Pin empty)";

            player.sendMessage(Language.get("nap_the_that_bai") + errorMsg);
            NapTheXAPI.removePromptCard(player);
            return;
        }

        if (Config.debug()) {
            Debug.write("Gui the len API", card);
        }

        player.sendMessage(Language.get("nap_the_dang_xu_ly"));

        String apiKey = Config.APIKey();
        String apiSecret = Config.APISecret();
        
        JsonObject response = requestCardTransaction(apiKey, apiSecret, card.cardType(), card.cardPrice(), card.SERIAL(), card.PIN());

        if (Config.debug()) {
            Debug.write("API Response: " + (response != null ? response.toString() : "null"), card);
        }

        if (response == null) {
            player.sendMessage(Language.get("nap_the_that_bai") + " (Null response)");
            NapTheXAPI.removePromptCard(player);
            return;
        }

        String status = response.get("status").getAsString();
        if (status.equals("2")) {
            player.sendMessage(Language.get("nap_the_that_bai") + " " + response.get("msg").getAsString());
            NapTheXAPI.removePromptCard(player);
            return;
        }

        if (!status.equals("00")) {
            player.sendMessage(Language.get("nap_the_that_bai"));
            player.sendMessage("§c" + response.get("msg").getAsString());
        }

        if (!response.has("transaction_id")) {
            if (response.has("msg")) {
                player.sendMessage(Language.get("nap_the_that_bai") + " " + response.get("msg").getAsString());
            } else {
                player.sendMessage(Language.get("nap_the_that_bai") + " (No TransID)");
                Debug.write("No transaction_id in response: " + response.toString(), card);
            }
            NapTheXAPI.removePromptCard(player);
            return;
        }

        String transId = response.get("transaction_id").getAsString();
        card.transID(transId);

        if (trongHeThong.containsKey(transId)) {
            player.sendMessage(Language.get("nap_the_that_bai") + " (Duplicate TransID)");
            return;
        }

        String randomMD5 = response.has("randomMD5") ? response.get("randomMD5").getAsString() : Utils.randomMD5();
        trongHeThong.put(transId, System.currentTimeMillis());
        
        // Finalize transaction
        Task.sync(() -> {
            org.bukkit.Bukkit.getPluginManager().callEvent(new net.napthex.event.PlayerCardProcessEvent(
                    player, card.SERIAL(), card.PIN(), card.cardType(), card.cardPrice(), randomMD5, System.currentTimeMillis()
            ));
        });

        NapTheXAPI.removePromptCard(player);
        NapTheX.getInstance().getQueueCardStatus().addQueue(card);
    }

    private static JsonObject requestCardTransaction(String apiKey, String apiSecret, String type, int amount, String serial, String pin) {
        String urlTemplate = "http://vnpt.thesieutoc.net/API/transaction?APIkey={0}&APIsecret={1}&mathe={2}&seri={3}&type={4}&menhgia={5}";
        String url = MessageFormat.format(urlTemplate, apiKey, apiSecret, pin, serial, type, String.valueOf(CardPrice.getPrice(amount).getId()));
        
        String randomMD5 = Utils.randomMD5();
        if (Config.customURL()) {
            String customUrl = Config.customURLValue();
            url = MessageFormat.format(customUrl, apiKey, apiSecret, pin, serial, type, String.valueOf(amount), randomMD5);
        }
        
        url = url.replace("\"", "");
        JsonObject json = Utils.fetchJsonResponse(url);
        if (json != null) {
            json.addProperty("randomMD5", randomMD5);
        }
        return json;
    }
}
