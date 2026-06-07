package net.napthex.commands;

import net.napthex.NapTheX;
import net.napthex.api.Card;
import net.napthex.api.CardPrice;
import net.napthex.api.NapTheXAPI;
import net.napthex.config.Config;
import net.napthex.config.Language;
import net.napthex.menu.chat.ChatMenu;
import net.napthex.utils.Task;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;

public class NaptheCmd implements CommandExecutor, TabCompleter {
    private final HashMap<String, Long> fastcmd_cooldown = new HashMap<>();

    public NaptheCmd() {
        NapTheX.getInstance().getCommand("napthe").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCommand chi xai duoc trong game!");
            return true;
        }

        Player player = (Player) sender;

        // Chức năng nạp Bank
        if (args.length == 1 && args[0].equalsIgnoreCase("bank")) {
            player.sendMessage(Language.get("bank_prompt_amount"));
            ChatMenu.setPromptBank(player);
            return true;
        }

        // Fast command logic (/napthe type amount seri pin)
        if (args.length == 4) {
            if (!Config.fastCmd()) return true;

            long cooldown = fastcmd_cooldown.getOrDefault(player.getUniqueId().toString(), 0L);
            if (cooldown > System.currentTimeMillis()) {
                player.sendMessage(Language.get("nap_the_that_bai"));
                return true;
            }
            fastcmd_cooldown.put(player.getUniqueId().toString(), System.currentTimeMillis() + 3000L);

            try {
                String type = args[0];
                int amount = Integer.parseInt(args[1]);
                String seri = args[2];
                String pin = args[3];

                if (!Config.cardList().contains(type)) return true;
                if (CardPrice.getPrice(amount).getId() == -1) return true;

                Card card = new Card(player, type, amount, seri, pin);
                Task.async(() -> NapTheXAPI.processCard(player, card));
            } catch (Exception e) {
                player.sendMessage(Language.get("nap_the_that_bai"));
            }
            return true;
        }

        // GUI nạp thẻ bằng Chat (khi gõ /napthe không có đối số)
        if (args.length == 0) {
            TextComponent message = new TextComponent("");
            for (String type : Config.cardList()) {
                TextComponent cardType = new TextComponent("§b[" + type + "] ");
                cardType.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/napthe textchoosecard " + type));
                message.addExtra(cardType);
            }
            
            // Thêm nút Bank vào menu
            TextComponent bankBtn = new TextComponent("§a§l[NGÂN HÀNG/QR] ");
            bankBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/napthe bank"));
            
            player.sendMessage("");
            player.sendMessage(Language.get("chat_chon_loai_the"));
            player.spigot().sendMessage(bankBtn);
            player.spigot().sendMessage(message);
            return true;
        }

        // Logic xử lý các bước chọn thẻ/mệnh giá qua text
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("textchoosecard")) {
                String type = args[1];
                if (!Config.cardList().contains(type)) {
                    NapTheXAPI.removePromptCard(player);
                    player.sendMessage(Language.get("nap_the_that_bai"));
                    return true;
                }
                Card card = NapTheXAPI.getPromptCard(player);
                card.cardType(type);
                NapTheXAPI.updatePromptCard(player, card);

                TextComponent prices = new TextComponent("");
                int[] availablePrices = {10000, 20000, 30000, 50000, 100000, 200000, 500000, 1000000};
                DecimalFormat df = new DecimalFormat("#,###");

                for (int price : availablePrices) {
                    TextComponent pComp = new TextComponent("§e[" + df.format(price) + "đ] ");
                    pComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/napthe textchoosecardprice " + price));
                    prices.addExtra(pComp);
                }

                player.sendMessage(MessageFormat.format(Language.get("chat_da_chon_loai_the"), type));
                player.sendMessage("");
                player.sendMessage(Language.get("chat_chon_menh_gia"));
                player.spigot().sendMessage(prices);
            }
            else if (args[0].equalsIgnoreCase("textchoosecardprice")) {
                try {
                    int price = Integer.parseInt(args[1]);
                    if (CardPrice.getPrice(price).getId() == -1) {
                        NapTheXAPI.removePromptCard(player);
                        player.sendMessage(Language.get("nap_the_that_bai"));
                        return true;
                    }
                    Card card = NapTheXAPI.getPromptCard(player);
                    card.cardPrice(price);
                    NapTheXAPI.updatePromptCard(player, card);
                    ChatMenu.prompt(player);
                } catch (Exception e) {
                    NapTheXAPI.removePromptCard(player);
                    player.sendMessage(Language.get("nap_the_that_bai"));
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> sub = new ArrayList<>(Config.cardList());
            sub.add("bank");
            return sub;
        }
        return Collections.emptyList();
    }
}
