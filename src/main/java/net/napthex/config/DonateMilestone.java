package net.napthex.config;

import net.napthex.NapTheX;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DonateMilestone {
    private static File f;
    private static FileConfiguration fc;

    public static void saveDefault() {
        f = new File(NapTheX.getInstance().getDataFolder(), "milestone.yml");
        if (!f.exists()) {
            NapTheX.getInstance().saveResource("milestone.yml", false);
        }
        reload();
    }

    public static void reload() {
        if (f == null) {
            f = new File(NapTheX.getInstance().getDataFolder(), "milestone.yml");
        }
        fc = YamlConfiguration.loadConfiguration(f);
    }

    public static boolean isEnable() {
        return fc.getBoolean("enable", false);
    }

    public static List<String> getCommands(int milestone) {
        return fc.getStringList("milestones." + milestone);
    }

    public static HashSet<Integer> getMilestones() {
        HashSet<Integer> milestones = new HashSet<>();
        if (fc.getConfigurationSection("milestones") == null) return milestones;
        for (String key : fc.getConfigurationSection("milestones").getKeys(false)) {
            try {
                milestones.add(Integer.parseInt(key));
            } catch (NumberFormatException ignored) {}
        }
        return milestones;
    }
}
