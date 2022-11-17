package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class WallSpell implements CustomSpell {
    private static final int HEIGHT = 10;

    @Override
    public int getCost() {
        return 300;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        List<Block> blocks = p.getLineOfSight(null,16);
        World w = p.getWorld();
        for (Block b: blocks) {
            int y = w.getHighestBlockYAt(b.getX(), b.getZ());
            if (b.getType() != Material.AIR) {
                int count = 1;
                for (; count < HEIGHT; ++count) {
                    if (w.getBlockAt(b.getX(), y + count, b.getZ()).getType() != Material.AIR) {
                        break;
                    }
                }
                --count;

                for (int i = 0; i < count; ++i) {
                    Block original = w.getBlockAt(b.getX(), y - i, b.getZ());
                    Block newBlock = w.getBlockAt(b.getX(), y + count - i, b.getZ());
                    newBlock.setBlockData(original.getBlockData());
                    original.setType(Material.AIR);
                }
            }
        }
    }
}
