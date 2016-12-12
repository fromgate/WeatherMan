package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.ParamUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.M;
import org.bukkit.Utility;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "wm", subCommands = "replace", permission = "wm.cmdbiome",
        description = M.CMD_REPLACE, shortDescription = "/wm replace source:<biome1> biome:<biome2> [fill:true] [radius:<radius> [loc:<world,x,z]|region:region]",
        allowConsole = true)
public class WmReplace extends Cmd {
    @Utility
    public boolean execute(CommandSender player, String[] args) {
        return BiomeTools.replaceBiomeCommand(player, ParamUtil.parseParams(args, 1, "param"));
    }
}
