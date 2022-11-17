package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class IceOrbSpell implements CustomSpell {
    private int radius;
    private JavaPlugin plugin;

    public IceOrbSpell(int radius, JavaPlugin plugin) {
        this.radius = radius;
        this.plugin = plugin;
    }

    @Override
    public int getCost() {
        return radius * radius;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Location loc = e.getPlayer().getTargetBlock(null,40).getLocation();
        World w = loc.getWorld();

        Set<Block> blocks = new HashSet<>();

        int radiusSquared = radius * radius;
        for (int x = -radius; x < radius; ++x) {
            for (int z = -radius; z < radius; ++z) {
                for (int y = -radius; y < radius; ++y) {
                    if (x*x + z*z + y*y < radiusSquared) {
                        Vector vect = new Vector(x,y,z);
                        Block b = w.getBlockAt(loc.add(vect));
                        loc.subtract(vect);
                        if (b.getType() == Material.AIR) {
                            b.setType(Material.ICE);
                            blocks.add(b);
                        }
                    }
                }
            }
        }

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Block b: blocks) {
                    if (b.getType() == Material.ICE || b.getType() == Material.WATER) {
                        b.setType(Material.AIR);
                    }
                }
            }
        }, 400);
    }
}
