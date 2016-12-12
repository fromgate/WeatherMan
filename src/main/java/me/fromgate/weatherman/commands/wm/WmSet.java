package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.ParamUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.M;
import org.bukkit.command.CommandSender;


@CmdDefine(command = "wm", subCommands = "set", permission = "wm.cmdbiome",
        description = M.CMD_SET, shortDescription = "/wm set biome:<biome> [radius:<radius> [loc:<world,x,z]|region:region]",
        allowConsole = true)
public class WmSet extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] params) {
        return BiomeTools.setBiomeCommand(sender, ParamUtil.parseParams(params, 1, "param"));
    }

}
