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

import me.fromgate.weatherman.util.Brush;
import me.fromgate.weatherman.util.ItemUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.entity.Player;

@CmdDefine(command = "weatherman", subCommands = "give", permission = "weatherman.wandbiome",
        description = M.CMD_GIVE, shortDescription = "/wm give [biome|woodcutter|depopulator]")
public class WmGive extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        String arg = args.length > 1 ? args[1] : "";
        if (arg.equalsIgnoreCase(Brush.BIOME.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.BIOME.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.WOODCUTTER.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.WOODCUTTER.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.DEPOPULATOR.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.DEPOPULATOR.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.FORESTER.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.FORESTER.getItemStr()));
        else arg = "";
        if (arg.isEmpty()) {
            return M.MSG_WANDLIST.print(player, "BIOME, WOODCUTTER, DEPOPULATOR, FORESTER");
        }
        return M.MSG_WANDITEMGIVEN.print(player, arg.toUpperCase());
    }
}
