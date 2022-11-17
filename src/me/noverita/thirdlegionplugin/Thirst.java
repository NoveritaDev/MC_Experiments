package me.noverita.thirdlegionplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;

public class Thirst implements Listener {
    private final HashMap<Player,Integer> thirstValues;

    public Thirst(JavaPlugin jp) {
        thirstValues = new HashMap<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(jp, new ThirstTask(), 0, 120);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent pje) {
        thirstValues.put(pje.getPlayer(),100);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent pqe) {
        thirstValues.remove(pqe.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent pde) {
        thirstValues.put(pde.getEntity(),100);
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent pice) {
        ItemStack item = pice.getItem();
        if (item.getType() == Material.POTION) {
            PotionType type = ((PotionMeta) item.getItemMeta()).getBasePotionData().getType();
            if (type == PotionType.WATER) {
                if (thirstValues.get(pice.getPlayer()) > 0) {
                    int thirst = Math.min(thirstValues.get(pice.getPlayer()) + 50,100);
                    thirstValues.put(pice.getPlayer(),thirst);
                    pice.getPlayer().sendMessage(String.format("%sYour thirst level is at %d%%.%s",ChatColor.AQUA,thirst, ChatColor.RESET));
                }
            }
        }
    }

    private class ThirstTask implements Runnable {
        @Override
        public void run() {
            for (Player p: thirstValues.keySet()) {
                if (thirstValues.get(p) > 0) {
                    thirstValues.put(p,thirstValues.get(p) - 1);
                } else {
                    p.damage(500);
                }

                int thirst = thirstValues.get(p);
                if (thirst % 10 == 0) {
                    p.sendMessage(String.format("%sYour thirst level is at %d%%.%s",ChatColor.AQUA,thirst, ChatColor.RESET));
                }

                if (thirst < 30) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,160,0,false,true));
                    if (thirst < 20) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,160,0,false,true));
                        if (thirst < 10) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,320,0,false,true));
                            if (thirst < 5) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,320,0,false,true));
                            }
                        }
                    }
                }
            }
        }
    }
}
