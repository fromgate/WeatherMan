/*
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CmdDefine(command = "wth", subCommands = "player", permission = "weatherman.weather",
        description = M.WTH_PLAYER, shortDescription = "/wth player [<player> <rain|clear|remove>]",
        allowConsole = true)
public class WthPlayer extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!Cfg.localTimeEnable) return M.WTH_DISABLED.print(sender);
        if (args.length <= 2) {
            LocalWeather.printPlayerList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {

            String playerName = args[1];
            if (!isKnownPlayer(playerName)) {
                return M.WTH_UNKNOWNPLAYER.print(playerName);
            }

            @SuppressWarnings("deprecation") Player player = Bukkit.getPlayerExact(playerName);

            switch (args[2].toLowerCase()) {
                case "rain":
                case "storm":
                    LocalWeather.setPlayerRain(player, true);
                    M.WTH_PLAYERWEATHER.print(sender, playerName, M.RAIN);
                    if (player != null && !player.equals(sender)) {
                        M.MY_WEATHER_SET.print(player, M.RAIN);
                    }
                    break;
                case "sun":
                case "clear":
                    LocalWeather.setPlayerRain(player, false);
                    M.WTH_PLAYERWEATHER.print(sender, playerName, M.CLEAR);
                    if (player != null && !player.equals(sender)) {
                        M.MY_WEATHER_SET.print(player, M.CLEAR);
                    }
                    break;
                case "remove":
                case "delete":
                    LocalWeather.clearPlayerRain(player);
                    M.WTH_PLAYERWEATHERREMOVED.print(sender, playerName);
                    if (player != null && !player.equals(sender)) {
                        M.MY_WEATHER_REMOVED.print(player);
                    }
                    break;
                default:
                    M.WTH_UNKNOWNWEATHER.print(sender, args[2]);
                    break;
            }
        }
        return true;
    }


    @SuppressWarnings("deprecation")
    public boolean isKnownPlayer(String playerName) {
        if (Bukkit.getPlayerExact(playerName) != null) {
            return true;
        }
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName().equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }


}
