package me.noverita.thirdlegionplugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Collection;

public class LocalDeathMessages implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent pde) {
        String message = pde.getDeathMessage();
        Player player = pde.getEntity();
        Location loc = player.getLocation();
        Collection<? extends Player> otherPlayers = player.getServer().getOnlinePlayers();
        for (Player p : otherPlayers) {
            if (loc.distance(p.getLocation()) < 20) {
                p.sendMessage(ChatColor.RED + message + ChatColor.RESET);
            }
        }
    }
}
