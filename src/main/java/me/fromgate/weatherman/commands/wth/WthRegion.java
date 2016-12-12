package me.fromgate.weatherman.commands.wth;

import me.fromgate.weatherman.util.WMWorldEdit;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wth", subCommands = "region|rg", permission = "wm.wth",
        description = M.WTH_REGION, shortDescription = "/wth region [<region> <rain|clear|remove>]",
        allowConsole = true)
public class WthRegion extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!WMWorldEdit.isWG()) return M.WG_NOTFOUND.print(sender);
        if (args.length <= 2) {
            LocalWeather.printRegionList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {
            String regionName = args[1];
            if (!WMWorldEdit.isRegionExists(regionName)) {
                LocalWeather.clearRegionRain(regionName);
                return M.WTH_UNKNOWNREGION.print(sender, regionName);
            }

            switch (args[2].toLowerCase()) {
                case "rain":
                case "storm":
                    LocalWeather.setRegionRain(regionName, true);
                    M.WTH_REGIONWEATHER.print(sender, regionName, M.RAIN);
                    break;
                case "sun":
                case "clear":
                    LocalWeather.setRegionRain(regionName, false);
                    M.WTH_REGIONWEATHER.print(sender, regionName, M.CLEAR);
                    break;
                case "remove":
                case "delete":
                    LocalWeather.clearRegionRain(regionName);
                    M.WTH_REGIONWEATHERREMOVED.print(sender, regionName);
                    break;
                default:
                    M.WTH_UNKNOWNWEATHER.print(sender, args[2]);
                    break;
            }
        }
        return true;
    }
}
