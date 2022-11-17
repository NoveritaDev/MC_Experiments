package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;


public class StoneblastSpell implements CustomSpell {
    private Random rng;

    public StoneblastSpell() {
        rng = new Random();
    }

    @Override
    public int getCost() {
        return 1;
    }

    @Override
    public void execute(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        World w = p.getWorld();
        Location loc = p.getEyeLocation();

        int x = rng.nextInt(5) - 2 + loc.getBlockX();
        int z = rng.nextInt(5) - 2 + loc.getBlockZ();
        int y = w.getHighestBlockYAt(x,z);

        Block b = w.getBlockAt(new Location(w,x,y,z));
        FallingBlock fb = w.spawnFallingBlock(loc, b.getBlockData());
        b.setType(Material.AIR);
        fb.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
    }
}
