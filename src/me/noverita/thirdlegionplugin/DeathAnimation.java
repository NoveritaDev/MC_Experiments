package me.noverita.thirdlegionplugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DeathAnimation implements Listener {

    private JavaPlugin plugin;

    public DeathAnimation(JavaPlugin plugin) {
       this.plugin = plugin;
    }

    public float directionVectorYaw(Vector motion) {
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0;
        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        return (float) (-yaw * 180 / Math.PI - 90);
    }

    public float directionVectorPitch(Vector direction) {
        return (float) Math.asin(direction.getY() / (
                Math.sqrt(Math.pow(direction.getX(),2) + Math.pow(direction.getZ(),2))
                ));
    }

    @EventHandler
    public void onDeath(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.GOLDEN_PICKAXE) {
            Player player = event.getPlayer();
            ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(as);

            Vector facing = player.getLocation().getDirection().normalize();
            Vector onPlane = new Vector(facing.getX(),0,facing.getZ()).normalize();
            Vector orthogonal = new Vector(
                    facing.getY() * onPlane.getZ() - facing.getZ() * onPlane.getY(),
                    facing.getZ() * onPlane.getX() - facing.getX() * onPlane.getZ(),
                    facing.getX() * onPlane.getY() - facing.getY() * onPlane.getX()
            ).normalize();

            player.sendMessage("Facing: "+facing+", On Plane: "+onPlane+", Orthogonal: "+orthogonal);

            VolatileIDBox vidb = new VolatileIDBox();
            vidb.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                int iterations = 80;
                int i = 0;

                @Override
                public void run() {
                    as.getLocation().setDirection(facing);
                    Vector vec = facing.rotateAroundAxis(orthogonal,(0.5 * Math.PI / iterations));
                    as.setRotation(directionVectorPitch(vec), directionVectorYaw(vec));
                    i++;
                    if (i > iterations) {
                        Bukkit.getScheduler().cancelTask(vidb.taskID);
                        player.sendMessage("Looking vertical");
                        Bukkit.getScheduler().cancelTask(vidb.taskID);
                        player.setSpectatorTarget(null);
                        as.remove();
                        Creeper creeper = (Creeper) as.getWorld().spawnEntity(as.getLocation(), EntityType.CREEPER);
                        creeper.setAI(false);
                        creeper.setAware(false);
                        creeper.setRotation(directionVectorPitch(vec), -directionVectorYaw(vec));
                        player.setSpectatorTarget(creeper);
                        i = 0;
                        vidb.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Vector vec = facing.rotateAroundAxis(orthogonal,-(0.5 * Math.PI / iterations));
                                creeper.setRotation(directionVectorPitch(vec), directionVectorYaw(vec));
                                i++;
                                if (i > iterations) {
                                    Bukkit.getScheduler().cancelTask(vidb.taskID);
                                    creeper.remove();
                                }
                            }
                        }, 0L,2L);
                    }
                }
            }, 0L,2L);
        }
    }

    // I REALLY hate this.
    private class VolatileIDBox {
        public volatile int taskID;
        public VolatileIDBox() {
            taskID = 0;
        }
    }
}
