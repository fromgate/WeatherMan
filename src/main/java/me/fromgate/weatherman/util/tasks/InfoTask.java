package me.fromgate.weatherman.util.tasks;

import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.NmsUtil;
import me.fromgate.weatherman.util.Util;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class InfoTask extends BukkitRunnable {

    private static final Map<String, Location> prevLocations = new HashMap<>();

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            String name = player.getName();
            if (PlayerConfig.isWalkInfoMode(player)) {
                Location prev = prevLocations.getOrDefault(name, null);
                Location playerLoc = player.getLocation();
                if (prev != null && !player.getWorld().equals(prev.getWorld())) {
                    prev = null;
                }
                if (prev == null || !Util.isSameBlocks(playerLoc, prev)) {
                    Biome prevBiome = prev == null ? null : prev.getBlock().getBiome();
                    Biome biome = playerLoc.getBlock().getBiome();
                    if (!biome.equals(prevBiome)) {
                        Biome originalBiome = NmsUtil.getOriginalBiome(playerLoc);
                        if (biome.equals(originalBiome)) {
                            M.MSG_MOVETOBIOME.print(player, BiomeTools.biomeToString(biome));
                        } else {
                            M.MSG_MOVETOBIOME2.print(player, BiomeTools.biomeToString(biome), BiomeTools.biomeToString(originalBiome));
                        }
                    }
                }
                prevLocations.put(name, playerLoc);
            } else prevLocations.remove(name);
        });
    }

    public static void setPrevLocation(Player player) {
        prevLocations.put(player.getName(), player.getLocation());
    }

    public static void removePrevLocation(Player player) {
        prevLocations.remove(player.getName());
    }
}
