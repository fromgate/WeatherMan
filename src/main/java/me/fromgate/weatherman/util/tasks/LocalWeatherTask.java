package me.fromgate.weatherman.util.tasks;

import me.fromgate.weatherman.localtime.LocalTime;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class LocalWeatherTask extends BukkitRunnable {

    private static final Map<String, Location> prevLocations = new HashMap<>();

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            String name = player.getName();
            Location loc = player.getLocation();
            Location prevLoc = prevLocations.getOrDefault(name, null);
            if (prevLoc == null || !Util.isSameBlocks(loc, prevLoc)) {
                LocalWeather.updatePlayerRain(player);
                LocalTime.updatePlayerTime(player);
            }
            prevLocations.put(name, loc);
        });
    }

    public static void removePrevLocation(Player player) {
        prevLocations.remove(player.getName());
    }
}
