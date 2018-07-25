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

package me.fromgate.weatherman.commands.wmt;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localtime.LocalTime;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.Time;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wtm", subCommands = "world", permission = "wm.wth",
        description = M.WTH_WORLD, shortDescription = "/wtm world [<world> <HH:MM|day|night|remove>]",
        allowConsole = true)
public class WtmWorld extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!Cfg.localTimeEnable) return M.TM_DISABLED.print(sender);
        if (args.length <= 2) {
            LocalWeather.printWorldList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {

            World world = Bukkit.getWorld(args[1]);
            if (world == null) {
                return M.WTH_UNKNOWNWORLD.print(args[1]);
            }
            String worldName = world.getName();
            if (args[2].equalsIgnoreCase("remove")) {
                LocalTime.clearWorldTime(worldName);
                M.TM_REGIONREMOVED.print(sender, worldName);
            } else {
                Long time = Time.parseTime(args[2]);
                if (time == null) {
                    M.TM_WRONG_TIME.print(sender, args[2]);
                } else {
                    LocalTime.setWorldTime(worldName, time);
                    M.TM_WORLD.print(sender, worldName, Time.timeToString(time));
                }
            }
            LocalTime.updatePlayerTime(world);
        }
        return true;
    }
}
