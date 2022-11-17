package me.noverita.thirdlegionplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class HarderMobs implements Listener {
    private Random rng;

    public HarderMobs() {
        rng = new Random();
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent ese) {
        switch (ese.getEntityType()) {
            case CREEPER:
                Creeper creeper = (Creeper) ese.getEntity();
                creeper.setPowered(true);
                break;
            case ZOMBIE:
                Zombie zombie = (Zombie) ese.getEntity();
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, true));
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent ple) {
        Projectile projectile = ple.getEntity();

        if (projectile.getShooter() instanceof  Skeleton) {
            Skeleton skeleton = (Skeleton) projectile.getShooter();
            LivingEntity target = skeleton.getTarget();
            if (target != null) {
                Vector targetLocation = target.getEyeLocation().toVector();
                Vector skeletonLocation = skeleton.getEyeLocation().toVector();
                Vector deltaVector = targetLocation.subtract(skeletonLocation).normalize().multiply(projectile.getVelocity().length() * 3);
                projectile.setVelocity(deltaVector);
            }
        }
    }

    @EventHandler
    public void onIgnite(ExplosionPrimeEvent epe) {
        if (epe.getEntityType() == EntityType.CREEPER) {
            epe.setFire(true);
            Creeper creeper = (Creeper) epe.getEntity();
            creeper.setFuseTicks(1);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent edbee) {
        if (edbee.getEntityType() == EntityType.PLAYER && (edbee.getDamager().getType() == EntityType.SPIDER || edbee.getDamager().getType() == EntityType.CAVE_SPIDER)) {
            Player player = (Player) edbee.getEntity();
            Location loc = player.getLocation();
            World world = player.getWorld();
            Block block = world.getBlockAt(loc);
            block.setType(Material.COBWEB);
        } else if (edbee.getEntityType() == EntityType.PLAYER && edbee.getDamager().getType() == EntityType.ZOMBIE) {
            ((Player) edbee.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 1, false, false));
        }
    }

    @EventHandler
    public void replaceWithVariants(EntitySpawnEvent ese) {
        if (ese.getEntityType() == EntityType.SPIDER && rng.nextFloat() > 0.6) {
            Location loc = ese.getLocation();
            ese.setCancelled(true);
            ese.getEntity().getWorld().spawnEntity(loc,EntityType.CAVE_SPIDER);
        } if (ese.getEntityType() == EntityType.SKELETON && rng.nextFloat() > 0.8) {
            Location loc = ese.getLocation();
            ese.setCancelled(true);
            ese.getEntity().getWorld().spawnEntity(loc,EntityType.WITHER_SKELETON);
        }
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent pbee) {
        pbee.setCancelled(true);
        pbee.getPlayer().sendMessage("Your spawn has been set, but ");
        pbee.getPlayer().setBedSpawnLocation(pbee.getBed().getLocation());
    }
}
