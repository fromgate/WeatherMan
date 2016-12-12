package me.fromgate.weatherman.commands.wth;

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.localweather.LocalWeather;
import me.fromgate.weatherman.util.M;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;


@CmdDefine(command = "wth", subCommands = "biome|biom", permission = "wm.wth",
        description = M.WTH_BIOME, shortDescription = "/wth biome [<biome> <rain|clear|remove>]",
        allowConsole = true)
public class WthBiome extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        for (String a : args) {
            sender.sendMessage(a);
        }

        if (args.length <= 2) {
            LocalWeather.printBiomeList(sender, args.length == 2 && args[1].matches("\\d+") ? Integer.parseInt(args[1]) : 1);
        } else {
            String biomeName = args[1];
            if (!BiomeTools.isBiomeExists(biomeName)) {
                return M.WTH_UNKNOWNBIOME.print(biomeName);
            }
            Biome biome = BiomeTools.str2Biome(biomeName);
            switch (args[2].toLowerCase()) {
                case "rain":
                case "storm":
                    LocalWeather.setBiomeRain(biome, true);
                    M.WTH_BIOMEWEATHER.print(sender, biome.name(), M.RAIN);
                    break;
                case "sun":
                case "clear":
                    LocalWeather.setBiomeRain(biome, false);
                    M.WTH_BIOMEWEATHER.print(sender, biome.name(), M.CLEAR);
                    break;
                case "remove":
                case "delete":
                    LocalWeather.clearBiomeRain(biome);
                    M.WTH_BIOMEWEATHERREMOVED.print(sender, BiomeTools.biome2Str(biome));
                    break;
                default:
                    M.WTH_UNKNOWNWEATHER.print(sender, args[2]);
                    break;
            }
        }
        return true;
    }
}
