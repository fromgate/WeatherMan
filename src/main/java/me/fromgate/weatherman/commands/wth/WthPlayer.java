package me.fromgate.weatherman.commands.wth;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.M;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CmdDefine(command = "wth", subCommands = "player", permission = "wm.wth",
        description = M.WTH_PLAYER, shortDescription = "/wth player [<player> <rain|clear|remove>]",
        allowConsole = true)
public class WthPlayer extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
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
                    LocalWeather.setPlayerRain(player, true);
                    M.WTH_PLAYERWEATHER.print(sender, playerName, M.RAIN);
                    if (player != null && !player.equals(sender)) {
                        M.WTH_PLAYERWEATHER.print(player, playerName, M.RAIN);
                    }
                    break;
                case "sun":
                case "clear":
                    LocalWeather.setPlayerRain(player, false);
                    M.WTH_PLAYERWEATHER.print(sender, playerName, M.CLEAR);
                    if (player != null && !player.equals(sender)) {
                        M.WTH_PLAYERWEATHER.print(player, playerName, M.CLEAR);
                    }
                    break;
                case "remove":
                case "delete":
                    LocalWeather.clearPlayerRain(player);
                    M.WTH_PLAYERWEATHERREMOVED.print(sender, playerName);
                    if (player != null && !player.equals(sender)) {
                        M.WTH_PLAYERWEATHERREMOVED.print(player, playerName);
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
