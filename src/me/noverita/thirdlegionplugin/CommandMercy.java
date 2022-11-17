package me.noverita.thirdlegionplugin;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;
import java.util.Set;


/*
 * HOW TO USE:
 * CommandMercy mercy = new CommandMercy();
 * plugin.getCommand("mercy").setExecutor(mercy);
 * plugin.getServer().getPluginManager().registerEvents(mercy, this);
 */
public class CommandMercy implements Listener, CommandExecutor {
    private static final String disableMessage = ChatColor.RED + "You are no longer being merciful. Your attacks are lethal." + ChatColor.RESET;
    private static final String enableMessage = ChatColor.AQUA + "You are now being merciful. You cannot directly kill another player in melee. However, due to their inaccuracy, your bow shots may still be lethal, so be careful." + ChatColor.RESET;

    private final Set<Player> mercifulPlayers;

    public CommandMercy() {
        super();
        mercifulPlayers  = new HashSet<>();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command _command, String _s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (arguments.length > 0) {

                // Optional section where you can check if people are merciful.
                Server server = player.getServer();
                for (String name: arguments) {
                    Player queryPlayer = server.getPlayer(name);
                    if (queryPlayer != null) {
                        if (mercifulPlayers.contains(queryPlayer)) {
                            player.sendMessage(ChatColor.AQUA + name + " is being merciful." + ChatColor.RESET);
                        } else {
                            player.sendMessage(ChatColor.RED + name + " is not being merciful." + ChatColor.RESET);
                        }
                    } else {
                        player.sendMessage(name + " is not online.");
                        return false;
                    }
                }

            } else {

                // Toggle mercy mode
                boolean removed = mercifulPlayers.remove(player);
                if (removed) {
                    player.sendMessage(disableMessage);
                } else {
                    player.sendMessage(enableMessage);
                    mercifulPlayers.add(player);
                }

            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onHitByPlayer(EntityDamageByEntityEvent ede) {
        Entity damager = ede.getDamager();
        if (ede.getEntity().getType() == EntityType.PLAYER && damager.getType() == EntityType.PLAYER) {
            Player target = (Player) ede.getEntity();
            if (mercifulPlayers.contains(damager)) {
                double health = target.getHealth();
                if (ede.getDamage() > health) {
                    ede.setDamage((health < 2) ?  0: health - 2);
                    damager.sendMessage(ChatColor.LIGHT_PURPLE + "Being merciful, you spared " + target.getDisplayName() + "'s life.");
                }
            }
        }
    }
}
