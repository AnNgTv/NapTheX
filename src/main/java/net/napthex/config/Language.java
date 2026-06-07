package net.napthex.config;

import net.napthex.NapTheX;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Language {
    private static File f = null;
    private static FileConfiguration fc;

    public static String get(String path) {
        if (f == null) {
            saveDefault();
        }
        String s = "";
        try {
            s = fc.getString(path);
            s = ChatColor.translateAlternateColorCodes('&', s);
        } catch (Exception e) {
            Bukkit.getLogger().warning("NapTheX: Khong tim thay ngon ngu cho path: " + path);
        }
        return s;
    }

    public static void saveDefault() {
        f = new File(NapTheX.getInstance().getDataFolder(), "lang.yml");
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            NapTheX.getInstance().saveResource("lang.yml", false);
        }
        fc = YamlConfiguration.loadConfiguration(f);
    }

    public static void reload() {
        saveDefault();
    }
}
