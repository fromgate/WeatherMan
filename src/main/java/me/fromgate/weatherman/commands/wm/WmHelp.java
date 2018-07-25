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

package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.commands.Commander;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "weatherman", subCommands = "help|hlp", permission = "weatherman.basic",
        description = M.CMD_HELP, shortDescription = "/wm help [page]",
        allowConsole = true)
public class WmHelp extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        int page = ((args.length > 1) && args[1].matches("[1-9]+[0-9]*")) ? page = Integer.parseInt(args[1]) : 1;
        Commander.printHelp(sender, page);
        return true;
    }
}
