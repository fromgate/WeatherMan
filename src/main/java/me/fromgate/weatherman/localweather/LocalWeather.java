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


package me.fromgate.weatherman.localweather;

import me.fromgate.weatherman.WeatherMan;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.WMWorldEdit;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalWeather {
    private static Map<String, Boolean> regions; // true  - дождь
    private static Map<String, Boolean> biomes;
    private static Map<String, Boolean> worlds;


    public static void init() {
        regions = new HashMap<>(); // true  - дождь
        biomes = new HashMap<>();
        worlds = new HashMap<>();
        loadLocalWeather();
    }

    public static boolean getRain(Player p) {
        WeatherState r = PlayerConfig.getPersonalWeather(p);
        if (r == WeatherState.UNSET) {
            r = getRegionRain(p);
        }
        if (r == WeatherState.UNSET) {
            r = getBiomeRain(p.getLocation().getBlock().getBiome());
        }
        if (r == WeatherState.CLEAR) {
            return false;
        }
        if (r == WeatherState.RAIN) {
            return true;
        }
        return getWorldRain(p.getWorld());
    }

    public static boolean getRain(Player p, boolean world_to_rain) {
        WeatherState r = PlayerConfig.getPersonalWeather(p);
        if (r == WeatherState.UNSET) {
            r = getRegionRain(p);
        }
        if (r == WeatherState.UNSET) {
            r = getBiomeRain(p.getLocation().getBlock().getBiome());
        }
        if (r == WeatherState.CLEAR) {
            return false;
        }
        if (r == WeatherState.RAIN) {
            return true;
        }
        return world_to_rain;
    }

    public static boolean getRain(Location loc) {
        WeatherState r = getRegionRain(loc);
        if (r == WeatherState.UNSET) {
            r = getBiomeRain(loc.getBlock().getBiome());
        }
        if (r == WeatherState.CLEAR) {
            return false;
        }
        if (r == WeatherState.RAIN) {
            return true;
        }
        return getWorldRain(loc.getWorld());
    }

    public static void sendWeather(Player player, boolean rain) {
        WeatherType newPlayerWeather = rain ? WeatherType.DOWNFALL : WeatherType.CLEAR;
        if (player.getPlayerWeather() != newPlayerWeather) {
            player.setPlayerWeather(newPlayerWeather);
        }
    }

    /*
     * Player
     */
    public static void setPlayerRain(Player player, boolean rain) {
        if (PlayerConfig.isWeatherChanged(player, rain)) sendWeather(player, rain);
        PlayerConfig.setPersonalWeather(player, rain);
    }

    public static void clearPlayerRain(Player player) {
        PlayerConfig.removePersonalWeather(player);
        sendWeather(player, getRain(player));
    }

    /*
     * Biome Weather
     */
    public static void setBiomeRain(Biome biome, boolean rain) {
        setBiomeRain(BiomeTools.biomeToString(biome), rain);
    }

    public static void setBiomeRain(String biome, boolean rain) {
        biomes.put(biome, rain);
        saveLocalWeather();
    }

    public static void clearBiomeRain(Biome biome) {
        clearBiomeRain(BiomeTools.biomeToString(biome));
    }

    public static void clearBiomeRain(String biome) {
        biomes.remove(biome);
        saveLocalWeather();
    }

    //0 - clear, 1 - rain, -1 - error/default
    public static WeatherState getBiomeRain(Biome biome) {
        if (biome == null) return WeatherState.UNSET;
        return getBiomeRain(BiomeTools.biomeToString(biome));
    }

    public static WeatherState getBiomeRain(String biome) {
        if (!biomes.containsKey(biome)) return WeatherState.UNSET;
        if (biomes.get(biome)) return WeatherState.RAIN;
        return WeatherState.CLEAR;
    }

    /*
     * Regions
     */
    //0 - clear, 1 - rain, -1 - error/default
    public static WeatherState getRegionRain(Player p) {
        return getRegionRain(p.getLocation());
    }


    public static WeatherState getRegionRain(Location loc) {
        List<String> rgList = WMWorldEdit.getRegions(loc);
        for (String rgStr : rgList) {
            if (regions.containsKey(rgStr)) {
                return (regions.get(rgStr) ? WeatherState.RAIN : WeatherState.CLEAR);
            }
        }
        return WeatherState.UNSET;
    }


    public static WeatherState getRegionRain(String region) {
        if (!regions.containsKey(region)) return WeatherState.UNSET;
        if (regions.get(region)) return WeatherState.RAIN;
        return WeatherState.CLEAR;
    }

    public static void setRegionRain(String region, boolean rain) {
        regions.put(region, rain);
        saveLocalWeather();
    }

    public static void clearRegionRain(String region) {
        regions.remove(region);
        saveLocalWeather();
    }

    /*
     * World wth
     */
    public static boolean getWorldRain(String world) {
        World w = Bukkit.getWorld(world);
        if (w == null) {
            w = Bukkit.getWorlds().get(0); // if given wrong world, will use first world. Not good solution, but better than NPE
        }
        return getWorldRain(w);
    }

    public static boolean getWorldRain(World world) {
        String w = world.getName();
        if (worlds.containsKey(w)) return worlds.get(w);
        else return world.hasStorm();
    }

    public static void setWorldRain(World world, boolean rain) {
        setWorldRain(world.getName(), rain);
        world.setStorm(rain);
    }

    public static void setWorldRain(String world, boolean rain) {
        worlds.put(world, rain);
        saveLocalWeather();
    }

    public static void clearWorldRain(World world) {
        clearWorldRain(world.getName());
        updatePlayersRain(world, 10);
    }

    public static void clearWorldRain(String world) {
        worlds.remove(world);
        saveLocalWeather();
    }

    public static void updatePlayersRain(final World w, int delay, boolean toWeather) {
        final boolean toWstate = toWeather;
        Bukkit.getScheduler().runTaskLater(WeatherMan.getPlugin(), () -> {
            for (Player p : w.getPlayers()) {
                boolean newrain = getRain(p, toWstate);
                sendWeather(p, newrain);
            }
        }, delay);
    }

    public static void updatePlayersRain(final World world, int delay) {
        Bukkit.getScheduler().runTaskLater(WeatherMan.getPlugin(), () -> {
            for (Player player : world.getPlayers()) {
                boolean newrain = getRain(player);
                sendWeather(player, newrain);
            }
        }, delay);
    }

    public static void updatePlayerRain(Player player) {
        if (!Cfg.localWeatherEnable) return;
        boolean newRain = getRain(player);
        if (PlayerConfig.isWeatherChanged(player, newRain)) {
            sendWeather(player, newRain);
        }
    }


    public static void saveLocalWeather() {
        try {
            File file = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "localweather.yml");
            if (file.exists()) {
                file.delete();
            }
            YamlConfiguration cfg = new YamlConfiguration();
            if (worlds.size() > 0) {
                for (String worldName : worlds.keySet())
                    cfg.set("worlds." + worldName, worlds.get(worldName));
            }
            if (biomes.size() > 0) {
                for (String biomeName : biomes.keySet())
                    cfg.set("biomes." + biomeName, biomes.get(biomeName));
            }
            if (regions.size() > 0) {
                for (String regionName : regions.keySet())
                    cfg.set("regions." + regionName, regions.get(regionName));
            }
            cfg.save(file);
        } catch (Exception ignored) {
        }
    }

    public static void loadLocalWeather() {
        try {
            File file = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "localweather.yml");
            if (file.exists()) {
                YamlConfiguration cfg = new YamlConfiguration();
                cfg.load(file);
                worlds.clear();
                biomes.clear();
                regions.clear();
                for (String key : cfg.getKeys(true)) {
                    if (key.contains(".")) {
                        String[] kln = key.split("\\.");
                        if (kln.length == 2) {
                            String type = kln[0];
                            String keyfield = kln[1];
                            switch (type.toLowerCase()) {
                                case "worlds":
                                    worlds.put(keyfield, cfg.getBoolean(key));
                                    break;
                                case "biomes":
                                    biomes.put(keyfield, cfg.getBoolean(key));
                                    break;
                                case "regions":
                                    regions.put(keyfield, cfg.getBoolean(key));
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

    }

    public static void printPlayerList(CommandSender sender, int page) {
        List<String> plst = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isOnline()) continue;
            WeatherState pw = PlayerConfig.getPersonalWeather(player);
            if (pw != WeatherState.UNSET) {
                plst.add("&6" + player.getName() + "&e : " + (pw == WeatherState.CLEAR ? M.CLEAR : M.RAIN));
            }
        }
        if (plst.size() > 0) {
            M.printPage(sender, plst, M.WTH_PLAYERLIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.WTH_PLAYERLISTEMPTY.print(sender);
        }
    }

    public static void printBiomeList(CommandSender sender, int page) {
        if (biomes.size() > 0) {
            List<String> blst = new ArrayList<>();
            for (String b : biomes.keySet()) {
                blst.add("&6" + b + "&e : " + ((biomes.get(b)) ? M.RAIN : M.CLEAR));
            }
            M.printPage(sender, blst, M.WTH_BIOMELIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.WTH_BIOMELISTEMPTY.print(sender);
        }
    }

    public static void printRegionList(CommandSender sender, int page) {
        if (regions.size() > 0) {
            List<String> blst = new ArrayList<>();
            for (String b : regions.keySet()) {
                blst.add("&6" + b + "&e : " + ((regions.get(b)) ? M.RAIN : M.CLEAR));
            }
            M.printPage(sender, blst, M.WTH_REGIONLIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.WTH_REGIONLISTEMPTY.print(sender);
        }
    }

    public static void printWorldList(CommandSender sender, int page) {
        if (worlds.size() > 0) {
            List<String> blst = new ArrayList<>();
            for (String b : worlds.keySet()) {
                blst.add("&6" + b + "&e : " + ((worlds.get(b)) ? M.RAIN : M.CLEAR));
            }
            M.printPage(sender, blst, M.WTH_WORLDLIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.WTH_WORLDLISTEMPTY.print(sender);
        }
    }

    public static boolean getWorldWeather(World world) {
        return worlds.containsKey(world.getName());
    }

    public static boolean isWorldWeatherSet(World world) {
        return worlds.containsKey(world.getName());
    }

}
