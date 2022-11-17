package me.noverita.thirdlegionplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class FakeNameHandler implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ArmorStand as = (ArmorStand) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setSmall(true);
        as.setCustomName("Test");
        as.setCustomNameVisible(true);
        event.getPlayer().eject();
        event.getPlayer().addPassenger(as);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        List<Entity> passengers = event.getPlayer().getPassengers();
        Bukkit.broadcastMessage(passengers.toString());
        for (Entity e: passengers) {
            event.getPlayer().removePassenger(e);
            e.remove();
        }
    }
}
