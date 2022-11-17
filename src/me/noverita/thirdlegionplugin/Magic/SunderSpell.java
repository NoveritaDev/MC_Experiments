package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class SunderSpell implements CustomSpell {
    @Override
    public int getCost() {
        return 200;
    }

    @Override
    public void execute(PlayerInteractEvent event) {
        Location loc = event.getPlayer().getTargetBlock(null,5).getLocation();
        Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 1, 1, 1, entity -> entity.getType() == EntityType.PLAYER);
        for (Entity e: entities) {
            Player p = (Player) e;
            for (ItemStack item: p.getInventory()) {
                if (item != null && item.getItemMeta() instanceof Damageable) {
                    Damageable meta = (Damageable) item.getItemMeta();
                    meta.setDamage(meta.getDamage() - 100);
                    item.setItemMeta(meta);
                }
            }
            break;
        }
    }
}
