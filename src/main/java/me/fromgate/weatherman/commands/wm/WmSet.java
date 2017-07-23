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

package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.ParamUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.command.CommandSender;


@CmdDefine(command = "weatherman", subCommands = "set", permission = "weatherman.cmdbiome",
        description = M.CMD_SET, shortDescription = "/wm set biome:<biome> [radius:<radius> [loc:<world,x,z]|region:region]",
        allowConsole = true)
public class WmSet extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] params) {
        return BiomeTools.setBiomeCommand(sender, ParamUtil.parseParams(params, 1, "param"));
    }

}
