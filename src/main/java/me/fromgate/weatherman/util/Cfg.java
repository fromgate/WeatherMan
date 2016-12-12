package me.fromgate.weatherman.util;

import me.fromgate.weatherman.WeatherMan;
import org.bukkit.block.Biome;

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

    private static boolean checkUpdates = true;
    // private static boolean consoleColored = false;

    public static void loadCfg() {
        WeatherMan.getPlugin().reloadConfig();
        language = WeatherMan.getPlugin().getConfig().getString("general.language", "english").toLowerCase();
        languageSave = WeatherMan.getPlugin().getConfig().getBoolean("general.language-save", false);
        debug = WeatherMan.getPlugin().getConfig().getBoolean("general.debug", false);
        checkUpdates = WeatherMan.getPlugin().getConfig().getBoolean("general.check-updates", true);
        smoke = WeatherMan.getPlugin().getConfig().getBoolean("effect.smoke-effect", true);
        smokeChance = WeatherMan.getPlugin().getConfig().getInt("effect.smoke-chance", 50);
        maxRadiusCmd = WeatherMan.getPlugin().getConfig().getInt("limits.maximum-command-radius", 250);
        maxRadiusWand = WeatherMan.getPlugin().getConfig().getInt("limits.maximum-wand-radius", 15);
        maxRadiusSign = WeatherMan.getPlugin().getConfig().getInt("limits.maximum-sign-radius", 100);
        netherMob = WeatherMan.getPlugin().getConfig().getBoolean("biomes.spawn-nether-mobs-in-normal", true);
        unsnowBiomes = WeatherMan.getPlugin().getConfig().getString("biomes.disable-snow-forming", "");
        uniceBiomes = WeatherMan.getPlugin().getConfig().getString("biomes.disable-ice-forming", "");
        meltIce = WeatherMan.getPlugin().getConfig().getBoolean("biomes.melt-ice", true);
        meltSnow = WeatherMan.getPlugin().getConfig().getBoolean("biomes.melt-snow", true);
        defaultRadius = WeatherMan.getPlugin().getConfig().getInt("brush.default-radius", 5);
        String bstr = WeatherMan.getPlugin().getConfig().getString("brush.biome.default-biome", "iceplains");
        if (BiomeTools.isBiomeExists(bstr)) defaultBiome = BiomeTools.str2Biome(bstr);
        Brush.load(WeatherMan.getPlugin().getConfig());
    }

    public static void saveCfg() {
        WeatherMan.getPlugin().getConfig().set("general.language", language);
        WeatherMan.getPlugin().getConfig().set("general.language-save", languageSave);
        WeatherMan.getPlugin().getConfig().set("general.debug", debug);
        WeatherMan.getPlugin().getConfig().set("general.check-updates", checkUpdates);
        WeatherMan.getPlugin().getConfig().set("effect.smoke-effect", smoke);
        WeatherMan.getPlugin().getConfig().set("effect.smoke-chance", smokeChance);
        WeatherMan.getPlugin().getConfig().set("limits.maximum-command-radius", maxRadiusCmd);
        WeatherMan.getPlugin().getConfig().set("limits.maximum-wand-radius", maxRadiusWand);
        WeatherMan.getPlugin().getConfig().set("limits.maximum-sign-radius", maxRadiusSign);
        WeatherMan.getPlugin().getConfig().set("biomes.melt-snow", meltSnow);
        WeatherMan.getPlugin().getConfig().set("biomes.melt-ice", meltIce);
        WeatherMan.getPlugin().getConfig().set("biomes.spawn-nether-mobs-in-normal", netherMob);
        WeatherMan.getPlugin().getConfig().set("biomes.disable-snow-forming", unsnowBiomes);
        WeatherMan.getPlugin().getConfig().set("biomes.disable-ice-forming", uniceBiomes);
        WeatherMan.getPlugin().getConfig().set("brush.default-radius", defaultRadius);
        WeatherMan.getPlugin().getConfig().set("brush.biome.default-biome", BiomeTools.biome2Str(defaultBiome));
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

}
