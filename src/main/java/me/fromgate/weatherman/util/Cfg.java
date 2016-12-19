/*
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012-2016, fromgate, fromgate@gmail.com
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
import org.bukkit.configuration.file.FileConfiguration;

public class Cfg {


    private static String language = "english";
    private static boolean languageSave = false;
    private static boolean debug = false;
    private static int defaultRadius = 5;
    private static boolean smoke = true;
    private static int smokeChance = 50;
    private static boolean meltSnow = true;
    private static boolean meltIce = true;
    private static Biome defaultBiome = Biome.ICE_FLATS;
    private static int maxRadiusCmd = 250;
    private static int maxRadiusWand = 15;
    private static int maxRadiusSign = 100;
    private static boolean netherMob = true;
    private static String unsnowBiomes = "taiga";
    private static String uniceBiomes = "taiga";

    private static boolean personalTimeClear = false;
    private static boolean personalWeatherClear = false;
    private static boolean personalBrushClear = true;
    private static boolean checkUpdates = true;
    // private static boolean consoleColored = false;

    public static void loadCfg() {
        WeatherMan.getPlugin().reloadConfig();
        language = getConfig().getString("general.language", "english").toLowerCase();
        languageSave = getConfig().getBoolean("general.language-save", false);
        debug = getConfig().getBoolean("general.debug", false);
        checkUpdates = getConfig().getBoolean("general.check-updates", true);
        personalTimeClear = getConfig().getBoolean("personal.time.reset-on-start", false);
        personalWeatherClear = getConfig().getBoolean("personal.weather.reset-on-start", false);
        personalBrushClear = getConfig().getBoolean("personal.brush.reset-on-start", true);
        smoke = getConfig().getBoolean("effect.smoke-effect", true);
        smokeChance = getConfig().getInt("effect.smoke-chance", 50);
        maxRadiusCmd = getConfig().getInt("limits.maximum-command-radius", 250);
        maxRadiusWand = getConfig().getInt("limits.maximum-wand-radius", 15);
        maxRadiusSign = getConfig().getInt("limits.maximum-sign-radius", 100);
        netherMob = getConfig().getBoolean("biomes.spawn-nether-mobs-in-normal", true);
        unsnowBiomes = getConfig().getString("biomes.disable-snow-forming", "");
        uniceBiomes = getConfig().getString("biomes.disable-ice-forming", "");
        meltIce = getConfig().getBoolean("biomes.melt-ice", true);
        meltSnow = getConfig().getBoolean("biomes.melt-snow", true);
        defaultRadius = getConfig().getInt("brush.default-radius", 5);
        String biomeStr = getConfig().getString("brush.biome.default-biome", "iceplains");
        if (BiomeTools.isBiomeExists(biomeStr)) {
            defaultBiome = BiomeTools.biomeByName(biomeStr);
        }
        Brush.load(WeatherMan.getPlugin().getConfig());
    }

    public static void saveCfg() {
        getConfig().set("general.language", language);
        getConfig().set("general.language-save", languageSave);
        getConfig().set("general.debug", debug);
        getConfig().set("general.check-updates", checkUpdates);
        getConfig().set("personal.time.reset-on-start", personalTimeClear);
        getConfig().getBoolean("personal.weather.reset-on-start", personalWeatherClear);
        getConfig().getBoolean("personal.brush.reset-on-start", personalBrushClear);
        getConfig().set("effect.smoke-effect", smoke);
        getConfig().set("effect.smoke-chance", smokeChance);
        getConfig().set("limits.maximum-command-radius", maxRadiusCmd);
        getConfig().set("limits.maximum-wand-radius", maxRadiusWand);
        getConfig().set("limits.maximum-sign-radius", maxRadiusSign);
        getConfig().set("biomes.melt-snow", meltSnow);
        getConfig().set("biomes.melt-ice", meltIce);
        getConfig().set("biomes.spawn-nether-mobs-in-normal", netherMob);
        getConfig().set("biomes.disable-snow-forming", unsnowBiomes);
        getConfig().set("biomes.disable-ice-forming", uniceBiomes);
        getConfig().set("brush.default-radius", defaultRadius);
        getConfig().set("brush.biome.default-biome", BiomeTools.biomeToString(defaultBiome));
        Brush.save(WeatherMan.getPlugin().getConfig());
        WeatherMan.getPlugin().saveConfig();
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Cfg.debug = debug;
    }

    public static int getDefaultRadius() {
        return defaultRadius;
    }

    public static void setDefaultRadius(int defaultRadius) {
        Cfg.defaultRadius = defaultRadius;
    }

    public static boolean isSmoke() {
        return smoke;
    }

    public static void setSmoke(boolean smoke) {
        Cfg.smoke = smoke;
    }

    public static int getSmokeChance() {
        return smokeChance;
    }

    public static void setSmokeChance(int smokeChance) {
        Cfg.smokeChance = smokeChance;
    }

    public static boolean isMeltSnow() {
        return meltSnow;
    }

    public static void setMeltSnow(boolean meltSnow) {
        Cfg.meltSnow = meltSnow;
    }

    public static boolean isMeltIce() {
        return meltIce;
    }

    public static void setMeltIce(boolean meltIce) {
        Cfg.meltIce = meltIce;
    }

    public static Biome getDefaultBiome() {
        return defaultBiome;
    }

    public static void setDefaultBiome(Biome defaultBiome) {
        Cfg.defaultBiome = defaultBiome;
    }

    public static int getMaxRadiusCmd() {
        return maxRadiusCmd;
    }

    public static void setMaxRadiusCmd(int maxRadiusCmd) {
        Cfg.maxRadiusCmd = maxRadiusCmd;
    }

    public static int getMaxRadiusWand() {
        return maxRadiusWand;
    }

    public static void setMaxRadiusWand(int maxRadiusWand) {
        Cfg.maxRadiusWand = maxRadiusWand;
    }

    public static int getMaxRadiusSign() {
        return maxRadiusSign;
    }

    public static void setMaxRadiusSign(int maxRadiusSign) {
        Cfg.maxRadiusSign = maxRadiusSign;
    }

    public static boolean isNetherMob() {
        return netherMob;
    }

    public static void setNetherMob(boolean netherMob) {
        Cfg.netherMob = netherMob;
    }

    public static String getUnsnowBiomes() {
        return unsnowBiomes;
    }

    public static void setUnsnowBiomes(String unsnowBiomes) {
        Cfg.unsnowBiomes = unsnowBiomes;
    }

    public static String getUniceBiomes() {
        return uniceBiomes;
    }

    public static void setUniceBiomes(String uniceBiomes) {
        Cfg.uniceBiomes = uniceBiomes;
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        Cfg.language = language;
    }

    public static boolean isLanguageSave() {
        return languageSave;
    }

    public static void setLanguageSave(boolean languageSave) {
        Cfg.languageSave = languageSave;
    }

    public static boolean isCheckUpdates() {
        return checkUpdates;
    }

    public static void setCheckUpdates(boolean checkUpdates) {
        Cfg.checkUpdates = checkUpdates;
    }

    public static boolean isPersonalTimeClear() {
        return personalTimeClear;
    }

    public static void setPersonalTimeClear(boolean personalTimeClear) {
        Cfg.personalTimeClear = personalTimeClear;
    }

    public static boolean isPersonalWeatherClear() {
        return personalWeatherClear;
    }

    public static void setPersonalWeatherClear(boolean personalWeatherClear) {
        Cfg.personalWeatherClear = personalWeatherClear;
    }

    public static boolean isPersonalBrushClear() {
        return personalBrushClear;
    }

    public static void setPersonalBrushClear(boolean personalBrushClear) {
        Cfg.personalBrushClear = personalBrushClear;
    }

    private static FileConfiguration getConfig() {
        return WeatherMan.getPlugin().getConfig();
    }

}
