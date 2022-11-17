package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class FireballSpell implements CustomSpell {
    private float yield;

    public FireballSpell(float yield) {
        this.yield = yield;
    }

    @Override
    public int getCost() {
        return (int) (yield * yield);
    }

    @Override
    public void execute(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Fireball fireball = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREBALL);
        fireball.setYield(yield);
        fireball.setVelocity(p.getLocation().getDirection().normalize());
    }
}
