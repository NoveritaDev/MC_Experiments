package me.noverita.thirdlegionplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RealisticFalls implements Listener {
    @EventHandler
    public void onFallDamage(EntityDamageEvent ede) {
        if (ede.getEntity().getType() == EntityType.PLAYER) {
            if (ede.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Player player = (Player) ede.getEntity();
                new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false).apply(player);
            }
        }
    }

    @EventHandler
    public void onWaterBucket(PlayerBucketEmptyEvent pbee) {
        if (pbee.getPlayer().getVelocity().getY() < -0.08 && pbee.getBucket() == Material.WATER_BUCKET) {
            pbee.setCancelled(true);
        }
    }

    @EventHandler
    public void reduceKnockback(EntityDamageEvent ede) {
        if (ede.getEntity().getType() == EntityType.PLAYER && ((Player) ede.getEntity()).getInventory().contains(Material.IRON_INGOT)) {
            ede.getEntity().sendMessage("Knockback should be reduced");
            ede.getEntity().sendMessage(ede.getEntity().getLocation().getDirection().toString());
            ede.getEntity().sendMessage(ede.getEntity().getLocation().getDirection().multiply(-0.1).toString());

            ede.getEntity().setVelocity(ede.getEntity().getLocation().getDirection().multiply(-0.1));
        }
    }
}
