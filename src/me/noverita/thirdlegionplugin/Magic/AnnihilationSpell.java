package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Random;


public class AnnihilationSpell implements CustomSpell {
    private Random rand = new Random();
    private int strength;

    public AnnihilationSpell(int strength) {
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
            Vector vect = new Vector(rand.nextFloat() * 2 - 1, 0, rand.nextFloat() * 2 - 1).normalize().multiply(rand.nextFloat() * 27 + 8);

            int x = vect.getBlockX() + loc.getBlockX();
            int z = vect.getBlockZ() + loc.getBlockZ();

            //int x = rand.nextInt(64) - 32 + loc.getBlockX();
            //int z = rand.nextInt(64) - 32 + loc.getBlockZ();
            World world = event.getPlayer().getWorld();
            Location spawnLocation = new Location(event.getPlayer().getWorld(), x, world.getHighestBlockYAt(x,z), z);
            world.spawnEntity(spawnLocation, EntityType.LIGHTNING);
            world.createExplosion(spawnLocation,5);
            world.spawnParticle(Particle.EXPLOSION_LARGE,spawnLocation,1);
        }
    }
}
