package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.ParamUtil;
import me.fromgate.weatherman.util.Repopulator;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.M;
import org.bukkit.command.CommandSender;


@CmdDefine(command = "wm", subCommands = "populate|repopulate", permission = "wm.repopulate",
        description = M.CMD_REPOPULATE, shortDescription = "/wm populate <[radius:<radius>] [loc:<world,x,z]|region:region>",
        allowConsole = true)
public class WmPopulate extends Cmd {

    @Override
    public boolean execute(CommandSender player, String[] args) {
        return Repopulator.populateCommand(player, ParamUtil.parseParams(args, 1, "param"));
    }


}
