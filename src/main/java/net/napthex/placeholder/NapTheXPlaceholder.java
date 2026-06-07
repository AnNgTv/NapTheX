package net.napthex.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.napthex.NapTheX;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NapTheXPlaceholder extends PlaceholderExpansion {
    private final NapTheX plugin;

    public NapTheXPlaceholder(NapTheX plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return "DuongZX";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ntx";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (plugin.NAPTHEXTOP == null) return "0";

        if (identifier.startsWith("total_")) {
            return String.valueOf(plugin.NAPTHEXTOP.cacheValue(identifier));
        }

        if (identifier.startsWith("top_")) {
            // identifier format: top_alltime_name_1, top_alltime_value_1
            String[] split = identifier.split("_");
            if (split.length >= 4) {
                String type = split[0] + "_" + split[1]; // top_alltime
                String field = split[2]; // name or value
                String rank = split[3]; // 1
                
                String key = type + "_" + rank;
                if (plugin.NAPTHEXTOP.cache.containsKey(key)) {
                    return plugin.NAPTHEXTOP.cache.get(key).get(field).getAsString();
                }
            }
        }

        return null;
    }
}
