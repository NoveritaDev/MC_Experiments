package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SelfPotionEffectSpell implements CustomSpell {
    private PotionEffectType type;
    private int duration;
    private boolean showParticles;
    private int cost;
    private int amplitude;

    public SelfPotionEffectSpell(PotionEffectType type, int duration, boolean showParticles, int cost, int amplitude) {
        this.type = type;
        this.duration = duration;
        this.showParticles = showParticles;
        this.cost = cost;
        this.amplitude = amplitude;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        e.getPlayer().addPotionEffect(new PotionEffect(type,duration,amplitude, false, showParticles));
    }
}
