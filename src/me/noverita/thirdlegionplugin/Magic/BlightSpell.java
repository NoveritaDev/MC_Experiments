package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public class BlightSpell implements CustomSpell {
    private Map<Material,Material> materials;
    private int radius;
    private JavaPlugin plugin;

    public BlightSpell(int radius, JavaPlugin plugin) {
        this.plugin = plugin;
        this.radius = radius;
        materials = new HashMap<>();
        materials.put(Material.ACACIA_LEAVES,Material.AIR);
        materials.put(Material.SPRUCE_LEAVES,Material.AIR);
        materials.put(Material.DARK_OAK_LEAVES,Material.AIR);
        materials.put(Material.OAK_LEAVES,Material.AIR);
        materials.put(Material.BIRCH_LEAVES,Material.AIR);
        materials.put(Material.JUNGLE_LEAVES,Material.AIR);
        materials.put(Material.VINE,Material.AIR);
        materials.put(Material.GRASS,Material.DEAD_BUSH);
        materials.put(Material.GRASS_BLOCK,Material.DIRT);
        materials.put(Material.SUGAR_CANE,Material.AIR);
        materials.put(Material.WHEAT_SEEDS,Material.AIR);
        materials.put(Material.CARROTS,Material.AIR);
        materials.put(Material.POTATOES,Material.AIR);
        materials.put(Material.FARMLAND,Material.DIRT);
        materials.put(Material.OAK_SAPLING,Material.DEAD_BUSH);
        materials.put(Material.BIRCH_SAPLING,Material.DEAD_BUSH);
        materials.put(Material.SPRUCE_SAPLING,Material.DEAD_BUSH);
        materials.put(Material.JUNGLE_SAPLING,Material.DEAD_BUSH);
        materials.put(Material.DARK_OAK_SAPLING,Material.DEAD_BUSH);
        materials.put(Material.ACACIA_SAPLING,Material.DEAD_BUSH);
    }

    @Override
    public int getCost() {
        return radius * radius;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Location loc = e.getPlayer().getLocation();
        World w = loc.getWorld();

        for (int i = 0; i < radius; ++i) {
            int finalI = i;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                int rad = finalI + 1;
                int radiusSquared = rad * rad;
                for (int x = -rad; x < rad; ++x) {
                    for (int z = -rad; z < rad; ++z) {
                        for (int y = -rad; y < rad; ++y) {
                            if (x * x + z * z + y * y < radiusSquared) {
                                Vector vect = new Vector(x, y, z);
                                Block b = w.getBlockAt(loc.add(vect));
                                loc.subtract(vect);
                                Material temp = materials.get(b.getType());
                                if (temp != null) {
                                    b.setType(temp);
                                }
                            }
                        }
                    }
                }
            },i * 8);

        }
    }
}
