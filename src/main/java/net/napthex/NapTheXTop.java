package net.napthex;

import com.google.gson.JsonObject;
import net.napthex.config.Config;
import net.napthex.utils.Task;
import net.napthex.utils.Transaction;
import net.napthex.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class NapTheXTop {
    public List<Transaction> allTimeTrans = new LinkedList<>();
    public HashMap<String, JsonObject> cache = new HashMap<>();

    public NapTheXTop() {
        Task.async(() -> updatePlaceholder(), 0, Config.placeholderUpdate() * 20);
    }

    public int cacheValue(String key) {
        JsonObject json = cache.getOrDefault(key, new JsonObject());
        return json.has("value") ? json.get("value").getAsInt() : 0;
    }

    public void updatePlaceholder() {
        allTimeTrans.clear();
        cache.clear();
        allTimeTrans = loadTransactions();
        
        Date now = new Date();
        List<Transaction> daily = TransactionManager.forDaily(allTimeTrans, now);
        List<Transaction> weekly = TransactionManager.forWeekly(allTimeTrans, now);
        List<Transaction> monthly = TransactionManager.forMonthly(allTimeTrans, now);
        List<Transaction> yearly = TransactionManager.forYear(allTimeTrans, now);

        total(allTimeTrans, "total_alltime");
        total(daily, "total_daily");
        total(weekly, "total_weekly");
        total(monthly, "total_monthly");
        total(yearly, "total_year");

        top(allTimeTrans, "top_alltime");
        top(daily, "top_daily");
        top(weekly, "top_weekly");
        top(monthly, "top_monthly");
        top(yearly, "top_year");
    }

    private List<Transaction> loadTransactions() {
        if (NapTheX.getDatabase() != null) {
            return NapTheX.getDatabase().transactions();
        }
        return new LinkedList<>();
    }

    public void total(List<Transaction> list, String key) {
        int total = 0;
        for (Transaction t : list) {
            total += t.getAmount();
        }
        JsonObject json = new JsonObject();
        json.addProperty("value", total);
        cache.put(key, json);
    }

    public void top(List<Transaction> list, String key) {
        Map<String, Integer> map = new HashMap<>();
        for (Transaction t : list) {
            map.put(t.getPlayerName(), map.getOrDefault(t.getPlayerName(), 0) + t.getAmount());
        }
        LinkedHashMap<Integer, String> sorted = Utils.sortByComparator(map, false, 10);
        for (Map.Entry<Integer, String> entry : sorted.entrySet()) {
            JsonObject json = new JsonObject();
            json.addProperty("name", entry.getValue());
            json.addProperty("value", map.get(entry.getValue()));
            cache.put(key + "_" + entry.getKey(), json);
        }
    }
}
