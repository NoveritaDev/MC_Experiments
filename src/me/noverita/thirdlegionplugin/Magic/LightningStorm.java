package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Random;


public class LightningStorm implements CustomSpell {
    private Random rand = new Random();
    private int strength;

    public LightningStorm(int strength) {
        this.strength = strength;
    }

    @Override
    public int getCost() {
        return strength;
    }

    @Override
    public void execute(PlayerInteractEvent event) {
        Location loc = event.getPlayer().getLocation();
        for (int i = 0; i < strength; ++i) {
            int x = rand.nextInt(32) - 16 + loc.getBlockX();
            int z = rand.nextInt(32) - 16 + loc.getBlockZ();
            World world = event.getPlayer().getWorld();
            Location spawnLocation = new Location(event.getPlayer().getWorld(), x, world.getHighestBlockYAt(x,z), z);
            world.spawnEntity(spawnLocation, EntityType.LIGHTNING);
            world.spawnParticle(Particle.EXPLOSION_LARGE,spawnLocation,1);
        }
    }
}
