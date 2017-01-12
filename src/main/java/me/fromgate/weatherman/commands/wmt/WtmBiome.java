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
import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.M;
import me.fromgate.weatherman.util.Time;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;


@CmdDefine(command = "wtm", subCommands = "biome|biom", permission = "weatherman.time",
        description = M.WTH_BIOME, shortDescription = "/wth biome [<biome> <HH:MM|day|night|remove>]",
        allowConsole = true)
public class WtmBiome extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!Cfg.isLocalTimeEnable()) return M.TM_DISABLED.print(sender);
        if (args.length <= 2) {
            LocalTime.printBiomeList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {
            String biomeName = args[1];
            if (!BiomeTools.isBiomeExists(biomeName)) {
                return M.TM_UNKNOWNBIOME.print(biomeName);
            }
            Biome biome = BiomeTools.biomeByName(biomeName);

            if (args[2].equalsIgnoreCase("remove")) {
                LocalTime.clearBiomeTime(biome);
                M.TM_BIOMEREMOVED.print(sender, BiomeTools.biomeToString(biome));
            } else {
                Long time = Time.parseTime(args[2]);
                if (time == null) {
                    M.TM_WRONG_TIME.print(sender, args[2]);
                } else {
                    LocalTime.setBiomeTime(biome, time);
                    M.TM_BIOME.print(sender, biomeName, Time.timeToString(time));
                }
            }
        }
        return true;
    }
}
