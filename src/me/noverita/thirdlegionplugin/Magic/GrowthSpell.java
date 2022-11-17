package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class GrowthSpell implements CustomSpell {
    private int radius;

    public GrowthSpell(int radius) {
        this.radius = radius;
    }

    @Override
    public int getCost() {
        return radius * radius;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Location loc = e.getPlayer().getLocation();
        World w = loc.getWorld();

        int radiusSquared = radius * radius;
        for (int x = -radius; x < radius; ++x) {
            for (int z = -radius; z < radius; ++z) {
                for (int y = -radius; y < radius; ++y) {
                    if (x*x + z*z + y*y < radiusSquared) {
                        Vector vect = new Vector(x,y,z);
                        Block b = w.getBlockAt(loc.add(vect));
                        loc.subtract(vect);
                        b.applyBoneMeal(BlockFace.DOWN);
                    }
                }
            }
        }
    }
}
