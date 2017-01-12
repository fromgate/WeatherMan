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


package me.fromgate.weatherman.commands.wmt;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localtime.LocalTime;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.M;
import me.fromgate.weatherman.util.Time;
import me.fromgate.weatherman.util.WMWorldEdit;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wtm", subCommands = "region|rg", permission = "wm.wth",
        description = M.WTH_REGION, shortDescription = "/wth region [<region> <HH:MM|day|night|remove>]",
        allowConsole = true)
public class WtmRegion extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!Cfg.isLocalTimeEnable()) return M.TM_DISABLED.print(sender);
        if (!WMWorldEdit.isWG()) {
            return M.WG_NOTFOUND.print(sender);
        }
        if (args.length <= 2) {
            LocalTime.printRegionList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {
            String regionName = args[1];
            if (!WMWorldEdit.isRegionExists(regionName)) {
                LocalWeather.clearRegionRain(regionName);
                return M.TM_UNKNOWNREGION.print(sender, regionName);
            }

            if (args[2].equalsIgnoreCase("remove")) {
                LocalTime.clearRegionTime(regionName);
                M.TM_REGIONREMOVED.print(sender, regionName);
            } else {
                Long time = Time.parseTime(args[2]);
                if (time == null) {
                    M.TM_WRONG_TIME.print(sender, args[2]);
                } else {
                    LocalTime.setRegionTime(regionName, time);
                    M.TM_REGION.print(sender, regionName, Time.timeToString(time));
                }
            }

        }
        return true;
    }
}
