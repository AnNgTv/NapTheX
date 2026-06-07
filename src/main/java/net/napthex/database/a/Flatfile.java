package net.napthex.database.a;

import net.napthex.NapTheX;
import net.napthex.api.Card;
import net.napthex.database.DatabaseType;
import net.napthex.utils.Transaction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Flatfile implements DatabaseType {
    private final File file;
    private FileConfiguration config;

    public Flatfile() {
        this.file = new File(NapTheX.getInstance().getDataFolder(), "naptheomoc.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void writeLog(Player player, Card card) {
        String path = "logs." + System.currentTimeMillis();
        config.set(path + ".player", player.getName());
        config.set(path + ".amount", card.cardPrice());
        config.set(path + ".type", card.cardType());
        config.set(path + ".serial", card.cardSerial());
        config.set(path + ".pin", card.cardPin());
        config.set(path + ".status", "SUCCESS");
        config.set(path + ".date", System.currentTimeMillis());
        save();
    }

    @Override
    public int getPlayerTotalCharged(Player player) {
        int total = 0;
        if (config.getConfigurationSection("logs") == null) return 0;
        for (String key : config.getConfigurationSection("logs").getKeys(false)) {
            if (config.getString("logs." + key + ".player").equalsIgnoreCase(player.getName())) {
                total += config.getInt("logs." + key + ".amount");
            }
        }
        return total;
    }

    @Override
    public List<Transaction> transactions() {
        List<Transaction> list = new ArrayList<>();
        if (config.getConfigurationSection("logs") == null) return list;
        for (String key : config.getConfigurationSection("logs").getKeys(false)) {
            String player = config.getString("logs." + key + ".player");
            int amount = config.getInt("logs." + key + ".amount");
            long date = config.getLong("logs." + key + ".date");
            list.add(new Transaction(player, amount, new Date(date)));
        }
        return list;
    }

    @Override
    public List<Transaction> transactions(Date start, Date end) {
        List<Transaction> list = new ArrayList<>();
        if (config.getConfigurationSection("logs") == null) return list;
        for (String key : config.getConfigurationSection("logs").getKeys(false)) {
            long dateLong = config.getLong("logs." + key + ".date");
            Date date = new Date(dateLong);
            if (date.after(start) && date.before(end)) {
                String player = config.getString("logs." + key + ".player");
                int amount = config.getInt("logs." + key + ".amount");
                list.add(new Transaction(player, amount, date));
            }
        }
        return list;
    }

    @Override
    public void disable() {
        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
