package me.noverita.thirdlegionplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class BossAbilities implements CommandExecutor, Listener {
    private Set<Player> bosses;
    private Random rng;

    public BossAbilities() {
        bosses = new HashSet<>();
        rng = new Random();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player && strings.length == 1) {
            if (strings[0].equals("SecretKey")) {
                commandSender.sendMessage("You have the correct key. Your boss abilities have been enabled.");
                bosses.add((Player) commandSender);
            } else {
                commandSender.sendMessage("You do not have access to this secret.");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void useAbility(PlayerInteractEvent event) {
        if (bosses.contains(event.getPlayer()) && event.getItem() != null) {
            Player boss = event.getPlayer();
            Material item = event.getItem().getType();
            switch (item) {
                case FIRE_CHARGE:
                    event.setCancelled(true);
                    Fireball fireball = (Fireball) boss.getWorld().spawnEntity(boss.getEyeLocation(), EntityType.FIREBALL);
                    fireball.setYield(4);
                    fireball.setVelocity(boss.getLocation().getDirection().normalize());
                    break;
                case ZOMBIE_SPAWN_EGG:
                    event.setCancelled(true);
                    Location loc = boss.getLocation();
                    for (int i = 0; i < 5; ++i) {
                        int x = rng.nextInt(30) - 15 + loc.getBlockX();
                        int z = rng.nextInt(30) - 15 + loc.getBlockY();
                        int y = boss.getWorld().getHighestBlockYAt(x,z);
                        Zombie zombie = (Zombie) boss.getWorld().spawnEntity(new Location(boss.getWorld(),x,y,z), EntityType.ZOMBIE);
                    }
                    break;
                case GLASS_BOTTLE:
                    event.setCancelled(true);
                    for (Player p: Bukkit.getServer().getOnlinePlayers()) {
                        if (p == boss) {
                            continue;
                        }
                        Vector targetLocation = p.getEyeLocation().toVector();
                        Vector bossLocation = boss.getLocation().toVector();
                        Vector deltaVector = targetLocation.subtract(bossLocation);
                        if (deltaVector.length() < 16) {
                            double power = 16 - deltaVector.length();
                            p.setVelocity(deltaVector.normalize().multiply(power));
                        }
                    }
                    break;
                case FERMENTED_SPIDER_EYE:
                    break;
            }
        }
    }
}
