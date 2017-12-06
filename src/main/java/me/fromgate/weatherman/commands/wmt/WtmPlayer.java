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

package me.fromgate.weatherman.commands.wmt;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localtime.LocalTime;
import me.fromgate.weatherman.util.Time;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CmdDefine(command = "wtm", subCommands = "player", permission = "weatherman.time",
        description = M.WTH_PLAYER, shortDescription = "/wtm player [<player> <HH:MM|day|night|remove>]",
        allowConsole = true)
public class WtmPlayer extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length <= 2) {
            LocalTime.printPlayerList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {
            String playerName = args[1];
            if (!isKnownPlayer(playerName)) {
                return M.TM_UNKNOWNPLAYER.print(playerName);
            }
            @SuppressWarnings("deprecation") Player player = Bukkit.getPlayerExact(playerName);
            if (args[2].equalsIgnoreCase("remove")) {
                LocalTime.clearPlayerTime(playerName);
                M.TM_PLAYERTIMEREMOVED.print(sender, playerName);
            } else {
                Long time = Time.parseTime(args[2]);
                if (time == null) {
                    M.TM_WRONG_TIME.print(sender, args[2]);
                } else {
                    LocalTime.setPlayerTime(playerName, time);
                    M.TM_PLAYERTIME.print(sender, playerName, Time.timeToString(time));
                }
            }
            LocalTime.updatePlayerTime(playerName);
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
