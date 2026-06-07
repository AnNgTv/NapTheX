package net.napthex.commands;

import net.napthex.NapTheX;
import net.napthex.config.Config;
import net.napthex.config.DonateMilestone;
import net.napthex.config.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NaptheXCmd implements CommandExecutor, TabCompleter {

    public NaptheXCmd() {
        NapTheX.getInstance().getCommand("napthex").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("napthex.admin")) {
            sender.sendMessage("§cBạn không có quyền sử dụng lệnh này!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§b§l[NapTheX] §fDanh sách lệnh:");
            sender.sendMessage("§e/ntx reload §7- Tải lại cấu hình.");
            sender.sendMessage("§e/ntx top §7- Cập nhật bảng xếp hạng.");
            sender.sendMessage("§e/ntx version §7- Kiểm tra phiên bản.");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            NapTheX.getInstance().reloadConfig();
            Language.saveDefault(); // This usually reloads too
            DonateMilestone.reload();
            sender.sendMessage("§a[NapTheX] Đã tải lại cấu hình!");
        } else if (args[0].equalsIgnoreCase("top")) {
            if (NapTheX.getInstance().NAPTHEXTOP != null) {
                NapTheX.getInstance().NAPTHEXTOP.updatePlaceholder();
                sender.sendMessage("§a[NapTheX] Đã cập nhật bảng xếp hạng!");
            }
        } else if (args[0].equalsIgnoreCase("version")) {
            sender.sendMessage("§a[NapTheX] Phiên bản: §e" + NapTheX.getInstance().getDescription().getVersion());
        } else {
            sender.sendMessage("§cLệnh không hợp lệ!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> sub = new ArrayList<>();
            sub.add("reload");
            sub.add("top");
            sub.add("version");
            return sub;
        }
        return Collections.emptyList();
    }
}
