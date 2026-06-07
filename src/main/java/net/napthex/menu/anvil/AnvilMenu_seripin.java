package net.napthex.menu.anvil;

import net.napthex.NapTheX;
import net.napthex.api.Card;
import net.napthex.api.NapTheXAPI;
import net.napthex.config.Language;
import net.napthex.utils.Task;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class AnvilMenu_seripin implements Listener {

    public static void seri(Player player) {
        new AnvilGUI.Builder()
                .onComplete((target, text) -> {
                    Card card = NapTheXAPI.getPromptCard(target);
                    card.cardSerial(text);
                    NapTheXAPI.updatePromptCard(target, card);
                    pin(target);
                    return Collections.singletonList(AnvilGUI.ResponseAction.close());
                })
                .text("Nhập số Seri")
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Nhập số Seri")
                .plugin(NapTheX.getInstance())
                .open(player);
    }

    public static void pin(Player player) {
        new AnvilGUI.Builder()
                .onComplete((target, text) -> {
                    Card card = NapTheXAPI.getPromptCard(target);
                    card.cardPin(text);
                    NapTheXAPI.removePromptCard(target);
                    
                    target.sendMessage(Language.get("nap_the_dang_xu_ly"));
                    Task.async(() -> NapTheXAPI.processCard(target, card));
                    return Collections.singletonList(AnvilGUI.ResponseAction.close());
                })
                .text("Nhập mã thẻ (PIN)")
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Nhập mã thẻ (PIN)")
                .plugin(NapTheX.getInstance())
                .open(player);
    }
}
