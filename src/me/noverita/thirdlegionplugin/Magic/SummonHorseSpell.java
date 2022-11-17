package me.noverita.thirdlegionplugin.Magic;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SummonHorseSpell implements CustomSpell {
    private static Map<Player, Horse> horses = new HashMap<>();

    private double speed;
    private double jump;
    private int health;

    public SummonHorseSpell(double speed, double jump, int health) {
        this.speed = speed;
        this.jump = jump;
        this.health = health;
    }

    @Override
    public int getCost() {
        return (int) (speed + jump + health);
    }

    @Override
    public void execute(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        World w = p.getWorld();
        Horse temp = horses.remove(p);
        if (temp != null) {
            temp.remove();
        }

        Horse horse = (Horse) w.spawnEntity(p.getLocation(), EntityType.HORSE);
        horse.setTamed(true);
        horse.setOwner(p);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE,1));
        horse.setJumpStrength(jump);
        horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        horses.put(p,horse);
    }
}
