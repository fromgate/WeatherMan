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
import me.fromgate.weatherman.util.Forester;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "weatherman", subCommands = "list", permission = "weatherman.basic",
        description = M.CMD_LIST, shortDescription = "/wm list [tree|BiomeMask]",
        allowConsole = true)
public class WmList extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String arg = args.length > 1 ? args[1] : "";
        if (arg.isEmpty()) {
            return M.MSG_BIOMELIST.print(sender, BiomeTools.getBiomeList(""));
        }
        if (arg.equalsIgnoreCase("tree") || arg.equalsIgnoreCase("trees")) {
            return M.MSG_TREELIST.print(sender, Forester.getTreeStr(sender));
        }
        return M.MSG_BIOMELIST.print(sender, BiomeTools.getBiomeList(arg));
    }

}
