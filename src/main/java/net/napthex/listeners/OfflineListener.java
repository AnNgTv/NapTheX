package net.napthex.listeners;

import net.napthex.NapTheX;
import net.napthex.api.Card;
import net.napthex.internal.QueueCardStatus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OfflineListener implements Listener {
    private final File f;
    private FileConfiguration fc;

    public OfflineListener() {
        this.f = new File(NapTheX.getInstance().getDataFolder(), "offline.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.fc = YamlConfiguration.loadConfiguration(f);
    }

    public void add(Card card) {
        List<String> list = fc.getStringList(card.player());
        // Simple format: amount|type|serial|pin
        list.add(card.cardPrice() + "|" + card.cardType() + "|" + card.cardSerial() + "|" + card.cardPin());
        fc.set(card.player(), list);
        save();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (fc.contains(player.getName())) {
            List<String> list = fc.getStringList(player.getName());
            for (String s : list) {
                String[] split = s.split("\\|");
                if (split.length >= 4) {
                    int amount = Integer.parseInt(split[0]);
                    Card card = new Card()
                            .player(player.getName())
                            .cardPrice(amount)
                            .cardType(split[1])
                            .cardSerial(split[2])
                            .cardPin(split[3]);
                    
                    Bukkit.getScheduler().runTaskLater(NapTheX.getInstance(), () -> {
                        NapTheX.getInstance().getQueueCardStatus().chargeSuccess(player, card);
                    }, 20L);
                }
            }
            fc.set(player.getName(), null);
            save();
        }
    }

    private void save() {
        try {
            fc.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
