package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class JumpSpell implements CustomSpell {
    private double strength;

    public JumpSpell(double strength) {
        this.strength = strength;
    }

    @Override
    public int getCost() {
        return (int) strength;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        if (e.getPlayer().isOnGround()) {
            e.getPlayer().setVelocity(e.getPlayer().getVelocity().add(new Vector(0, strength, 0)));
        }
    }
}