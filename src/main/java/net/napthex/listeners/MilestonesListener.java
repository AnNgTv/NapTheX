package net.napthex.listeners;

import net.napthex.config.DonateMilestone;
import net.napthex.event.PlayerCardChargedEvent;
import net.napthex.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.List;

public class MilestonesListener implements Listener {

    @EventHandler
    public void onCharged(PlayerCardChargedEvent event) {
        if (!DonateMilestone.isEnable()) return;

        Player player = event.getPlayer();
        int total = event.getTotalCharged();
        int oldTotal = total - event.getCardPrice();

        HashSet<Integer> milestones = DonateMilestone.getMilestones();
        for (int milestone : milestones) {
            if (total >= milestone && oldTotal < milestone) {
                List<String> commands = DonateMilestone.getCommands(milestone);
                for (String cmd : commands) {
                    Utils.dispatchCommand(player, cmd);
                }
            }
        }
    }
}
