package me.noverita.thirdlegionplugin.Origins;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class OriginMenu implements Listener {
    private Map<Player,String> origins;

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        Inventory menu = Bukkit.createInventory(null, 6 * 9, "Origin Selection");
        menu.setContents(getPage("Slimeling"));

        p.openInventory(menu);
    }

    private ItemStack[] getPage(String title) {
        Origin origin = OriginHandler.getOriginData(title);

        ItemStack[] stacks = new ItemStack[6 * 9];

        stacks[13] = new ItemStack(origin.getIcon());

        return stacks;
    }
}
