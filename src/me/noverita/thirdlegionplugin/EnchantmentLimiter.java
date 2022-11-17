package me.noverita.thirdlegionplugin;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentLimiter implements Listener {
    private Map<Enchantment,Integer> maximums;

    public EnchantmentLimiter() {
        maximums = new HashMap<>();
        maximums.put(Enchantment.DAMAGE_ALL,2);
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        event.getEnchantsToAdd().entrySet().stream()
                .filter(entry -> maximums.containsKey(entry.getKey()))
                .forEach(entry -> {
                    int maxLevel = maximums.get(entry.getKey());
                    if (entry.getValue() > maxLevel) {
                        entry.setValue(maxLevel);
                    }
                });
    }

    @EventHandler
    public void onAnvil(InventoryClickEvent event) {
        if (event.getClickedInventory().getType() == InventoryType.ANVIL) {
            if (event.getSlot() == 2) {
                ItemStack item = event.getCurrentItem();
                item.getEnchantments().entrySet().stream()
                        .filter(entry -> maximums.containsKey(entry.getKey()))
                        .forEach(entry -> {
                            int maxLevel = maximums.get(entry.getKey());
                            if (entry.getValue() > maxLevel) {
                                entry.setValue(maxLevel);
                            }
                        });
            }
        }
    }
}
