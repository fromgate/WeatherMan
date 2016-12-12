package me.fromgate.weatherman.localweather;

import me.fromgate.weatherman.util.M;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;


public class WeatherListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!LocalWeather.isWorldWeatherSet(event.getWorld())) {
            LocalWeather.updatePlayersRain(event.getWorld(), 20, event.toWeatherState());
        } else {
            final boolean worldstorm = LocalWeather.getWorldWeather(event.getWorld());
            if (event.toWeatherState() != worldstorm) event.setCancelled(true);
            else LocalWeather.updatePlayersRain(event.getWorld(), 20, worldstorm);
        }

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getWorld().equals(event.getTo().getWorld()) &&
                event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        M.BC("PlayerMoveEvent");
        LocalWeather.updatePlayerRain(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        LocalWeather.updatePlayerRain(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        LocalWeather.updatePlayerRain(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        LocalWeather.updatePlayerRain(event.getPlayer());
    }
}
