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

package me.fromgate.weatherman.playerconfig;

import me.fromgate.weatherman.WeatherMan;
import me.fromgate.weatherman.localweather.WeatherState;
import me.fromgate.weatherman.util.BiomeBall;
import me.fromgate.weatherman.util.Cfg;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class PlayerConfig {

    private static WeatherMan plg;
    private static File folder;

    public static void init(WeatherMan plugin) {
        plg = plugin;
        folder = new File(plugin.getDataFolder() + File.separator + "players" + File.separator);
        folder.mkdirs();
    }

    private static final Map<String, PlayerData> players = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    // Personal configuration
    // Tree wand
    public static void setTree(Player player, String treeStr) {
        getPlayerData(player).setBallTree(treeStr);
        savePlayerData(player);
    }

    public static String getTree(Player player) {
        return getPlayerData(player).getBallTree();
    }


    //Biome ball
    public static BiomeBall getBiomeBall(Player player) {
        PlayerData data = getPlayerData(player);
        Biome biome = data.getBallBiome();
        int radius = data.getBallRadius();
        return new BiomeBall(biome, radius);
    }

    public static void setBiomeBallCfg(Player player, String biomeStr, int radius) {
        PlayerData data = getPlayerData(player);
        data.setBallBiome(biomeStr);
        data.setBallRadius(radius);
        savePlayerData(player);
    }


    public static void clearPlayerConfig(Player player) {
        PlayerData data = getPlayerData(player);
        data.setStateInfoMode(false);
        data.setStateWand(false);
        data.setStateLastWeather(false);
        savePlayerData(player);
    }

	/*
     * Local Weather
	 */

    public static boolean getLastWeather(Player player) {
        return getPlayerData(player).isStateLastWeather();
    }

    public static void setLastWeather(Player player, boolean rain) {
        getPlayerData(player).setStateLastWeather(rain);
        savePlayerData(player);
    }

    public static boolean isWeatherChanged(Player player, boolean newRain) {
        boolean lastrain = getLastWeather(player);
        setLastWeather(player, newRain);
        return (newRain != lastrain);
    }

    /*
     * Personal Weather
     */
    public static WeatherState getPersonalWeather(Player player) {
        return getPlayerData(player).getPersonalWeather();
    }

    public static void setPersonalWeather(Player player, boolean rain) {
        getPlayerData(player).setPersonalWeather(rain);
        savePlayerData(player);
    }

    public static void removePersonalWeather(Player player) {
        getPlayerData(player).setPersonalWeather(WeatherState.UNSET);
        savePlayerData(player);
    }

    /*
     * Personal time
     */
    public static Long getPersonalTime(String player) {
        return getPlayerData(player).getPersonalTime();
    }

    public static Long getPersonalTime(Player player) {
        return getPlayerData(player).getPersonalTime();
    }

    public static void setPersonalTime(String player, Long time) {
        getPlayerData(player).setPersonalTime(time);
        savePlayerData(player);
    }

    public static void setPersonalTime(String player, String time) {
        getPlayerData(player).setPersonalTime(time);
        savePlayerData(player);
    }

    public static void removePersonalTime(String player) {
        getPlayerData(player).setPersonalTime((Long) null);
        savePlayerData(player);
    }


    /*
     *  WalkInfo mode
     */
    public static boolean isWalkInfoMode(Player player) {
        return getPlayerData(player).isStateInfoMode();
    }

    public static void setWalkInfoMode(Player player, boolean mode) {
        getPlayerData(player).setStateInfoMode(mode);
        savePlayerData(player);
    }

    public static void toggleWalkInfoMode(Player player) {
        PlayerData data = getPlayerData(player);
        data.setStateInfoMode(!data.isStateInfoMode());
        savePlayerData(player);
    }

    /*
     *  Wand mode
     */
    public static boolean isWandMode(Player player) {
        return getPlayerData(player).isStateWand();
    }

    public static void setWandMode(Player player, boolean mode) {
        getPlayerData(player).setStateWand(mode);
    }

    public static void toggleWandMode(Player player) {
        PlayerData data = getPlayerData(player);
        data.setStateWand(!data.isStateWand());
        savePlayerData(player);
    }


    public static PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getName());
    }

    public static PlayerData getPlayerData(String name) {
        if (players.containsKey(name)) {
            return players.get(name);
        }
        PlayerData data = new PlayerData();
        YamlConfiguration cfg = new YamlConfiguration();
        File file = new File(folder, name + ".yml");
        if (file.exists()) {
            try {
                cfg.load(file);
                if (Cfg.personalBrushClear) {
                    data.setBallRadius(cfg.getInt("brush.radius", data.getBallRadius()));
                    data.setBallBiome(cfg.getString("brush.biome"));
                    data.setBallTree(cfg.getString("brush.tree", data.getBallTree()));
                }

                if (Cfg.personalWeatherClear) {
                    data.setPersonalWeather(cfg.getString("personal.weather"));
                }

                if (Cfg.personalTimeClear) {
                    data.setPersonalTime(cfg.getString("personal.time", data.getPersonalTimeStr()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        players.put(name, data);
        return data;
    }

    public static void savePlayerData(Player player) {
        savePlayerData(player.getName());
    }

    public static void savePlayerData(String name) {
        PlayerData data = players.containsKey(name) ? data = players.get(name) : null;
        if (data == null) return;
        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("brush.radius", data.getBallRadius());
        cfg.set("brush.biome", data.getBallBiome().name());
        cfg.set("brush.tree", data.getBallTree());
        cfg.set("personal.weather", data.personalWeather.name());
        cfg.set("personal.time", data.getPersonalTimeStr());
        try {
            cfg.save(new File(folder, name + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPersonalTimeStr(Player player) {
        return getPlayerData(player).getPersonalTimeStr();
    }


    public static void quitPlayer(Player player) {
        players.remove(player.getName());
    }
}
