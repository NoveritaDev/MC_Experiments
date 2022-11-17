package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Collection;

public class HealthDrainSpell implements CustomSpell {
    private int strength;

    public HealthDrainSpell(int strength) {
        this.strength = strength;
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public void execute(PlayerInteractEvent event) {
        Location loc = event.getPlayer().getTargetBlock(null,5).getLocation();
        Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 1, 1, 1, entity -> entity.getType() == EntityType.PLAYER);
        for (Entity e: entities) {
            Player p = (Player) e;
            p.setHealth(Math.max(p.getHealth() - strength,0));
            event.getPlayer().setHealth(Math.min(20,event.getPlayer().getHealth() + strength));
            break;
        }
    }
}
