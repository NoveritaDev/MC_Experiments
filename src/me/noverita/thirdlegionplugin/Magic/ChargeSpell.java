package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class ChargeSpell implements CustomSpell {
    private double strength;

    public ChargeSpell(double strength) {
        this.strength = strength;
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Vector vel = p.getLocation().getDirection();
        vel.setY(Math.min(vel.getY(),0));
        vel.normalize();
        vel.multiply(strength);
        p.setVelocity(p.getVelocity().add(vel));
    }
}
