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


package me.fromgate.weatherman.commands.wth;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.WMWorldEdit;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wth", subCommands = "region|rg", permission = "weatherman.weather",
        description = M.WTH_REGION, shortDescription = "/wth region [<region> <rain|clear|remove>]",
        allowConsole = true)
public class WthRegion extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!Cfg.localTimeEnable) return M.WTH_DISABLED.print(sender);
        if (!WMWorldEdit.isWG()) return M.WG_NOTFOUND.print(sender);
        if (args.length <= 2) {
            LocalWeather.printRegionList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {
            String regionName = args[1];
            if (!WMWorldEdit.isRegionExists(regionName)) {
                LocalWeather.clearRegionRain(regionName);
                return M.WTH_UNKNOWNREGION.print(sender, regionName);
            }

            switch (args[2].toLowerCase()) {
                case "rain":
                case "storm":
                    LocalWeather.setRegionRain(regionName, true);
                    M.WTH_REGIONWEATHER.print(sender, regionName, M.RAIN);
                    break;
                case "sun":
                case "clear":
                    LocalWeather.setRegionRain(regionName, false);
                    M.WTH_REGIONWEATHER.print(sender, regionName, M.CLEAR);
                    break;
                case "remove":
                case "delete":
                    LocalWeather.clearRegionRain(regionName);
                    M.WTH_REGIONWEATHERREMOVED.print(sender, regionName);
                    break;
                default:
                    M.WTH_UNKNOWNWEATHER.print(sender, args[2]);
                    break;
            }
        }
        return true;
    }
}
