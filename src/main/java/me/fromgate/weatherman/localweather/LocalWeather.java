/*  
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/weatherman/
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

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.WMWorldEdit;
import me.fromgate.weatherman.WeatherMan;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.M;
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
    }

    public static boolean getRain(Player p) {
        int r = PlayerConfig.getPersonalWeather(p);
        if (r < 0) r = getRegionRain(p);
        if (r < 0) r = getBiomeRain(p.getLocation().getBlock().getBiome());
        if (r == 0) return false;
        if (r == 1) return true;
        return getWorldRain(p.getWorld());
    }

    public static boolean getRain(Player p, boolean world_to_rain) {
        int r = PlayerConfig.getPersonalWeather(p);
        if (r < 0) r = getRegionRain(p);
        if (r < 0) r = getBiomeRain(p.getLocation().getBlock().getBiome());
        if (r == 0) return false;
        if (r == 1) return true;
        return world_to_rain;
    }

    public static boolean getRain(Location loc) {
        int r = getRegionRain(loc);
        if (r < 0) r = getBiomeRain(loc.getBlock().getBiome());
        if (r == 0) return false;
        if (r == 1) return true;
        return getWorldRain(loc.getWorld());
    }

    public static void sendWeather(Player player, boolean rain) {
        WeatherType newPlayerWeather = rain ? WeatherType.DOWNFALL : WeatherType.CLEAR;
        if (player.getPlayerWeather() != newPlayerWeather) {
            player.sendMessage(newPlayerWeather.name());
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
        setBiomeRain(BiomeTools.biome2Str(biome), rain);
    }

    public static void setBiomeRain(String biome, boolean rain) {
        biomes.put(biome, rain);
        saveLocalWeather();
    }

    public static void clearBiomeRain(Biome biome) {
        clearBiomeRain(BiomeTools.biome2Str(biome));
    }

    public static void clearBiomeRain(String biome) {
        if (biomes.containsKey(biome)) biomes.remove(biome);
        saveLocalWeather();
    }

    //0 - clear, 1 - rain, -1 - error/default
    public static int getBiomeRain(Biome biome) {
        if (biome == null) return -1;
        return getBiomeRain(BiomeTools.biome2Str(biome));
    }

    public static int getBiomeRain(String biome) {
        if (!biomes.containsKey(biome)) return -1;
        if (biomes.get(biome)) return 1;
        return 0;
    }

    /*
     * Regions
     */
    //0 - clear, 1 - rain, -1 - error/default
    public static int getRegionRain(Player p) {
        return getRegionRain(p.getLocation());
    }


    public static int getRegionRain(Location loc) {
        List<String> rgList = WMWorldEdit.getRegions(loc);
        for (String rgStr : rgList)
            if (regions.containsKey(rgStr)) return (regions.get(rgStr) ? 1 : 0);
        return -1;
    }


    //0 - clear, 1 - rain, -1 - error/default
    public static int getRegionRain(String region) {
        if (!regions.containsKey(region)) return -1;
        if (regions.get(region)) return 1;
        return 0;
    }

    public static void setRegionRain(String region, boolean rain) {
        regions.put(region, rain);
        saveLocalWeather();
    }

    public static void clearRegionRain(String region) {
        if (regions.containsKey(region)) regions.remove(region);
        saveLocalWeather();
    }

    /*
     * World wth
     */
    //0 - clear, 1 - rain, -1 - error/default
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
        if (worlds.containsKey(world)) worlds.remove(world);
        saveLocalWeather();
    }

    public static void updatePlayersRain(final World w, int delay, boolean to_weather) {
        final boolean to_wstate = to_weather;
        Bukkit.getScheduler().runTaskLater(WeatherMan.getPlugin(), new Runnable() {
            public void run() {
                for (Player p : w.getPlayers()) {
                    boolean newrain = getRain(p, to_wstate);
                    sendWeather(p, newrain);
                }
            }
        }, delay);
    }

    public static void updatePlayersRain(final World w, int delay) {
        Bukkit.getScheduler().runTaskLater(WeatherMan.getPlugin(), new Runnable() {
            public void run() {
                for (Player p : w.getPlayers()) {
                    boolean newrain = getRain(p);
                    sendWeather(p, newrain);
                }
            }
        }, delay);
    }

    public static void updatePlayerRain(Player player) {
        boolean newrain = getRain(player);
        if (PlayerConfig.isWeatherChanged(player, newrain)) {
            sendWeather(player, newrain);
        }
    }


    public static void saveLocalWeather() {
        try {
            File f = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "localweather.yml");
            if (f.exists()) {
                f.delete();
            }
            YamlConfiguration cfg = new YamlConfiguration();
            if (worlds.size() > 0) {
                for (String wname : worlds.keySet())
                    cfg.set("worlds." + wname, worlds.get(wname));
            }
            if (biomes.size() > 0) {
                for (String b : biomes.keySet())
                    cfg.set("biomes." + b, biomes.get(b));
            }
            if (regions.size() > 0) {
                for (String r : regions.keySet())
                    cfg.set("regions." + r, regions.get(r));
            }
            cfg.save(f);
        } catch (Exception e) {
        }
    }

    public static void loadLocalWeather() {
        try {
            File f = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "localweather.yml");
            if (f.exists()) {
                YamlConfiguration cfg = new YamlConfiguration();
                cfg.load(f);
                worlds.clear();
                biomes.clear();
                regions.clear();
                for (String key : cfg.getKeys(true)) {
                    if (key.contains(".")) {
                        String[] kln = key.split("\\.");
                        if (kln.length == 2) {
                            String type = kln[0];
                            String keyfield = kln[1];
                            if (type.equalsIgnoreCase("worlds")) worlds.put(keyfield, cfg.getBoolean(key));
                            if (type.equalsIgnoreCase("biomes")) biomes.put(keyfield, cfg.getBoolean(key));
                            if (type.equalsIgnoreCase("regions")) regions.put(keyfield, cfg.getBoolean(key));
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    public static void printPlayerList(CommandSender sender, int page) {
        List<String> plst = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isOnline()) continue;
            int pw = PlayerConfig.getPersonalWeather(player);
            if (pw >= 0)
                plst.add("&6" + player.getName() + "&e : " + ((pw == 1) ? M.RAIN : M.CLEAR).toString());
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
            for (String b : biomes.keySet())
                blst.add("&6" + b + "&e : " + ((biomes.get(b)) ? M.RAIN : M.CLEAR));
            M.printPage(sender, blst, M.WTH_BIOMELIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.WTH_BIOMELISTEMPTY.print(sender);
        }
    }

    public static void printRegionList(CommandSender sender, int page) {
        if (regions.size() > 0) {
            List<String> blst = new ArrayList<String>();
            for (String b : regions.keySet())
                blst.add("&6" + b + "&e : " + ((regions.get(b)) ? M.RAIN : M.CLEAR));
            M.printPage(sender, blst, M.WTH_REGIONLIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.WTH_REGIONLISTEMPTY.print(sender);
        }
    }

    public static void printWorldList(CommandSender sender, int page) {
        if (worlds.size() > 0) {
            List<String> blst = new ArrayList<String>();
            for (String b : worlds.keySet())
                blst.add("&6" + b + "&e : " + ((worlds.get(b)) ? M.RAIN : M.CLEAR));
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
