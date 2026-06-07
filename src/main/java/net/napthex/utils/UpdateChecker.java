package net.napthex.utils;

import net.napthex.NapTheX;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker implements Listener {
    private final String currentVersion;
    private String latestVersion;

    public UpdateChecker() {
        this.currentVersion = NapTheX.getInstance().getDescription().getVersion();
        checkUpdate();
    }

    private void checkUpdate() {
        Task.async(() -> {
            try {
                URL url = new URL("https://thesieutoc.net/version");
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                latestVersion = reader.readLine();
                if (latestVersion != null && !latestVersion.equalsIgnoreCase(currentVersion)) {
                    Bukkit.getLogger().info("[NapTheX] A new version is available: " + latestVersion);
                    Bukkit.getLogger().info("[NapTheX] Current version: " + currentVersion);
                }
            } catch (Exception e) {
                // Ignore
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("napthex.admin")) {
            if (latestVersion != null && !latestVersion.equalsIgnoreCase(currentVersion)) {
                event.getPlayer().sendMessage("§a[NapTheX] Một phiên bản mới đã có sẵn: §e" + latestVersion);
                event.getPlayer().sendMessage("§a[NapTheX] Phiên bản hiện tại: §c" + currentVersion);
            }
        }
    }
}
