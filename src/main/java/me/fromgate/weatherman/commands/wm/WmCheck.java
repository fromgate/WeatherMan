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
import me.fromgate.weatherman.util.NMSUtil;
import me.fromgate.weatherman.util.ParamUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.M;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;


@CmdDefine(command = "weatherman", subCommands = "check", permission = "weatherman.basic",
        description = M.CMD_CHECK, shortDescription = "/wm check [loc:<world>,<x>,<z>]",
        allowConsole = true)
public class WmCheck extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Map<String, String> params = ParamUtil.parseParams(args, 1, "param");
        Location loc = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc", ParamUtil.getParam(params, "loc1", "")));
        if (loc == null) {
            if (sender instanceof Player) {
                loc = ((Player) sender).getLocation();
            } else {
                M.MSG_CMDNEEDPLAYER.print(sender);
            }
        }
        Biome b1 = loc.getBlock().getBiome();
        Biome b2 = NMSUtil.getOriginalBiome(loc);
        if (b1.equals(b2)) {
            M.MSG_BIOMELOC.print(sender, BiomeTools.colorBiomeName(BiomeTools.biomeToString(b1)));
        } else {
            M.MSG_BIOMELOC2.print(sender, BiomeTools.colorBiomeName(BiomeTools.biomeToString(b1)), BiomeTools.colorBiomeName(BiomeTools.biomeToString(b2)));
        }
        return true;
    }

}
