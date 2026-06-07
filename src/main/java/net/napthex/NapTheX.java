package net.napthex;

import net.napthex.commands.NaptheCmd;
import net.napthex.commands.NaptheXCmd;
import net.napthex.config.Config;
import net.napthex.config.DonateMilestone;
import net.napthex.config.Language;
import net.napthex.database.Database;
import net.napthex.database.a.Flatfile;
import net.napthex.database.a.MySQL;
import net.napthex.internal.QueueCardStatus;
import net.napthex.listeners.MilestonesListener;
import net.napthex.listeners.OfflineListener;
import net.napthex.menu.anvil.AnvilMenu_seripin;
import net.napthex.menu.chat.ChatMenu;
import net.napthex.placeholder.NapTheXPlaceholder;
import net.napthex.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NapTheX extends JavaPlugin {
    private static NapTheX instance;
    private static Database database;
    private QueueCardStatus queueCardStatus;
    private OfflineListener offlineListener;
    public NapTheXTop NAPTHEXTOP;

    @Override
    public void onEnable() {
        instance = this;
        NAPTHEXTOP = new NapTheXTop();
        saveDefaultConfig();
        Language.saveDefault();
        DonateMilestone.saveDefault();

        getCommand("napthe").setExecutor(new NaptheCmd());
        getCommand("napthex").setExecutor(new NaptheXCmd());

        Bukkit.getPluginManager().registerEvents(new ChatMenu(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilMenu_seripin(), this);
        Bukkit.getPluginManager().registerEvents(new MilestonesListener(), this);
        
        offlineListener = new OfflineListener();
        Bukkit.getPluginManager().registerEvents(offlineListener, this);
        Bukkit.getPluginManager().registerEvents(new UpdateChecker(), this);

        if (Config.SQLEnable()) {
            MySQL mysql = new MySQL();
            mysql.init();
            database = new Database(mysql);
        } else {
            database = new Database(new Flatfile());
        }

        queueCardStatus = new QueueCardStatus();
        queueCardStatus.init();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                new NapTheXPlaceholder(this).register();
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void onDisable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "papi unregister ntx");
        }
        if (database != null) {
            database.disable();
        }
    }

    public static NapTheX getInstance() {
        return instance;
    }

    public static Database getDatabase() {
        return database;
    }

    public QueueCardStatus getQueueCardStatus() {
        return queueCardStatus;
    }

    public OfflineListener getOffline() {
        return offlineListener;
    }
}
