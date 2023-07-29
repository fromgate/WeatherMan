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

package me.fromgate.weatherman.localtime;

import me.fromgate.weatherman.WeatherMan;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.Time;
import me.fromgate.weatherman.util.WMWorldEdit;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

public class LocalTime {

    private static Map<String, Long> regions;
    private static Map<String, Long> biomes;
    private static Map<String, Long> worlds;

    public static void init() {
        regions = new HashMap<>(); // true  - дождь
        biomes = new HashMap<>();
        worlds = new HashMap<>();
        loadLocalTime();
    }


    public static Long getTime(Player player) {
        Long time = PlayerConfig.getPersonalTime(player);
        if (time == null) {
            time = getRegionTime(player);
        }
        if (time == null) {
            time = getBiomeTime(player.getLocation().getBlock().getBiome());
        }
        return time == null ? getWorldTime(player.getWorld()) : time;
    }

    public static Long getTime(Player player, long worldTime) {
        Long time = PlayerConfig.getPersonalTime(player);
        if (time == null) {
            time = getRegionTime(player);
        }
        if (time == null) {
            time = getBiomeTime(player.getLocation().getBlock().getBiome());
        }
        return time == null ? worldTime : time;
    }

    public static Long getTime(Location loc) {
        Long time = getRegionTime(loc);
        if (time == null) {
            time = getBiomeTime(loc.getBlock().getBiome());
        }
        return time == null ? getWorldTime(loc.getWorld()) : time;
    }

    public static void sendTime(Player player, Long time) {
        if (time == null) {
            player.resetPlayerTime();
        } else {
            player.setPlayerTime(time, false);
        }
    }

    /*
     * Player
     */
    public static void setPlayerTime(Player player, Long time) {
        PlayerConfig.setPersonalTime(player.getName(), time);
        updatePlayerTime(player);
    }

    public static void setPlayerTime(String playerName, Long time) {
        PlayerConfig.setPersonalTime(playerName, time);
        updatePlayerTime(playerName);
    }

    public static void clearPlayerTime(Player player) {
        PlayerConfig.setPersonalTime(player.getName(), (Long) null);
        updatePlayerTime(player);
    }

    public static void clearPlayerTime(String playerName) {
        PlayerConfig.setPersonalTime(playerName, (Long) null);
        updatePlayerTime(playerName);
    }

    /*
     * Biome Weather
     */
    public static void setBiomeTime(Biome biome, Long time) {
        setBiomeTime(BiomeTools.biomeToString(biome), time);
    }

    public static void setBiomeTime(String biome, Long time) {
        biomes.put(biome, time);
        saveLocalTime();
    }

    public static void clearBiomeTime(Biome biome) {
        clearBiomeTime(BiomeTools.biomeToString(biome));
    }

    public static void clearBiomeTime(String biome) {
        biomes.remove(biome);
        saveLocalTime();
    }

    public static Long getBiomeTime(Biome biome) {
        if (biome == null) return null;
        return getBiomeTime(BiomeTools.biomeToString(biome));
    }

    public static Long getBiomeTime(String biome) {
        if (biomes.containsKey(biome)) {
            return biomes.get(biome);
        }
        return null;
    }

    /*
     * Regions
     */
    public static Long getRegionTime(Player p) {
        return getRegionTime(p.getLocation());
    }


    public static Long getRegionTime(Location loc) {
        List<String> rgList = WMWorldEdit.getRegions(loc);
        for (String rgStr : rgList) {
            if (regions.containsKey(rgStr)) {
                return regions.get(rgStr);
            }
        }
        return null;
    }


    public static Long getRegionTime(String region) {
        if (regions.containsKey(region)) {
            regions.get(region);
        }
        return null;
    }

    public static void setRegionTime(String region, Long time) {
        regions.put(region, time);
        saveLocalTime();
    }

    public static void clearRegionTime(String region) {
        regions.remove(region);
        saveLocalTime();
    }

    public static void clearWorldTime(String worldName) {
        worlds.remove(worldName);
        saveLocalTime();
    }

    /*
     * World time
     */
    public static Long getWorldTime(String world) {
        World w = Bukkit.getWorld(world);
        if (w == null) {
            w = Bukkit.getWorlds().get(0); // if given wrong world, will use first world. Not good solution, but better than NPE
        }
        return getWorldTime(w);
    }

    public static Long getWorldTime(World world) {
        return worlds.getOrDefault(world.getName(), null);
    }


    public static void setWorldTime(String worldName, Long time) {
        if (worldName == null || worldName.isEmpty()) return;
        if (time == null) {
            worlds.remove(worldName);
        } else {
            worlds.put(worldName, time);
        }
        saveLocalTime();
    }

    private static boolean isTimeChanged(Player player, Long newTime) {
        return player.getPlayerTime() != (newTime == null ? player.getWorld().getTime() : newTime);
    }


    public static void saveLocalTime() {
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            for (String wname : worlds.keySet()) {
                cfg.set("worlds." + wname, worlds.get(wname));
            }
            for (String b : biomes.keySet()) {
                cfg.set("biomes." + b, biomes.get(b));
            }
            for (String r : regions.keySet()) {
                cfg.set("regions." + r, regions.get(r));
            }
            cfg.save(new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "localtime.yml"));
        } catch (Exception ignored) {
        }
    }

    public static void loadLocalTime() {
        try {
            File f = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "localtime.yml");
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
                            if (type.equalsIgnoreCase("worlds")) worlds.put(keyfield, cfg.getLong(key));
                            if (type.equalsIgnoreCase("biomes")) biomes.put(keyfield, cfg.getLong(key));
                            if (type.equalsIgnoreCase("regions")) regions.put(keyfield, cfg.getLong(key));
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
            String pw = PlayerConfig.getPersonalTimeStr(player);
            if (!pw.equalsIgnoreCase("UNDEFINED")) {
                plst.add("&6" + player.getName() + "&e : " + pw);
            }
        }
        if (plst.size() > 0) {
            M.printPage(sender, plst, M.TM_PLAYERLIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.TM_PLAYERLISTEMPTY.print(sender);
        }
    }

    public static void printBiomeList(CommandSender sender, int page) {
        if (biomes.size() > 0) {
            List<String> blst = new ArrayList<>();
            for (String b : biomes.keySet()) {
                blst.add("&6" + b + "&e : " + Time.timeToString(biomes.get(b)));
            }
            M.printPage(sender, blst, M.TM_BIOMELIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.TM_BIOMELISTEMPTY.print(sender);
        }
    }

    public static void printRegionList(CommandSender sender, int page) {
        if (regions.size() > 0) {
            List<String> blst = new ArrayList<>();
            for (String b : regions.keySet()) {
                blst.add("&6" + b + "&e : " + Time.timeToString(regions.get(b)));
            }
            M.printPage(sender, blst, M.TM_REGIONLIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.TM_REGIONLISTEMPTY.print(sender);
        }
    }

    public static void printWorldList(CommandSender sender, int page) {
        if (worlds.size() > 0) {
            List<String> blst = new ArrayList<>();
            for (String b : worlds.keySet()) {
                blst.add("&6" + b + "&e : " + Time.timeToString(worlds.get(b)));
            }
            M.printPage(sender, blst, M.TM_WORLDLIST, page, sender instanceof Player ? 9 : 1000);
        } else {
            M.TM_WORLDLISTEMPTY.print(sender);
        }
    }

    public static boolean getWorldWeather(World world) {
        return worlds.containsKey(world.getName());
    }

    public static boolean isWorldWeatherSet(World world) {
        return worlds.containsKey(world.getName());
    }


    @SuppressWarnings("deprecation")
    public static void updatePlayerTime(String playerName) {
        if (!Cfg.localTimeEnable) return;
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) return;
        Long time = getTime(player);
        if (isTimeChanged(player, time)) {
            sendTime(player, time);
        }
    }

    public static void updatePlayerTime(Player player) {
        if (!Cfg.localTimeEnable) return;
        Long time = getTime(player);
        if (isTimeChanged(player, time)) {
            sendTime(player, time);
        }
    }

    public static void updatePlayerTime(World world) {
        if (!Cfg.localTimeEnable) return;
        if (world != null) {
            world.getPlayers().forEach(LocalTime::updatePlayerTime);
        }
    }

    public static void updateAllPlayersTime() {
        if (!Cfg.localTimeEnable) return;
        Bukkit.getOnlinePlayers().forEach(LocalTime::updatePlayerTime);
    }

}
