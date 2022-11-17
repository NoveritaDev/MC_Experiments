package me.noverita.thirdlegionplugin.Origins;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OriginHandler implements Listener {
    private static List<Origin> origins = new ArrayList<>();
    private static Map<Player,String> playerOrigins = new HashMap<>();

    public static boolean register(Origin origin) {
        for (Origin o: origins) {
            if (o.getIdentifier().equals(origin.getIdentifier())) {
                return false;
            }
        }
        origins.add(origin);
        Bukkit.getLogger().info("Kingdoms origin added: "+origin.getIdentifier()+" by "+origin.getAuthor());
        return true;
    }

    public static Origin getOriginData(String name) {
        for (Origin o: origins) {
            if (o.getIdentifier().equals(name)) {
                return o;
            }
        }
        return null;
    }

    public static Origin unregister(String identifier) {
        for (int i = 0; i < origins.size(); ++i) {
            if (origins.get(i).getIdentifier().equals(identifier)) {
                Origin o = origins.remove(i);
                o.unregisterListeners();
                return o;
            }
        }
        return null;
    }

    public static String getOrigin(Player player) {
        String origin = playerOrigins.get(player);
        if (origin == null) {
            return "None";
        } else {
            return origin;
        }
    }

    public static void setPlayerOrigin(Player player, String identifier) {
        playerOrigins.put(player,identifier);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        setPlayerOrigin(event.getPlayer(),"Slimeling");
    }

    @EventHandler
    public  void onLeave(PlayerQuitEvent event) {
        playerOrigins.remove(event.getPlayer());
    }
}
