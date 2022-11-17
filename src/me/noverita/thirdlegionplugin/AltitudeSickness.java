package me.noverita.thirdlegionplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AltitudeSickness implements Listener {
    @EventHandler
    public void onMovement(PlayerMoveEvent pme) {
        if (pme.getTo().getY() > 256) {
            pme.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,120,0,false,false));
            pme.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,120,0,false,false));
            pme.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,120,0,false,false));
        }
    }
}
