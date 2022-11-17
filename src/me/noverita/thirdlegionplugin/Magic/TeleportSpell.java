package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeleportSpell implements CustomSpell {
    private int distance;

    public TeleportSpell(int distance) {
        this.distance = distance;
    }

    @Override
    public int getCost() {
        return 400;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Location loc = e.getPlayer().getTargetBlock(null,distance).getLocation();
        e.getPlayer().teleport(loc);
    }
}
