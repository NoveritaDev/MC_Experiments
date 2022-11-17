package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class RepairSpell implements CustomSpell {

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Inventory inv = e.getPlayer().getInventory();
        for (ItemStack item: inv.getContents()) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable d = (Damageable) meta;
                d.setDamage(d.getDamage() + 10);
            }
        }
    }
}
