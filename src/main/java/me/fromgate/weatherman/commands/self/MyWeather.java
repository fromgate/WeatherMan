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

package me.fromgate.weatherman.commands.self;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.entity.Player;

@CmdDefine(command = "myweather", subCommands = "(?i)rain|storm|clear|sun|remove|delete", permission = "weatherman.myweather",
        description = M.MY_WEATHER, shortDescription = "/myweather <rain|clear|remove>",
        allowConsole = false)
public class MyWeather extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        if (!Cfg.localTimeEnable) return M.WTH_DISABLED.print(player);
        switch (args[0].toLowerCase()) {
            case "rain":
            case "storm":
                LocalWeather.setPlayerRain(player, true);
                M.MY_WEATHER_SET.print(player, M.RAIN);
                break;
            case "sun":
            case "clear":
                LocalWeather.setPlayerRain(player, false);
                M.MY_WEATHER_SET.print(player, M.CLEAR);
                break;
            case "remove":
            case "delete":
                LocalWeather.clearPlayerRain(player);
                M.MY_WEATHER_REMOVED.print(player);
                break;
            default:
                M.WTH_UNKNOWNWEATHER.print(player, args[0]);
                break;
        }
        return true;
    }
}
