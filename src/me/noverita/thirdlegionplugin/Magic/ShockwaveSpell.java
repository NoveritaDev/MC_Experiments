package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class ShockwaveSpell implements CustomSpell {
    @Override
    public int getCost() {
        return 5;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (p == e.getPlayer()) {
                continue;
            }
            Vector targetLocation = p.getEyeLocation().toVector();
            Vector bossLocation = e.getPlayer().getLocation().toVector();
            Vector deltaVector = targetLocation.subtract(bossLocation);
            if (deltaVector.length() < 16) {
                p.setVelocity(deltaVector.normalize().multiply(0.4));
            }
        }
    }
}
