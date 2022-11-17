package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.util.Vector;

public class Recall implements CustomSpell {
    @Override
    public int getCost() {
        return 900;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Inventory inv = e.getPlayer().getInventory();
        for (ItemStack item: inv.getContents()) {
            if (item != null && item.getType() == Material.COMPASS) {
                CompassMeta meta = (CompassMeta) item.getItemMeta();
                Location loc = meta.getLodestone();
                if (loc != null) {
                    Location finalLoc = loc.add(new Vector(0,1,0));
                    e.getPlayer().teleport(finalLoc);
                    break;
                }
            }
        }
    }
}
