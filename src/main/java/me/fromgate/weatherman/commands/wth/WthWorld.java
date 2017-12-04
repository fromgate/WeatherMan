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

package me.fromgate.weatherman.commands.wth;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wth", subCommands = "world", permission = "weatherman.weather",
        description = M.WTH_WORLD, shortDescription = "/wth world [<world> <rain|clear|remove>]",
        allowConsole = true)
public class WthWorld extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!Cfg.localTimeEnable) return M.WTH_DISABLED.print(sender);
        if (args.length <= 2) {
            LocalWeather.printWorldList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {

            World world = Bukkit.getWorld(args[1]);
            if (world == null) {
                return M.WTH_UNKNOWNWORLD.print(args[1]);
            }
            String worldName = world.getName();
            switch (args[2].toLowerCase()) {
                case "rain":
                case "storm":
                    LocalWeather.setWorldRain(world, true);
                    M.WTH_WORLDWEATHER.print(sender, worldName, M.RAIN);
                    break;
                case "sun":
                case "clear":
                    LocalWeather.setWorldRain(world, false);
                    M.WTH_WORLDWEATHER.print(sender, worldName, M.CLEAR);
                    break;
                case "remove":
                case "delete":
                    LocalWeather.clearWorldRain(world);
                    M.WTH_WORLDWEATHERREMOVED.print(sender, worldName);
                    break;
                default:
                    M.WTH_UNKNOWNWEATHER.print(sender, args[2]);
                    break;
            }
        }
        return true;
    }
}
