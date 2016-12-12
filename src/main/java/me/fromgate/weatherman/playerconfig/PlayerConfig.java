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

package me.fromgate.weatherman.playerconfig;

import me.fromgate.weatherman.util.*;
import me.fromgate.weatherman.WeatherMan;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerConfig {

    private static WeatherMan plg;

    public static void init(WeatherMan plugin) {
        plg = plugin;
    }


    //HashMap<String, Cfg> pcfg = new HashMap<String, Cfg>();  //TODO перевести все на Metadata


    //Common methods
    public static void setMeta(Player player, String key, String value) {
        player.setMetadata(key, new FixedMetadataValue(plg, value));
    }

    public static String getMeta(Player player, String key, String defaultValue) {
        if (!player.hasMetadata(key)) return defaultValue;
        if (player.getMetadata(key).isEmpty()) return defaultValue;
        return player.getMetadata(key).get(0).asString();

    }

    public static String getMeta(Player player, String key) {
        return getMeta(player, key, "");
    }

    public static boolean isMetaExists(Player player, String key) {
        if (!player.hasMetadata(key)) return false;
        if (player.getMetadata(key).isEmpty()) return false;
        return true;
    }


    // Personal configuration
    // Tree wand
    public static void setTree(Player player, String treeStr) {
        setMeta(player, "wm.pcfg-tree", Forester.isTreeExists(treeStr, true) ? treeStr : "default");
    }

    public static String getTree(Player player) {
        return getMeta(player, "wm.pcfg-tree", "default");
    }


    //Biome ball
    public static BiomeBall getBiomeBall(Player player) {
        String biomeStr = getMeta(player, "wm.pcfg-biome", "");
        Biome biome = BiomeTools.isBiomeExists(biomeStr) ? BiomeTools.str2Biome(biomeStr) : Cfg.getDefaultBiome();
        String radiusStr = getMeta(player, "wm.pcfg-radius");
        int radius = Util.isIntegerGZ(radiusStr) ? Integer.parseInt(radiusStr) : Cfg.getDefaultRadius();
        return new BiomeBall(biome, radius);
    }

    public static void setBiomeBallCfg(Player player, String biomeStr, int radius) {
        setMeta(player, "wm.pcfg-biome", BiomeTools.isBiomeExists(biomeStr) ? biomeStr : BiomeTools.biome2Str(Cfg.getDefaultBiome()));
        setMeta(player, "wm.pcfg-radius", Integer.toString(radius));
    }


    public static void clearPlayerConfig(Player p) {
        if (p.hasMetadata("wm.infomode")) p.removeMetadata("wm.infomode", plg);
        if (p.hasMetadata("wm.wandmode")) p.removeMetadata("wm.wandmode", plg);
        if (p.hasMetadata("wm.last-wth")) p.removeMetadata("wm.last-wth", plg);
        if (p.hasMetadata("wm.personal-wth")) p.removeMetadata("wm.personal-wth", plg);
    }

	/*
     * Local Weather
	 */

    public static boolean getLastWeather(Player p) {
        if (!p.hasMetadata("wm.last-wth")) return p.getWorld().hasStorm();
        if (p.getMetadata("wm.last-wth").isEmpty()) {
            p.removeMetadata("wm.last-wth", plg);
            return p.getWorld().hasStorm();
        }
        return p.getMetadata("wm.last-wth").get(0).asBoolean();
    }

    public static void setLastWeather(Player p, boolean rain) {
        p.setMetadata("wm.last-wth", new FixedMetadataValue(plg, rain));
    }

    public static boolean isWeatherChanged(Player p, boolean newrain) {
        boolean lastrain = getLastWeather(p);
        setLastWeather(p, newrain);
        return (newrain != lastrain);
    }

    /*
     * Personal Weather
     */
    public static int getPersonalWeather(Player p) {
        if (!p.hasMetadata("wm.personal-wth")) return -1;
        if (p.getMetadata("wm.personal-wth").size() == 0) {
            p.removeMetadata("wm.personal-wth", plg);
            return -1;
        }
        if (p.getMetadata("wm.personal-wth").get(0).asBoolean()) return 1;
        return 0;
    }

    public static void setPersonalWeather(Player p, boolean rain) {
        p.setMetadata("wm.personal-wth", new FixedMetadataValue(plg, rain));
    }

    public static void removePersonalWeather(Player p) {
        if (p.hasMetadata("wm.personal-wth")) p.removeMetadata("wm.personal-wth", plg);
    }

    /*
     *  WalkInfo mode
     */
    public static boolean isWalkInfoMode(Player p) {
        if (!p.hasMetadata("wm.infomode")) return false;
        if (p.getMetadata("wm.infomode").isEmpty()) return false;
        return p.getMetadata("wm.infomode").get(0).asBoolean();
    }

    public static void setWalkInfoMode(Player p, boolean mode) {
        p.setMetadata("wm.infomode", new FixedMetadataValue(plg, mode));
    }

    public static void toggleWalkInfoMode(Player p) {
        p.setMetadata("wm.infomode", new FixedMetadataValue(plg, !isWalkInfoMode(p)));
    }

    /*
     *  Wand mode
     */
    public static boolean isWandMode(Player p) {
        if (!p.hasMetadata("wm.wandmode")) return false;
        if (p.getMetadata("wm.wandmode").isEmpty()) return false;
        return p.getMetadata("wm.wandmode").get(0).asBoolean();
    }

    public static void setWandMode(Player p, boolean mode) {
        p.setMetadata("wm.wandmode", new FixedMetadataValue(plg, mode));
    }

    public static void toggleWandMode(Player p) {
        p.setMetadata("wm.wandmode", new FixedMetadataValue(plg, !isWandMode(p)));
    }


}
