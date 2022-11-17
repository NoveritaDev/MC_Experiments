package me.noverita.thirdlegionplugin;

import com.google.common.util.concurrent.Service;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class RideEverything implements Listener {

    private Set<Wither> withers;

    public RideEverything(JavaPlugin plugin) {
        withers = new HashSet<>();

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Wither w: withers) {
                    if (w.getPassengers().size() > 0) {
                        w.setVelocity(w.getPassengers().get(0).getLocation().getDirection().normalize());
                    }
                }
            }
        },0L, 1L);
    }

    @EventHandler
    public void spawnWither(EntitySpawnEvent event) {
        if (event.getEntity().getType() == EntityType.WITHER) {
            withers.add((Wither) event.getEntity());
        }
    }

    @EventHandler
    public void stopTargeting(EntityTargetLivingEntityEvent event) {
        if (event.getEntity().getPassengers().size() > 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void spawnSkull(PlayerInteractEvent event) {
        for (Wither w: withers) {
            if (w.getPassengers().size() > 0) {
                Entity passenger = w.getPassengers().get(0);
                if (passenger.equals(event.getPlayer())) {
                    Vector dir = passenger.getLocation().getDirection().normalize();
                    WitherSkull ws = (WitherSkull) w.getWorld().spawnEntity(w.getLocation().add(dir), EntityType.WITHER_SKULL);
                    ws.setVelocity(dir.multiply(2));
                    break;
                }
            }
        }
    }

    @EventHandler
    public void ride(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.WITHER) {
            event.getRightClicked().addPassenger(event.getPlayer());
        }
    }
}
