package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.ParamUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.M;
import org.bukkit.entity.Player;

import java.util.Map;

@CmdDefine(command = "wm", subCommands = "wand", permission = "wm.wandbiome",
        description = M.CMD_WAND, shortDescription = "/wm wand [biome:<biome> radius:<radius> tree:<tree>]",
        allowConsole = false)
public class WmdWand extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 1) {
            PlayerConfig.toggleWandMode(player);
            M.MSG_WANDCONFIG.print(player, M.enDis(PlayerConfig.isWandMode(player)),
                    BiomeTools.biome2Str(PlayerConfig.getBiomeBall(player).getBiome()),
                    PlayerConfig.getBiomeBall(player).getRadius(), PlayerConfig.getTree(player));
        } else {
            Map<String, String> params = ParamUtil.parseParams(args, 1, "param");
            String biomeStr = ParamUtil.getParam(params, "biome", BiomeTools.biome2Str(PlayerConfig.getBiomeBall(player).getBiome()));
            int radius = ParamUtil.getParam(params, "radius", PlayerConfig.getBiomeBall(player).getRadius());
            PlayerConfig.setBiomeBallCfg(player, biomeStr, radius);
            String treeStr = ParamUtil.getParam(params, "tree", PlayerConfig.getTree(player));
            PlayerConfig.setTree(player, treeStr);
            M.MSG_WANDCONFIG.print(player, M.enDis(PlayerConfig.isWandMode(player)), BiomeTools.biome2Str(PlayerConfig.getBiomeBall(player).getBiome()), PlayerConfig.getBiomeBall(player).getRadius(), PlayerConfig.getTree(player));
        }
        return true;
    }
}
