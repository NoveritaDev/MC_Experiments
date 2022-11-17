package me.noverita.thirdlegionplugin.Origins.Implementations;

import me.noverita.thirdlegionplugin.Origins.AbilityDescriptor;
import me.noverita.thirdlegionplugin.Origins.Origin;
import me.noverita.thirdlegionplugin.Origins.OriginHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Slimeling implements Origin,Listener {
    private static final String identifier = "Slimeling";
    private final Set<Player> players;

    public Slimeling(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        OriginHandler.register(this);
        players = new HashSet<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new EffectHandler(), 0, 1200);

        long timeOffset = (24000 - plugin.getServer().getWorld("world").getTime()) % 12000;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new DailySlimeball(), timeOffset, 24000);
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getAuthor() {
        return "Noverita";
    }

    @Override
    public Material getIcon() {
        return Material.SLIME_BALL;
    }

    @Override
    public List<AbilityDescriptor> getAbilityDescriptors() {
        return null;
    }

    @Override
    public void unregisterListeners() {

    }

    @EventHandler
    public void onRangeDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            if (OriginHandler.getOrigin((Player) event.getEntity()).equals(identifier) && event.getDamager().getType() == EntityType.ARROW) {
                event.setDamage(event.getDamage() * 0.5);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (OriginHandler.getOrigin(event.getPlayer()).equals(identifier)) {
            players.add(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        players.remove(event.getPlayer());
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (OriginHandler.getOrigin((Player) event.getEntity()).equals(identifier)) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    event.setCancelled(true);
                    Vector v = player.getVelocity();
                    player.setVelocity(new Vector(v.getX(), player.getFallDistance() * 0.05, v.getZ()));
                } else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                    event.setDamage(event.getDamage() * 3);
                }
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        if (OriginHandler.getOrigin(event.getPlayer()).equals(identifier)) {
            event.getPlayer().setSaturation(Math.min(event.getPlayer().getSaturation(), event.getPlayer().getFoodLevel() / 2f));
        }
    }

    private class EffectHandler implements Runnable {
        @Override
        public void run() {
            for (Player player: players) {
                if (OriginHandler.getOrigin(player).equals(identifier)) {
                    PotionEffect fireResist = new PotionEffect(PotionEffectType.JUMP, 1600, 1, true, false);
                    fireResist.apply(player);
                }
            }
        }
    }

    private class DailySlimeball implements Runnable {
        @Override
        public void run() {
            for (Player player: players) {
                if (OriginHandler.getOrigin(player).equals(identifier)) {
                    player.getInventory().addItem(new ItemStack(Material.SLIME_BALL));
                }
            }
        }
    }
}
