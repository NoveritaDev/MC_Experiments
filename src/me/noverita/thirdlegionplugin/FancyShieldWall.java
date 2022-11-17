package me.noverita.thirdlegionplugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Collection;

public class FancyShieldWall implements Listener {
    @EventHandler
    public void shieldBash() {

    }

    @EventHandler
    public void onBlock(PlayerMoveEvent event) {
        Player mover = event.getPlayer();
        Collection<Entity> players = mover.getWorld().getNearbyEntities(
                mover.getLocation(),
                1,1,1,
                entity -> (entity.getType() == EntityType.PLAYER && entity != mover)
        );

        for (Entity p: players) {
            Player player = (Player) p;
            if  (player.isBlocking()) {

                // Find the plane of the shield
                Vector playerDirection = player.getLocation().getDirection();
                Vector shieldPlane = new Vector(playerDirection.getZ(),0,-playerDirection.getX()).normalize();

                // Find the minimum vector from shield plane to moving player's origin
                // position of point rel one end of line
                double A = mover.getVelocity().getX() - playerDirection.getX();
                double B = mover.getVelocity().getZ() - playerDirection.getZ();

                // vector along line
                double C = playerDirection.getX() + shieldPlane.getX() - playerDirection.getX();
                double D = playerDirection.getZ() + shieldPlane.getZ()  - playerDirection.getZ();

                // orthogonal vector
                double E = -D;
                double F = C;

                // Final steps
                double dot = A * E + B * F;
                double len_sq = E * E + F * F;
                double distance = Math.abs(dot) / Math.sqrt(len_sq);
                mover.sendMessage(""+distance);

                // Subtract this vector from the total velocity.
                Vector counterVector = new Vector(playerDirection.getX(),0,playerDirection.getZ()).normalize().multiply(distance);
                Vector velocity = mover.getVelocity();
                //Vector newVelocity = velocity.add(counterVector);

                mover.sendMessage("Velocity: "+velocity.toString());
                velocity.add(counterVector);
                mover.sendMessage("Counter: "+counterVector.toString());
                mover.sendMessage("New: "+velocity.toString());
                //mover.setVelocity(newVelocity);
                mover.setVelocity(velocity);
            }
        }
    }
}
