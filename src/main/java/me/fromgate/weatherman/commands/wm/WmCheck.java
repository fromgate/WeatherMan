package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.NMSUtil;
import me.fromgate.weatherman.util.ParamUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.M;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;


@CmdDefine(command = "wm", subCommands = "check", permission = "wm.basic",
        description = M.CMD_CHECK, shortDescription = "/wm check [loc:<world>,<x>,<z>]",
        allowConsole = true)
public class WmCheck extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Map<String, String> params = ParamUtil.parseParams(args, 1, "param");
        Location loc = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc", ParamUtil.getParam(params, "loc1", "")));
        if (loc == null) {
            if (sender instanceof Player) {
                loc = ((Player) sender).getLocation();
            } else {
                M.MSG_CMDNEEDPLAYER.print(sender);
            }
        }
        Biome b1 = loc.getBlock().getBiome();
        Biome b2 = NMSUtil.getOriginalBiome(loc);
        if (b1.equals(b2)) {
            M.MSG_BIOMELOC.print(sender, BiomeTools.colorBiomeName(BiomeTools.biome2Str(b1)));
        } else {
            M.MSG_BIOMELOC2.print(sender, BiomeTools.colorBiomeName(BiomeTools.biome2Str(b1)), BiomeTools.colorBiomeName(BiomeTools.biome2Str(b2)));
        }
        return true;
    }

}
