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


package me.fromgate.weatherman;


import me.fromgate.weatherman.commands.Commander;
import me.fromgate.weatherman.localtime.LocalTime;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.Forester;
import me.fromgate.weatherman.util.Repopulator;
import me.fromgate.weatherman.util.WMListener;
import me.fromgate.weatherman.util.WMWorldEdit;
import me.fromgate.weatherman.util.lang.BukkitMessenger;
import me.fromgate.weatherman.util.lang.M;
import me.fromgate.weatherman.util.tasks.InfoTask;
import me.fromgate.weatherman.util.tasks.LocalWeatherTask;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;


public class WeatherMan extends JavaPlugin {

    private static WeatherMan instance;

    public static WeatherMan getPlugin() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        Cfg.loadCfg();
        Cfg.updateCfg();
        M.init("WeatherMan", new BukkitMessenger(this), Cfg.language, false, Cfg.languageSave);
        M.setDebugMode(Cfg.debug);
        Commander.init(this);
        WMWorldEdit.init();
        PlayerConfig.init(this);
        BiomeTools.initBioms();
        Repopulator.init();
        Forester.init();
        LocalTime.init();
        LocalWeather.init();
        getServer().getPluginManager().registerEvents(new WMListener(this), this);
        new LocalWeatherTask().runTaskTimer(this, 30, 11);
        new InfoTask().runTaskTimer(this, 30, 8);
        new Metrics(this, 0);
    }
}
