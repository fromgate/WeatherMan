/*
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  https://dev.bukkit.org/projects/weatherman
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
import org.bukkit.block.Biome;

public class Cfg {
    public static String language;
    public static boolean languageSave;
    public static boolean debug;
    public static int defaultRadius;
    public static boolean meltSnow;
    public static boolean meltIce;
    public static Biome defaultBiome;
    public static int maxRadiusCmd;
    public static int maxRadiusWand;
    public static int maxRadiusSign;
    public static boolean netherMob;
    public static String unsnowBiomes;
    public static String uniceBiomes;

    public static boolean localTimeEnable;
    public static boolean localWeatherEnable;
    public static boolean personalTimeClear;
    public static boolean personalWeatherClear;
    public static boolean personalBrushClear;
    public static boolean checkUpdates;

    public static int chunkUpdateMethod = 0; // 0 - default, 1 - experimental1, etc.

    public static void loadCfg() {
        WeatherMan.getPlugin().reloadConfig();
        language = getString("general.language", "english").toLowerCase();
        languageSave = getBoolean("general.language-save", false);
        debug = getBoolean("general.debug", false);
        checkUpdates = getBoolean("general.check-updates", true);
        chunkUpdateMethod = getInt("system.chunk-update-method", 0);
        personalTimeClear = getBoolean("personal.time.reset-on-start", false);
        personalWeatherClear = getBoolean("personal.weather.reset-on-start", false);
        personalBrushClear = getBoolean("personal.brush.reset-on-start", true);
        localTimeEnable = getBoolean("local.time-enable", true);
        localWeatherEnable = getBoolean("local.weather-enable", true);
        maxRadiusCmd = getInt("limits.maximum-command-radius", 250);
        maxRadiusWand = getInt("limits.maximum-wand-radius", 15);
        maxRadiusSign = getInt("limits.maximum-sign-radius", 100);
        netherMob = getBoolean("biomes.spawn-nether-mobs-in-normal", true);
        unsnowBiomes = getString("biomes.disable-snow-forming", "");
        uniceBiomes = getString("biomes.disable-ice-forming", "");
        meltIce = getBoolean("biomes.melt-ice", true);
        meltSnow = getBoolean("biomes.melt-snow", true);
        defaultRadius = getInt("brush.default-radius", 5);
        String biomeStr = getString("brush.biome.default-biome", "iceflats");
        defaultBiome = BiomeTools.isBiomeExists(biomeStr) ? BiomeTools.biomeByName(biomeStr) : Biome.ICE_FLATS;
        Brush.load(WeatherMan.getPlugin().getConfig());
    }

    public static void saveCfg() {
        set("general.language", language);
        set("general.language-save", languageSave);
        set("general.debug", debug);
        set("general.check-updates", checkUpdates);
        set("system.chunk-update-method", chunkUpdateMethod);
        set("local.time-enable", localTimeEnable);
        set("local.weather-enable", localWeatherEnable);
        set("personal.time.reset-on-start", personalTimeClear);
        set("personal.weather.reset-on-start", personalWeatherClear);
        set("personal.brush.reset-on-start", personalBrushClear);
        set("limits.maximum-command-radius", maxRadiusCmd);
        set("limits.maximum-wand-radius", maxRadiusWand);
        set("limits.maximum-sign-radius", maxRadiusSign);
        set("biomes.melt-snow", meltSnow);
        set("biomes.melt-ice", meltIce);
        set("biomes.spawn-nether-mobs-in-normal", netherMob);
        set("biomes.disable-snow-forming", unsnowBiomes);
        set("biomes.disable-ice-forming", uniceBiomes);
        set("brush.default-radius", defaultRadius);
        set("brush.biome.default-biome", BiomeTools.biomeToString(defaultBiome));
        Brush.save(WeatherMan.getPlugin().getConfig());
        WeatherMan.getPlugin().saveConfig();
    }

    private static void set(String key, Object value) {
        WeatherMan.getPlugin().getConfig().set(key, value);
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        return WeatherMan.getPlugin().getConfig().getBoolean(key, defaultValue);
    }

    private static int getInt(String key, int defaultValue) {
        return WeatherMan.getPlugin().getConfig().getInt(key, defaultValue);
    }

    private static String getString(String key, String defaultValue) {
        return WeatherMan.getPlugin().getConfig().getString(key, defaultValue);
    }
}
