package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DarknessSpell implements CustomSpell {
    @Override
    public int getCost() {
        return 40;
    }

    @Override
    public void execute(PlayerInteractEvent event) {
        Player caster = event.getPlayer();
        Location loc = caster.getLocation();
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(loc) < 10 && !p.equals(caster)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,400,0));
            }
        }
    }
}
