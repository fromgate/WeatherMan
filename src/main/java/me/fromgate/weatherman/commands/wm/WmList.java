package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.Forester;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wm", subCommands = "list", permission = "wm.basic",
        description = M.CMD_LIST, shortDescription = "/wm list [tree|BiomeMask]",
        allowConsole = true)
public class WmList extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String arg = args.length > 1 ? args[1] : "";
        if (arg.isEmpty()) {
            return M.MSG_BIOMELIST.print(sender, BiomeTools.getBiomeList(""));
        }
        if (arg.equalsIgnoreCase("tree") || arg.equalsIgnoreCase("trees")) {
            return M.MSG_TREELIST.print(sender, Forester.getTreeStr(sender));
        }
        return M.MSG_BIOMELIST.print(sender, BiomeTools.getBiomeList(arg));
    }

}
