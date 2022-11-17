package me.noverita.thirdlegionplugin;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PurificationAnimation implements CommandExecutor {
    private JavaPlugin jp;
    private BukkitTask task = null;

    public PurificationAnimation(JavaPlugin plugin) {
        jp = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();
            Location loc = player.getLocation();

            final int[] velocity = {1};
            int iterations = 15;

            Runnable runnable = new Runnable() {
                public void run() {
                    world.playSound(loc, Sound.ITEM_TOTEM_USE, ((float)velocity[0] / iterations),((float)velocity[0] / iterations));
                    Vector vec = new Vector(velocity[0],0,0);
                    int count = 100 * velocity[0];
                    for (int i = 0; i < count; ++i) {

                        world.spawnParticle(Particle.FLAME, loc, 0, vec.getX(), vec.getY(), vec.getZ(), 0.05, null, true);
                        vec = vec.rotateAroundY(i * (2 * Math.PI / count));
                    }

                    ++velocity[0];
                }
            };

            Runnable finalExplosion = new Runnable() {
                public void run() {
                    Random r = new Random();
                    world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_DEATH, ((float)velocity[0] / iterations),((float)velocity[0] / iterations));
                    int count = 500 * velocity[0];
                    for (int i = 0; i < count; ++i) {
                        Vector vec = new Vector(2 * r.nextFloat() - 1,r.nextFloat(),2 * r.nextFloat() - 1).normalize().multiply(velocity[0]);
                        world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 0, vec.getX(), vec.getY(), vec.getZ(), 0.05, null, true);
                    }
                }
            };

            Runnable sparkles = new Runnable() {
                public void run() {
                    Random r = new Random();
                    int count = 5000;
                    for (int i = 0; i < count; ++i) {
                        Vector vec = new Vector(2 * r.nextFloat() - 1,0,2 * r.nextFloat() - 1).normalize().multiply(r.nextFloat() * iterations);
                        world.spawnParticle(Particle.SOUL, loc.add(vec), 0, 0, 0.2, 0, 0.05, null, true);
                    }
                }
            };

            for (int i = 0; i < iterations; ++i) {
                jp.getServer().getScheduler().scheduleSyncDelayedTask(jp, runnable, 20 * i);
            }
            jp.getServer().getScheduler().scheduleSyncDelayedTask(jp, finalExplosion, 20 * iterations);
            jp.getServer().getScheduler().scheduleSyncDelayedTask(jp, sparkles, 20 * (iterations + 2));

            return true;
        }
        return false;
    }
}
