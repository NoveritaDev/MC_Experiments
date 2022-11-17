package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class FallingBlockStormSpell implements CustomSpell {
    private Random rand;
    private int strength;
    private Material type;

    public FallingBlockStormSpell(int strength, Material material) {
        this.strength = strength;
        rand = new Random();
        type = material;
    }

    @Override
    public int getCost() {
        return 1;
    }

    @Override
    public void execute(PlayerInteractEvent event) {
        Location loc = event.getPlayer().getLocation();
        for (int i = 0; i < strength; ++i) {
            int x = rand.nextInt(32) - 16 + loc.getBlockX();
            int z = rand.nextInt(32) - 16 + loc.getBlockZ();
            World world = event.getPlayer().getWorld();
            Location spawnLocation = new Location(event.getPlayer().getWorld(), x, 319, z);
            FallingBlock temp = world.spawnFallingBlock(spawnLocation, type, (byte) 0);
            temp.setVelocity(new Vector(0,-5,0));
        }
    }
}
