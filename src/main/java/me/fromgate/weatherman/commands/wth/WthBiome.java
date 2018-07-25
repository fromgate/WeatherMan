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

package me.fromgate.weatherman.commands.wth;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;


@CmdDefine(command = "wth", subCommands = "biome|biom", permission = "weatherman.weather",
        description = M.WTH_BIOME, shortDescription = "/wth biome [<biome> <rain|clear|remove>]",
        allowConsole = true)
public class WthBiome extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!Cfg.localTimeEnable) return M.WTH_DISABLED.print(sender);
        if (args.length <= 2) {
            LocalWeather.printBiomeList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {
            String biomeName = args[1];
            if (!BiomeTools.isBiomeExists(biomeName)) {
                return M.WTH_UNKNOWNBIOME.print(biomeName);
            }
            Biome biome = BiomeTools.biomeByName(biomeName);
            switch (args[2].toLowerCase()) {
                case "rain":
                case "storm":
                    LocalWeather.setBiomeRain(biome, true);
                    M.WTH_BIOMEWEATHER.print(sender, biome.name(), M.RAIN);
                    break;
                case "sun":
                case "clear":
                    LocalWeather.setBiomeRain(biome, false);
                    M.WTH_BIOMEWEATHER.print(sender, biome.name(), M.CLEAR);
                    break;
                case "remove":
                case "delete":
                    LocalWeather.clearBiomeRain(biome);
                    M.WTH_BIOMEWEATHERREMOVED.print(sender, BiomeTools.biomeToString(biome));
                    break;
                default:
                    M.WTH_UNKNOWNWEATHER.print(sender, args[2]);
                    break;
            }
        }
        return true;
    }
}
