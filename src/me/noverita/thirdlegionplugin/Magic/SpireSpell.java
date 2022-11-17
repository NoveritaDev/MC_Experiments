package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class SpireSpell implements CustomSpell {
    private static final int HEIGHT = 10;

    @Override
    public int getCost() {
        return 10;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Block b = e.getPlayer().getTargetBlock(null,10);
        World w = b.getWorld();
        if (b.getType() != Material.AIR) {
            int count = 1;
            for (; count < HEIGHT; ++count) {
                if (w.getBlockAt(b.getX(), b.getY() + count, b.getZ()).getType() != Material.AIR) {
                    break;
                }
            }
            --count;

            for (int i = 0; i < count; ++i) {
                Block original = w.getBlockAt(b.getX(), b.getY() - i, b.getZ());
                Block newBlock = w.getBlockAt(b.getX(), b.getY() + count - i, b.getZ());
                newBlock.setBlockData(original.getBlockData());
                original.setType(Material.AIR);
            }
        }
    }
}
