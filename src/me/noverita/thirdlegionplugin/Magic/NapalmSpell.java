package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class NapalmSpell implements CustomSpell {
    private Random rand;

    public NapalmSpell() {
        rand = new Random();
    }

    @Override
    public int getCost() {
        return 20;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        World w = e.getPlayer().getWorld();
        for (int j = 0; j < 20; ++j) {
            Entity falling = w.spawnFallingBlock(p.getEyeLocation(), Material.FIRE, (byte) 0);
            Vector velocity = new Vector(rand.nextFloat() - 0.5,rand.nextFloat() / 2,rand.nextFloat() - 0.5).normalize().multiply(rand.nextFloat() + 0.1);
            falling.setVelocity(velocity);
        }
    }
}
