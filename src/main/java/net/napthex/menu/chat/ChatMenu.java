package net.napthex.menu.chat;

import net.napthex.NapTheX;
import net.napthex.config.Language;
import net.napthex.api.NapTheXAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class ChatMenu implements Listener {
    private static final HashMap<String, String> prompt = new HashMap<>();
    private static final HashMap<UUID, Boolean> promptBank = new HashMap<>();

    public static void prompt(Player player) {
        prompt.put(player.getUniqueId().toString(), "seri");
        player.sendMessage(Language.get("chat_nhap_seri"));
    }

    public static void setPromptBank(Player player) {
        promptBank.put(player.getUniqueId(), true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.equalsIgnoreCase("huy")) {
            if (prompt.containsKey(player.getUniqueId().toString()) || promptBank.containsKey(player.getUniqueId())) {
                prompt.remove(player.getUniqueId().toString());
                promptBank.remove(player.getUniqueId());
                player.sendMessage(Language.get("nap_the_huy"));
                event.setCancelled(true);
            }
            return;
        }

        // Xử lý nhập số tiền nạp Bank
        if (promptBank.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            promptBank.remove(player.getUniqueId());
            try {
                int amount = Integer.parseInt(message);
                if (amount <= 0) throw new NumberValueException();
                
                showBankInfo(player, amount);
            } catch (Exception e) {
                player.sendMessage(Language.get("bank_invalid_amount"));
            }
            return;
        }

        // Logic xử lý nạp thẻ qua chat
        if (prompt.containsKey(player.getUniqueId().toString())) {
            event.setCancelled(true);
            String step = prompt.get(player.getUniqueId().toString());
            net.napthex.api.Card card = NapTheXAPI.getPromptCard(player);

            if (step.equalsIgnoreCase("seri")) {
                card.cardSerial(message);
                prompt.put(player.getUniqueId().toString(), "pin");
                player.sendMessage(Language.get("chat_nhap_pin"));
            } else if (step.equalsIgnoreCase("pin")) {
                card.cardPin(message);
                prompt.remove(player.getUniqueId().toString());
                NapTheXAPI.removePromptCard(player);
                
                player.sendMessage(Language.get("nap_the_dang_xu_ly"));
                net.napthex.utils.Task.async(() -> NapTheXAPI.processCard(player, card));
            }
            return;
        }
    }

    private void showBankInfo(Player player, int amount) {
        FileConfiguration config = NapTheX.getInstance().getConfig();
        String bank = config.getString("bank-transfer.bank-name");
        String account = config.getString("bank-transfer.account-number");
        String holder = config.getString("bank-transfer.account-holder");
        String content = config.getString("bank-transfer.content-prefix") + " " + player.getName();
        
        String qrUrl = config.getString("bank-transfer.qr-template")
                .replace("{bank}", bank)
                .replace("{account}", account)
                .replace("{amount}", String.valueOf(amount))
                .replace("{content}", content)
                .replace("{accountHolder}", holder);

        for (String msg : NapTheX.getInstance().getConfig().getStringList("bank_info")) {
            player.sendMessage(msg.replace("{bank}", bank)
                    .replace("{account}", account)
                    .replace("{holder}", holder)
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{content}", content)
                    .replace("{qr_url}", qrUrl));
        }
    }
    
    private static class NumberValueException extends Exception {}
}
