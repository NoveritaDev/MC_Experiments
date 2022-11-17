package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class PoisonCloudSpell implements CustomSpell {
    private int radius;
    private int duration;

    public PoisonCloudSpell(int strength) {
        radius = strength;
        duration = 80 * strength;
    }

    @Override
    public int getCost() {
        return radius;
    }

    @Override
    public void execute(PlayerInteractEvent e) {
        Block b = e.getPlayer().getTargetBlock(null,10);
        World w = b.getWorld();
        AreaEffectCloud aec = (AreaEffectCloud) w.spawnEntity(b.getLocation(), EntityType.AREA_EFFECT_CLOUD);
        aec.setBasePotionData(new PotionData(PotionType.POISON));
        aec.setDuration(duration);
        aec.setRadius(radius);
    }
}
