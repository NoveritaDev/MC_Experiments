package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SummonFoodSpell implements CustomSpell {
    @Override
    public int getCost() {
        return 20;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        ItemStack items = new ItemStack(Material.BREAD, 4);
        e.getPlayer().getInventory().addItem(items);
    }
}
