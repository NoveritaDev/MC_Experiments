package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BeamOfLightSpell implements CustomSpell {
    private int strength;
    private JavaPlugin plugin;

    public BeamOfLightSpell(int strength, JavaPlugin plugin) {
        this.strength = strength;
        this.plugin = plugin;
    }

    @Override
    public int getCost() {
        return strength;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        List<Block> blocks = e.getPlayer().getLineOfSight(null,15);
        for (Block b: blocks) {
            if (b.getType() == Material.AIR) {
                b.setType(Material.LIGHT);
                Levelled level = (Levelled) b.getBlockData();
                level.setLevel(strength);
            }
        }

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Block b: blocks) {
                    if (b.getType() == Material.LIGHT) {
                        b.setType(Material.AIR);
                    }
                }
            }
        }, 1200);
    }
}
