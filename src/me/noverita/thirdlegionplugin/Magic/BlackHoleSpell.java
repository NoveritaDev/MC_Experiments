package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class BlackHoleSpell implements CustomSpell {
    private JavaPlugin plugin;

    public BlackHoleSpell(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Player caster = e.getPlayer();
        final int[] runs = {200};
        final int[] id = {-1};

        id[0] = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Location origin = caster.getLocation();
            runs[0] -= 1;
            for (Entity p: caster.getWorld().getNearbyEntities(origin,32,32,32)) {
                if (p != caster) {
                    Vector vect = origin.toVector().subtract(p.getLocation().toVector()).normalize();
                    double distance = p.getLocation().distance(origin);
                    double speed = 0.15 / distance * distance;
                    Vector vel = p.getVelocity();
                    vel.add(vect.multiply(speed));
                    p.setVelocity(vel);
                }
            }

            if (runs[0] <= 0) {
                caster.sendMessage("Black hole spell ended.");
                plugin.getServer().getScheduler().cancelTask(id[0]);
            }
        },0,1);
    }
}
