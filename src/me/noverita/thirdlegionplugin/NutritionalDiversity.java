package me.noverita.thirdlegionplugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class NutritionalDiversity implements Listener {
    private int memoryLength;
    private HashMap<Material, FoodValue> foodValues;
    private HashMap<Player, Queue<Material>> playerNutrition;

    public NutritionalDiversity() {
        memoryLength = 10;
        foodValues = new HashMap<>();
        foodValues.put(Material.APPLE, new FoodValue(4, 2.4f));
        foodValues.put(Material.BAKED_POTATO, new FoodValue(5, 6.0f));
        foodValues.put(Material.BEETROOT, new FoodValue(1, 1.2f));
        foodValues.put(Material.BEETROOT_SOUP, new FoodValue(6, 7.2f));
        foodValues.put(Material.BREAD, new FoodValue(5, 6.0f));
        foodValues.put(Material.CARROT, new FoodValue(3, 3.6f));
        foodValues.put(Material.CHORUS_FRUIT, new FoodValue(4, 2.4f));
        foodValues.put(Material.COOKED_CHICKEN, new FoodValue(6, 7.2f));
        foodValues.put(Material.COOKED_COD, new FoodValue(5, 6.0f));
        foodValues.put(Material.COOKED_MUTTON, new FoodValue(6, 9.6f));
        foodValues.put(Material.COOKED_PORKCHOP, new FoodValue(8, 12.8f));
        foodValues.put(Material.COOKED_RABBIT, new FoodValue(5, 6f));
        foodValues.put(Material.COOKED_SALMON, new FoodValue(6, 9.6f));
        foodValues.put(Material.COOKIE, new FoodValue(2, 0.4f));
        foodValues.put(Material.DRIED_KELP, new FoodValue(1, 0.6f));
        foodValues.put(Material.ENCHANTED_GOLDEN_APPLE, new FoodValue(4, 9.6f));
        foodValues.put(Material.GOLDEN_APPLE, new FoodValue(4, 9.6f));
        foodValues.put(Material.GLOW_BERRIES, new FoodValue(2, 0.4f));
        foodValues.put(Material.GOLDEN_CARROT, new FoodValue(6, 14.4f));
        foodValues.put(Material.HONEY_BOTTLE, new FoodValue(6, 1.2f));
        foodValues.put(Material.MELON_SLICE, new FoodValue(2, 1.2f));
        foodValues.put(Material.MUSHROOM_STEW, new FoodValue(6, 7.2f));
        foodValues.put(Material.POISONOUS_POTATO, new FoodValue(2, 1.2f));
        foodValues.put(Material.POTATO, new FoodValue(1, 0.6f));
        foodValues.put(Material.PUFFERFISH, new FoodValue(1, 0.2f));
        foodValues.put(Material.PUMPKIN_PIE, new FoodValue(8, 4.8f));
        foodValues.put(Material.RABBIT_STEW, new FoodValue(10, 12f));
        foodValues.put(Material.BEEF, new FoodValue(3, 1.8f));
        foodValues.put(Material.CHICKEN, new FoodValue(2, 1.2f));
        foodValues.put(Material.COD, new FoodValue(2, 0.4f));
        foodValues.put(Material.MUTTON, new FoodValue(2, 1.2f));
        foodValues.put(Material.PORKCHOP, new FoodValue(3, 1.8f));
        foodValues.put(Material.RABBIT, new FoodValue(3, 1.8f));
        foodValues.put(Material.SALMON, new FoodValue(2, 0.4f));
        foodValues.put(Material.ROTTEN_FLESH, new FoodValue(4, 0.8f));
        foodValues.put(Material.SPIDER_EYE, new FoodValue(2, 3.2f));
        foodValues.put(Material.COOKED_BEEF, new FoodValue(8, 12.8f));
        foodValues.put(Material.SUSPICIOUS_STEW, new FoodValue(6, 7.2f));
        foodValues.put(Material.SWEET_BERRIES, new FoodValue(2, 0.4f));
        foodValues.put(Material.TROPICAL_FISH, new FoodValue(1, 0.2f));

        playerNutrition = new HashMap<>();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent pqe) {
        playerNutrition.remove(pqe.getPlayer());
    }

    @EventHandler
    public void onEat(FoodLevelChangeEvent flce) {
        ItemStack item = flce.getItem();

        if (item != null) {
            Material foodType = item.getType();
            if (flce.getEntity() instanceof Player && foodValues.containsKey(foodType)) {
                Player player = (Player) flce.getEntity();
                if (!playerNutrition.containsKey(player)) {
                    playerNutrition.put(player, new LinkedList<>());
                }
                Queue<Material> foodHistory = playerNutrition.get(player);

                int countInHistory = 0;
                for (Material food : foodHistory) {
                    if (food == foodType) {
                        ++countInHistory;
                    }
                }

                double percentValue = 1 - (((double) countInHistory) / memoryLength);
                FoodValue values = foodValues.get(foodType);

                int foodValue = Math.max((int) Math.ceil(values.getHunger() * percentValue),1);
                float saturationValue = (float) (values.getSaturation() * percentValue);
                flce.setFoodLevel(player.getFoodLevel() + foodValue);
                player.setSaturation(player.getSaturation() + saturationValue);
                foodHistory.add(foodType);
                if (foodHistory.size() > memoryLength) {
                    foodHistory.poll();
                }
                player.sendMessage(String.format("You have eaten that food %d out of the last %d times. Normally that food would give you %d hunger and %.1f saturation, but this time it gave you %d hunger and %.1f saturation.", countInHistory, memoryLength, values.getHunger(), values.getSaturation(), foodValue, saturationValue));
            }
        }
    }

    private static class FoodValue {
        private int hunger;
        private float saturation;

        public FoodValue(int hunger, float saturation) {
            this.hunger = hunger;
            this.saturation = saturation;
        }

        public float getSaturation() {
            return saturation;
        }

        public int getHunger() {
            return hunger;
        }
    }
}
