package net.napthex.config;

import net.napthex.NapTheX;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class Config {
    public static boolean debug() {
        if (!NapTheX.getInstance().getConfig().contains("debug")) {
            NapTheX.getInstance().getConfig().set("debug", false);
            NapTheX.getInstance().saveConfig();
        }
        return NapTheX.getInstance().getConfig().getBoolean("debug", false);
    }

    public static String APIKey() {
        return NapTheX.getInstance().getConfig().getString("NapTheX-API.key", "");
    }

    public static String APISecret() {
        return NapTheX.getInstance().getConfig().getString("NapTheX-API.secret", "");
    }

    public static boolean callBack() {
        return NapTheX.getInstance().getConfig().getBoolean("NapTheX-API.callback", true);
    }

    public static boolean customURL() {
        return NapTheX.getInstance().getConfig().getBoolean("NapTheX-API.custom.enable", false);
    }

    public static String customURLValue() {
        return NapTheX.getInstance().getConfig().getString("NapTheX-API.custom.url", "");
    }

    public static boolean SQLEnable() {
        return NapTheX.getInstance().getConfig().getBoolean("mysql.enable", false);
    }

    public static String SQLHost() {
        return NapTheX.getInstance().getConfig().getString("mysql.host", "");
    }

    public static int SQLPort() {
        return NapTheX.getInstance().getConfig().getInt("mysql.port", 3306);
    }

    public static String SQLUser() {
        return NapTheX.getInstance().getConfig().getString("mysql.user", "");
    }

    public static String SQLPassword() {
        return NapTheX.getInstance().getConfig().getString("mysql.password", "");
    }

    public static String SQLDatabase() {
        return NapTheX.getInstance().getConfig().getString("mysql.database", "");
    }

    public static int placeholderUpdate() {
        return NapTheX.getInstance().getConfig().getInt("placeholder_update", 300);
    }

    public static boolean fastCmd() {
        return NapTheX.getInstance().getConfig().getBoolean("fastcmd", true);
    }

    public static List<String> cardList() {
        return NapTheX.getInstance().getConfig().getStringList("card.enable");
    }
}
