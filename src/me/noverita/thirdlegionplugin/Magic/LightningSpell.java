package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.player.PlayerInteractEvent;

public class LightningSpell implements CustomSpell {
    private float explosionStrength;

    public LightningSpell(float explosionStrength) {
        this.explosionStrength = explosionStrength;
    }

    @Override
    public int getCost() {
        return (int) ((1 + explosionStrength) * ( 1 + explosionStrength));
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Location loc = e.getPlayer().getTargetBlock(null,40).getLocation();
        LightningStrike ls = (LightningStrike) e.getPlayer().getWorld().spawnEntity(loc, EntityType.LIGHTNING);
        loc.getWorld().createExplosion(loc,explosionStrength);
    }
}
