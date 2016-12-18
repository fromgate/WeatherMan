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


package me.fromgate.weatherman;


import me.fromgate.weatherman.commands.Commander;
import me.fromgate.weatherman.localtime.LocalTime;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public class WeatherMan extends JavaPlugin {

    private static WeatherMan instance;

    public static WeatherMan getPlugin() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        Cfg.loadCfg();
        Cfg.saveCfg();
        M.init("WeatherMan", new BukkitMessenger(this), Cfg.getLanguage(), false, Cfg.isLanguageSave());
        M.setDebugMode(Cfg.isDebug());
        Commander.init(this);
        NMSUtil.init();
        WMWorldEdit.init();
        PlayerConfig.init(this);
        BiomeTools.initBioms();
        Repopulator.init();
        Forester.init();
        LocalTime.init();
        LocalWeather.init();
        getServer().getPluginManager().registerEvents(new WMListener(this), this);

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
        }
        UpdateChecker.init(this, "WeatherMan", "38125", "wm", Cfg.isCheckUpdates());
    }
}
