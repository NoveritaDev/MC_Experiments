package me.noverita.thirdlegionplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class BleedingHandler implements Listener {
    private final Collection<Player> bleedingPlayers;
    private final Random rng;

    public BleedingHandler(JavaPlugin jp) {
        bleedingPlayers = new HashSet<>();
        rng = new Random();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(jp, new BleedingTask(), 0, 40);
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent ede) {
        Entity entity = ede.getEntity();
        if (entity.getType() == EntityType.PLAYER && !bleedingPlayers.contains(entity)) {
            if (rng.nextDouble() > 0.9) {
                entity.sendMessage(ChatColor.RED + "You are bleeding!" + ChatColor.RESET);
                bleedingPlayers.add((Player) entity);
            }
        }
    }

    @EventHandler
    public void onUseBandage(PlayerInteractEvent pie) {
        Player p = pie.getPlayer();
        ItemStack is = pie.getItem();
        if (is != null && is.getType() == Material.PAPER) {
            boolean used = bleedingPlayers.remove(p);
            if (used) {
                p.sendMessage(ChatColor.AQUA + "You are no longer bleeding." + ChatColor.RESET);
                is.setAmount(is.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onDeathCancelBleeding(PlayerDeathEvent pde) {
        bleedingPlayers.remove(pde.getEntity());
    }

    private class BleedingTask implements Runnable {
        @Override
        public void run() {
            for (Player p: bleedingPlayers) {
                p.damage(1);
            }
        }
    }
}
