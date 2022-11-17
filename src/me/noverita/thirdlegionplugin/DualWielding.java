package me.noverita.thirdlegionplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DualWielding implements Listener {
    private boolean jank;

    public DualWielding() {
        jank = false;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            player.sendMessage(Boolean.toString(jank));
            if (jank) {
                player.swingOffHand();
            } else {
                player.swingMainHand();
            }
            jank = !jank;
        }
    }
}
