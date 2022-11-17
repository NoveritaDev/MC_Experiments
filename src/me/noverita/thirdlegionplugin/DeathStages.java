package me.noverita.thirdlegionplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeathStages implements Listener {
    private static final int STAGGERED = 0;
    private static final int MORTAL = -10;
    private static final int UNCONCIOUS = -15;
    private static final int DEAD = -20;

    private Map<Player, Double> health;
    private JavaPlugin plugin;

    public DeathStages(JavaPlugin plugin) {
        health = new HashMap<>();
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (health.containsKey(player)) {
                double newHealth = health.get(player) - event.getDamage();
                health.put(player, newHealth);
                event.setDamage(0);
                if (newHealth < DEAD) {
                    player.setHealth(0);
                }
            } else if (event.getDamage() > player.getHealth()) {
                double overflow = event.getDamage() - player.getHealth();
                health.put(player, -overflow);
                event.setDamage(Math.max(event.getDamage() - overflow - 1, 0));
            }

            if (health.containsKey(player)) {
                double hp = health.get(player);
                player.sendMessage(Double.toString(hp));

                if (hp < UNCONCIOUS) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 10, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 0, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 999999, 0, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 128, false, false));
                    player.sleep(player.getLocation(),true);
                } else if (hp < MORTAL) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 2, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 0, false, false));
                } else if (hp < STAGGERED) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 1, false, false));
                }
            }
        }
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            if (health.containsKey((Player) event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        health.remove(event.getEntity());
    }

    @EventHandler
    public void useBandage(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.PAPER) {
            health.remove(event.getPlayer());
            event.getItem().setAmount(event.getItem().getAmount() - 1);
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
            event.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
            event.getPlayer().removePotionEffect(PotionEffectType.JUMP);
        }
    }
}
