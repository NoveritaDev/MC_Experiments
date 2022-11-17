package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WardingSpell implements CustomSpell, Listener {
    private Map<Block, Player> wardedBlocks;

    public WardingSpell(JavaPlugin plugin) {
        wardedBlocks = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @Override
    public int getCost() {
        return 500;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Block b = e.getPlayer().getTargetBlock(null,4);
        wardedBlocks.put(b,e.getPlayer());
    }

    @EventHandler
    public void onBreakWardedBlock(BlockBreakEvent event) {
        if (wardedBlocks.containsKey(event.getBlock())) {
            if (!event.getPlayer().equals(wardedBlocks.get(event.getBlock()))) {
                event.getBlock().getWorld().createExplosion(event.getBlock().getLocation(),5);
            }
            wardedBlocks.remove(event.getBlock());
        }
    }
}
