package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class MobNoAggroSpell implements CustomSpell, Listener {
    private JavaPlugin plugin;
    private Set<Player> active;

    public MobNoAggroSpell(JavaPlugin plugin) {
        this.plugin = plugin;
        active = new HashSet<>();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @Override
    public int getCost() {
        return 200;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        active.add(e.getPlayer());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> active.remove(e.getPlayer()), 1200);
    }

    @EventHandler
    public void onAggro(EntityTargetLivingEntityEvent event) {
        if (active.contains(event.getTarget())) {
            event.setCancelled(true);
        }
    }
}
