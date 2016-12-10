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

/*
 * TODO 
 * - глобальные биомы, регенерацию чанков под заданный биом
 * - walk-mode ?
 */

package me.fromgate.weatherman;


import org.bukkit.block.Biome;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;


public class WeatherMan extends JavaPlugin {

    //конфигурация
    boolean localWeather = true;
    int defaultRadius = 5;
    public boolean smoke = true;
    public int smokeChance = 50;
    public boolean meltSnow = true;
    public boolean meltIce = true;
    Biome defaultBiome = Biome.ICE_FLATS;
    int maxRadiusCmd = 250;
    int maxRadiusWand = 15;
    int maxRadiusSign = 100;
    boolean netherMob = true;
    String unsnowBiomes = "taiga";
    String uniceBiomes = "taiga";
    String language = "english";
    boolean languageSave = false;
    boolean checkUpdates = true;

    //текущие переменные
    boolean consoleColored = false;
    public Util u;
    private WMListener l;
    private Cmd lcmd;

    HashMap<Snowball, BiomeBall> sballs = new HashMap<Snowball, BiomeBall>();  //TODO перевести на Metadata?

    /////////////////////////////////////////////////
    protected LocalWeather lw;
    public static WeatherMan instance;


    @Override
    public void onEnable() {
        loadCfg();
        saveCfg();
        u = new Util(this, this.checkUpdates, this.languageSave, this.language, "weatherman", "WeatherMan", "wm");
        u.setConsoleColored(consoleColored);
        instance = this;
        NMSUtil.init();
        WMWorldEdit.init();
        PlayerConfig.init(this);
        if (NMSUtil.isBlocked()) localWeather = false;
        BiomeTools.initBioms();
        Repopulator.init();
        Forester.init();
        lcmd = new Cmd(this);
        getCommand("wm").setExecutor(lcmd);
        getCommand("wth").setExecutor(lcmd);
        l = new WMListener(this);
        getServer().getPluginManager().registerEvents(l, this);

        if (this.localWeather) {
            lw = new LocalWeather(this);
            getServer().getPluginManager().registerEvents(lw, this);
        } else u.log("Local weather features disabled!");

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
        }
    }

    public void loadCfg() {
        language = getConfig().getString("general.language", "english").toLowerCase();
        languageSave = getConfig().getBoolean("general.language-save", false);
        checkUpdates = getConfig().getBoolean("general.check-updates", true);
        consoleColored = getConfig().getBoolean("general.color-in-console", false);
        localWeather = getConfig().getBoolean("local-weather.enable", true);
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
        String bstr = getConfig().getString("brush.biome.default-biome", "iceplains");
        if (BiomeTools.isBiomeExists(bstr)) defaultBiome = BiomeTools.str2Biome(bstr);
        Brush.load(getConfig());
    }

    public void saveCfg() {
        getConfig().set("general.language", language);
        getConfig().set("general.language-save", languageSave);
        getConfig().set("general.color-in-console", consoleColored);
        getConfig().set("general.check-updates", checkUpdates);
        getConfig().set("local-weather.enable", localWeather);
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
        getConfig().set("brush.biome.default-biome", BiomeTools.biome2Str(defaultBiome));
        Brush.save(getConfig());
        this.saveConfig();
    }
}
