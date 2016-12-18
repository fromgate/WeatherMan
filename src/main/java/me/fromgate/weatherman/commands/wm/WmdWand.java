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
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.M;
import org.bukkit.entity.Player;

import java.util.Map;

@CmdDefine(command = "weatherman", subCommands = "wand", permission = "weatherman.wandbiome",
        description = M.CMD_WAND, shortDescription = "/wm wand [biome:<biome> radius:<radius> tree:<tree>]",
        allowConsole = false)
public class WmdWand extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 1) {
            PlayerConfig.toggleWandMode(player);
            M.MSG_WANDCONFIG.print(player, M.enDis(PlayerConfig.isWandMode(player)),
                    BiomeTools.biomeToString(PlayerConfig.getBiomeBall(player).getBiome()),
                    PlayerConfig.getBiomeBall(player).getRadius(), PlayerConfig.getTree(player));
        } else {
            Map<String, String> params = ParamUtil.parseParams(args, 1, "param");
            String biomeStr = ParamUtil.getParam(params, "biome", BiomeTools.biomeToString(PlayerConfig.getBiomeBall(player).getBiome()));
            int radius = ParamUtil.getParam(params, "radius", PlayerConfig.getBiomeBall(player).getRadius());
            PlayerConfig.setBiomeBallCfg(player, biomeStr, radius);
            String treeStr = ParamUtil.getParam(params, "tree", PlayerConfig.getTree(player));
            PlayerConfig.setTree(player, treeStr);
            M.MSG_WANDCONFIG.print(player, M.enDis(PlayerConfig.isWandMode(player)), BiomeTools.biomeToString(PlayerConfig.getBiomeBall(player).getBiome()), PlayerConfig.getBiomeBall(player).getRadius(), PlayerConfig.getTree(player));
        }
        return true;
    }
}
