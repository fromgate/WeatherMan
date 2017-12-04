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

package me.fromgate.weatherman.commands.self;


import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localtime.LocalTime;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.lang.M;
import me.fromgate.weatherman.util.Time;
import org.bukkit.entity.Player;

@CmdDefine(command = "mytime", subCommands = "(?i)day|noon|night|midnight|remove|reset|(\\d{1,2}:\\d{2})", permission = "weatherman.mytime",
        description = M.MY_TIME, shortDescription = "/mytime <HH:MM|day|night|remove>",
        allowConsole = false)
public class MyTime extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        if (!Cfg.localTimeEnable) return M.TM_DISABLED.print(player);
        if (args[0].matches("(?i)remove|reset")) {
            LocalTime.clearPlayerTime(player);
            M.MY_TIME_REMOVED.print(player);
        } else {
            Long time = Time.parseTime(args[0]);
            if (time == null) {
                M.TM_WRONG_TIME.print(player, args[0]);
            } else {
                LocalTime.setPlayerTime(player, time);
                M.MY_TIME_SET.print(player, Time.timeToString(time));
            }
        }
        return true;
    }
}

