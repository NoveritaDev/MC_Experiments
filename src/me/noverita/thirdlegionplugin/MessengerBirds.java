package me.noverita.thirdlegionplugin;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Predicate;

public class MessengerBirds implements Listener {
    private JavaPlugin plugin;
    private int currentID;
    private HashMap<Integer, ParrotData> activeDeliveries;

    public MessengerBirds(JavaPlugin jp) {
        plugin = jp;
        activeDeliveries = new HashMap<>();
        currentID = 0;
    }

    @EventHandler
    public void onSendMessage(PlayerDropItemEvent event) {
        Item itemEntity = event.getItemDrop();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() == Material.WRITABLE_BOOK) {
            World world = itemEntity.getWorld();
            Collection<Entity> parrots = world.getNearbyEntities(itemEntity.getLocation(), 3, 3, 3, entity -> entity.getType() == EntityType.PARROT);
            if (!parrots.isEmpty()) {
                Parrot parrot = null;
                double distance = Double.MAX_VALUE;
                for (Entity e: parrots) {
                    double tempDistance = itemEntity.getLocation().distance(e.getLocation());
                    if (tempDistance < distance) {
                        parrot = (Parrot) e;
                        distance = tempDistance;
                    }
                }

                BookMeta meta = (BookMeta) item.getItemMeta();
                String recipientName = meta.getPage(1).strip();
                Player target = plugin.getServer().getPlayer(recipientName);
                Player sender = event.getPlayer();

                sender.sendMessage("Your target is: " + recipientName);
                if (target == null) {
                    sender.sendMessage("Target not found.");
                } else {
                    long journeyTime = (long) sender.getLocation().distance(target.getLocation()) * 4;
                    sender.sendMessage("Message has been sent to " + recipientName + ". Estimated time to arrival: " + journeyTime + " ticks, and the same amount of time to return.");

                    itemEntity.remove();

                    int id = currentID;
                    ++currentID;
                    activeDeliveries.put(id, new ParrotData(parrot));
                    parrot.remove();

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            target.playSound(target.getLocation(), Sound.ENTITY_PARROT_AMBIENT, 1.0f, 1.0f);
                            target.getInventory().addItem(item);
                        }
                    }, journeyTime);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            activeDeliveries.remove(id).createParrot();
                        }
                    }, 2 * journeyTime);
                }
            }
        }
    }

    private static class ParrotData {
        private Parrot.Variant variant;
        private Location location;
        private String name;
        private AnimalTamer owner;

        public ParrotData(Parrot parrot) {
            variant = parrot.getVariant();
            location = parrot.getLocation();
            owner = parrot.getOwner();
            name = parrot.getCustomName();
        }

        public Parrot createParrot() {
            Parrot parrot = (Parrot) location.getWorld().spawnEntity(location,EntityType.PARROT);
            parrot.setCustomName(name);
            parrot.setOwner(owner);
            parrot.setVariant(variant);
            return parrot;
        }
    }
}
