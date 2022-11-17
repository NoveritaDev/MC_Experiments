package me.noverita.thirdlegionplugin.TeamBattles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class ScoreTracker implements Listener, CommandExecutor {
    private Map<UUID,BattleScore> scores;

    public ScoreTracker() {
        scores = new HashMap<>();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Player killer = dead.getKiller();

        scores.get(dead.getUniqueId()).deaths++;
        if (killer != null) {
            scores.get(killer.getUniqueId()).kills++;
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        int teamCount;

        if (strings.length == 1) {
            try {
                teamCount = Integer.parseInt(strings[0]);
            } catch (NumberFormatException exception) {
                commandSender.sendMessage(ChatColor.RED + "Invalid number of teams specified. Usage: /tlteams <number of teams>"+ChatColor.RESET);
                return false;
            }
        } else {
            teamCount = 2;
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getMainScoreboard();
        for (int i = 0; i < teamCount; ++i) {
            if (scoreboard.getTeam("tl_team_"+i) == null) {
                Team newTeam = scoreboard.registerNewTeam("tl_team_" + i);
                newTeam.setDisplayName("Team " + i);
                newTeam.setPrefix("[Team " + i + "]");
            }
        }

        List<List<Player>> teams = new ArrayList<>();
        List<Double> teamValues = new ArrayList<>();
        for (int i = 0; i < teamCount; ++i) {
            teams.add(new ArrayList<>());
            teamValues.add(0.0);
        }

        PriorityQueue<Player> playerQueue = new PriorityQueue<>(
                (playerOne, playerTwo) ->
                        (int) Math.ceil(scores.get(playerOne.getUniqueId()).getKDR()
                                - scores.get(playerTwo.getUniqueId()).getKDR())
        );
        playerQueue.addAll(Bukkit.getOnlinePlayers());

        while (playerQueue.size() > 0) {
            Player player = playerQueue.poll();
            double score = scores.get(player.getUniqueId()).getKDR();

            double minScore = teamValues.get(0);
            int index = 0;
            for (int i = 1; i < teamCount; ++i) {
                double tempScore = teamValues.get(i);
                if (tempScore < minScore) {
                    minScore = tempScore;
                    index = i;
                }
            }

            teams.get(index).add(player);
            teamValues.set(index, teamValues.get(index) + score);
        }

        return true;
    }

    private static class BattleScore {
        private int deaths;
        private int kills;

        public BattleScore() {
            deaths = 1;
            kills = 1;
        }

        public int getDeaths() {
            return deaths;
        }

        public int getKills() {
            return kills;
        }

        public double getKDR() {
            return ((double) kills) / deaths;
        }
    }
}
