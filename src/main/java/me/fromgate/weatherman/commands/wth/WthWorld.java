package me.fromgate.weatherman.commands.wth;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.M;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wth", subCommands = "world", permission = "wm.wth",
        description = M.WTH_WORLD, shortDescription = "/wth world [<world> <rain|clear|remove>]",
        allowConsole = true)
public class WthWorld extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length <= 2) {
            LocalWeather.printWorldList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {

            World world = Bukkit.getWorld(args[1]);
            if (world == null) {
                return M.WTH_UNKNOWNWORLD.print(args[1]);
            }
            String worldName = world.getName();
            switch (args[2].toLowerCase()) {
                case "rain":
                case "storm":
                    LocalWeather.setWorldRain(world, true);
                    M.WTH_WORLDWEATHER.print(sender, worldName, M.RAIN);
                    break;
                case "sun":
                case "clear":
                    LocalWeather.setWorldRain(world, false);
                    M.WTH_WORLDWEATHER.print(sender, worldName, M.CLEAR);
                    break;
                case "remove":
                case "delete":
                    LocalWeather.clearWorldRain(world);
                    M.WTH_WORLDWEATHERREMOVED.print(sender, worldName);
                    break;
                default:
                    M.WTH_UNKNOWNWEATHER.print(sender, args[2]);
                    break;
            }
        }
        return true;
    }
}
