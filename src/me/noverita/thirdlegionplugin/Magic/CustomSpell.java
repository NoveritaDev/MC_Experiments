package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public interface CustomSpell {
    int getCost();
    void execute(PlayerInteractEvent e);
}
