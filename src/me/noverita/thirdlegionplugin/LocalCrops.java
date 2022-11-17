package me.noverita.thirdlegionplugin;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class LocalCrops implements Listener {
    private final static int mapSize = 128;

    private final short[][] carrotGrowth;
    private final short[][] wheatGrowth;
    private final short[][] potatoGrowth;
    private final short[][] beetrootGrowth;

    private final Random rng;

    public LocalCrops() {
        carrotGrowth = new short[128][128];
        wheatGrowth = new short[128][128];
        potatoGrowth = new short[128][128];
        beetrootGrowth = new short[128][128];

        // Carrot
        for (int row = 0; row < mapSize; ++row) {
            for (int col = 0; col < mapSize; ++col) {
                carrotGrowth[col][row] = (short) ((Math.max(row,mapSize - row) - (mapSize >> 1)) * (256 / mapSize * 2));
            }
        }

        // Wheat
        for (int row = 0; row < 128; ++row) {
            for (int col = 0; col < 128; ++col) {
                wheatGrowth[col][row] = (short) (Math.min(Math.min((Math.max(row, 128 - row) - 64), row), 128 - row) * 8);
            }
        }

        // Potato
        for (int row = 0; row < 128; ++row) {
            for (int col = 0; col < 128; ++col) {
                potatoGrowth[col][row] = (short) (Math.min(row,128 - row) * 4);
            }
        }

        // Beetroot
        for (int row = 0; row < 128; ++row) {
            for (int col = 0; col < 128; ++col) {
                beetrootGrowth[col][row] = 255;
            }
        }

        rng = new Random();
    }

    @EventHandler
    public void allowToGrow(BlockGrowEvent bge) {
        Block block = bge.getBlock();
        Material type = block.getType();
        Location loc = block.getLocation();
        double probability = 1;
        Chunk chunk = loc.getChunk();
        if (chunk.getX() < 0 || chunk.getX() >= mapSize || chunk.getZ() < 0 || chunk.getZ() >= mapSize) {
            return;
        }
        switch (type) {
            case WHEAT:
            case WHEAT_SEEDS:
                probability = ((double) wheatGrowth[chunk.getZ()][chunk.getX()]) / 255;
                break;
            case CARROTS:
                probability = ((double) carrotGrowth[chunk.getZ()][chunk.getX()]) / 255;
                break;
            case POTATO:
                probability = ((double) potatoGrowth[chunk.getZ()][chunk.getX()]) / 255;
                break;
            case BEETROOT_SEEDS:
                probability = ((double) beetrootGrowth[chunk.getZ()][chunk.getX()]) / 255;
                break;
        }

        if (rng.nextDouble() > probability) {
            //Bukkit.broadcastMessage(String.format("%s at (%.0f,%.0f) failed to grow. It had a %.2f%% chance.",type.name(),loc.getX(),loc.getY(),probability * 100));
            bge.setCancelled(true);
        }
    }

    @EventHandler
    public void checkSoil(PlayerInteractEvent pie) {
        Block block = pie.getClickedBlock();
        if (block != null && block.getType() == Material.FARMLAND && pie.getItem() == null) {
            Player p = pie.getPlayer();
            Chunk chunk = block.getChunk();
            if (chunk.getX() >= 0 && chunk.getX() < mapSize && chunk.getZ() >= 0 && chunk.getZ() < mapSize) {
                double pBeetroot = ((double) beetrootGrowth[chunk.getZ()][chunk.getX()]) / 255 * 100;
                double pWheat = ((double) wheatGrowth[chunk.getZ()][chunk.getX()]) / 255 * 100;
                double pPotato = ((double) potatoGrowth[chunk.getZ()][chunk.getX()]) / 255 * 100;
                double pCarrot = ((double) carrotGrowth[chunk.getZ()][chunk.getX()]) / 255 * 100;
                p.sendMessage(String.format("Wheat: %.1f%%, Potato: %.1f%%, Carrot: %.1f%%, Beetroot: %.1f%%", pWheat, pPotato, pCarrot, pBeetroot));
            } else {
                p.sendMessage("You are outside of the area specified in the localcrops plugin.");
            }
        }
    }
}
