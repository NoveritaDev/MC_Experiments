package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class WallOfLightningSpell implements CustomSpell {
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
            w.spawnEntity(new Location(w,b.getX(),y,b.getZ()), EntityType.LIGHTNING);
        }
    }
}
