/*
 *  WeatherMan, Minecraft bukkit plugin
 *  ©2012-2018, fromgate, fromgate@gmail.com
 *  https://www.spigotmc.org/resources/weatherman.43379/
 *
 *  This file is part of WeatherMan.
 *
 *  WeatherMan is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WeatherMan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WeatherMan.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package me.fromgate.weatherman.util;


import me.fromgate.weatherman.WeatherMan;
import me.fromgate.weatherman.localtime.LocalTime;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.lang.M;
import me.fromgate.weatherman.util.tasks.InfoTask;
import me.fromgate.weatherman.util.tasks.LocalWeatherTask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WMListener implements Listener {
    WeatherMan plg;

    public WMListener(WeatherMan plg) {
        this.plg = plg;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockForm(BlockFormEvent event) {
        if ((event.getNewState().getType() == Material.SNOW) && (!Cfg.unsnowBiomes.isEmpty()) && (Util.isWordInList(BiomeTools.biomeToString(event.getBlock().getBiome()), Cfg.unsnowBiomes))) {
            event.setCancelled(true);
        }

        if ((event.getNewState().getType() == Material.ICE) && (!Cfg.uniceBiomes.isEmpty()) && (Util.isWordInList(BiomeTools.biomeToString(event.getBlock().getBiome()), Cfg.uniceBiomes))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        PlayerConfig.clearPlayerConfig(p);
        if (PlayerConfig.isWandMode(p)) {
            PlayerConfig.setWandMode(p, false);
            M.MSG_WANDMODEDISABLED.print(p, M.enDis(false), "&6/wm wand&a");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (Cfg.netherMob) return;
        if (event.getEntity().getWorld().getEnvironment() == Environment.NETHER) return;
        if (event.getSpawnReason() != SpawnReason.NATURAL) return;
        if ((event.getEntityType() == EntityType.ZOMBIFIED_PIGLIN) ||
                (event.getEntityType() == EntityType.MAGMA_CUBE) ||
                (event.getEntityType() == EntityType.GHAST))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
        if (PlayerConfig.isWandMode(player)) {
            Brush.shootWand(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.SNOWBALL) return;
        Brush.processSnowball((Snowball) event.getEntity());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        Player p = event.getPlayer();
        if (ChatColor.stripColor(event.getLine(1)).equalsIgnoreCase("[biome]"))
            if (!p.hasPermission("wm.sign")) event.setLine(1, "{biome}");
            else {
                if (!BiomeTools.isBiomeExists(event.getLine(0)))
                    event.setLine(0, BiomeTools.biomeToString(event.getBlock().getBiome()));
                event.setLine(0, ChatColor.GREEN + event.getLine(0));
                event.setLine(1, ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "[Biome]");
                String l2 = event.getLine(2);
                if (l2.equalsIgnoreCase("replace")) event.setLine(2, ChatColor.BLUE + "Replace");
                else if (l2.matches("[1-9]+[0-9]*")) event.setLine(2, ChatColor.BLUE + "radius=" + l2);
                else if ((l2.toLowerCase().startsWith("radius=")) && (l2.toLowerCase().replace("radius=", "").matches("[1-9]+[0-9]*")))
                    event.setLine(2, ChatColor.BLUE + "radius=" + l2.toLowerCase().replace("radius=", ""));
                else if (l2.isEmpty())
                    event.setLine(2, ChatColor.BLUE + "radius=" + Cfg.defaultRadius);
                else {
                    event.setLine(2, ChatColor.BLUE + l2);
                    if (WMWorldEdit.isWG()) {
                        if (!WMWorldEdit.isRegionExists(event.getBlock().getWorld(), l2)) {

                            M.WG_UNKNOWNREGION.print(p, l2);
                        }
                    } else {
                        M.WG_NOTFOUND.print(p);
                        event.setLine(2, ChatColor.BLUE + "radius=" + Cfg.defaultRadius);
                    }
                }
                if (event.getLine(3).isEmpty() ||
                        ((!event.getLine(3).isEmpty()) && (!BiomeTools.isBiomeExists(event.getLine(3)))))
                    event.setLine(3, BiomeTools.biomeToString(Cfg.defaultBiome));
                event.setLine(3, ChatColor.RED + event.getLine(3));
            }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        Block b = event.getBlock();
        if ((b.getType() == Material.OAK_SIGN) || (b.getType() == Material.OAK_WALL_SIGN)) {
            BlockState state = b.getState();
            if (state instanceof Sign) {
                Sign sign = (Sign) state;

                if (!ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[biome]")) {
                } else if ((!sign.getLine(0).isEmpty()) &&
                        (!sign.getLine(2).isEmpty()) &&
                        (!sign.getLine(3).isEmpty())) {
                    String b1 = ChatColor.stripColor(sign.getLine(0));
                    String b2 = ChatColor.stripColor(sign.getLine(3));

                    if (BiomeTools.isBiomeExists(b1) && BiomeTools.isBiomeExists(b2)) {

                        int radius = -1;
                        Location loc1 = null;
                        Location loc2 = null;

                        String rs = ChatColor.stripColor(sign.getLine(2)).toLowerCase();

                        int mode = -1;
                        if (rs.startsWith("radius=")) {
                            rs = rs.replace("radius=", "");
                            if (rs.matches("[1-9]+[0-9]*")) {
                                radius = Math.min(Integer.parseInt(rs), Cfg.maxRadiusSign);
                                mode = 0;
                            }
                        } else if (rs.equalsIgnoreCase("replace")) mode = 1;
                        else {
                            World w = event.getBlock().getWorld();
                            if (WMWorldEdit.isRegionExists(w, rs)) {
                                loc1 = WMWorldEdit.getMinPoint(w, rs);
                                loc1.setY(0);
                                loc2 = WMWorldEdit.getMaxPoint(w, rs);
                                loc2.setY(0);
                                mode = 2;
                            }
                        }

                        if (mode >= 0) {
                            Biome biome = BiomeTools.biomeByName(b1);
                            sign.setLine(0, ChatColor.GREEN + b1);
                            sign.setLine(3, ChatColor.RED + b2);
                            if (b.isBlockIndirectlyPowered()) {
                                biome = BiomeTools.biomeByName(b2);
                                sign.setLine(0, ChatColor.RED + b1);
                                sign.setLine(3, ChatColor.GREEN + b2);
                            }
                            switch (mode) {
                                case 0:
                                    BiomeTools.setBiomeRadius(null, sign.getLocation(), biome, radius, null);
                                    break;
                                case 1:
                                    BiomeTools.floodFill(null, sign.getLocation(), biome);
                                    break;
                                case 2:
                                    BiomeTools.setBiomeArea(null, loc1, loc2, biome, null);
                                    break;
                            }
                            sign.update(true);
                        }
                    }
                } else {
                    M.logMessage("Something wrong with WeatherMan-sign: [" + ChatColor.stripColor(sign.getLine(0)) + "|" + ChatColor.stripColor(sign.getLine(1)) +
                            "|" + ChatColor.stripColor(sign.getLine(2)) + "|" + ChatColor.stripColor(sign.getLine(3)) +
                            "] " + event.getBlock().getLocation());
                }
            }
        }
    }

    // LocalWeather events
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!LocalWeather.isWorldWeatherSet(event.getWorld())) {
            LocalWeather.updatePlayersRain(event.getWorld(), 20, event.toWeatherState());
        } else {
            final boolean worldStorm = LocalWeather.getWorldWeather(event.getWorld());
            if (event.toWeatherState() != worldStorm) event.setCancelled(true);
            else LocalWeather.updatePlayersRain(event.getWorld(), 20, worldStorm);
        }

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoinWeatherTime(PlayerJoinEvent event) {
        LocalWeather.updatePlayerRain(event.getPlayer());
        LocalTime.updatePlayerTime(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        LocalWeather.updatePlayerRain(event.getPlayer());
        LocalTime.updatePlayerTime(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        LocalWeather.updatePlayerRain(event.getPlayer());
        LocalTime.updatePlayerTime(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerConfig.quitPlayer(player);
        InfoTask.removePrevLocation(player);
        LocalWeatherTask.removePrevLocation(player);
    }


    private boolean isSameBlocks(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ();
    }

}
