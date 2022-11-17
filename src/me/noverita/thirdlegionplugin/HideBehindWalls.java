package me.noverita.thirdlegionplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class HideBehindWalls implements Listener {

    /*public HideBehindWalls(JavaPlugin plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player p1: players) {
                Collection<Entity> nearby = p1.getWorld().getNearbyEntities(
                        p1.getLocation(),50,50,50,
                        entity -> entity.getType() == EntityType.PLAYER
                );
                for (Entity p2: nearby) {
                    boolean canSee = p1.hasLineOfSight(p2);
                    if (canSee) {
                        p1.showPlayer(plugin, (Player) p2);
                    } else {
                        p1.hidePlayer(plugin, (Player) p2);
                    }
                }
            }
        }, 0L,5L);
    }*/

    public HideBehindWalls(JavaPlugin plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player p1: players) {
                for (Entity p2: players) {
                    boolean canSee = p1.hasLineOfSight(p2);
                    if (canSee) {
                        p1.showPlayer(plugin, (Player) p2);
                    } else {
                        p1.hidePlayer(plugin, (Player) p2);
                    }
                }
            }
        }, 0L,5L);
    }
}
