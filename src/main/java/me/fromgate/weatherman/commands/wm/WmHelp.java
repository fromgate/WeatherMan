package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.commands.Commander;
import me.fromgate.weatherman.util.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wm", subCommands = "help|hlp", permission = "wm.basic",
        description = M.CMD_HELP, shortDescription = "/wm help [page]",
        allowConsole = true)
public class WmHelp extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        int page = ((args.length > 1) && args[1].matches("[1-9]+[0-9]*")) ? page = Integer.parseInt(args[1]) : 1;
        Commander.printHelp(sender, page);
        return true;
    }
}
