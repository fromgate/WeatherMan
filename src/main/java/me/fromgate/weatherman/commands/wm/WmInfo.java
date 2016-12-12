package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.NMSUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import me.fromgate.weatherman.util.M;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

@CmdDefine(command = "wm", subCommands = "info", permission = "wm.basic",
        description = M.CMD_WALKINFO, shortDescription = "/wm info")
public class WmInfo extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        boolean newMode = !PlayerConfig.isWalkInfoMode(player);
        PlayerConfig.setWalkInfoMode(player, newMode);
        player.sendMessage(M.MSG_WALKINFO.getText() + ": " + M.enDis(newMode));
        if (newMode) {
            Biome b1 = player.getLocation().getBlock().getBiome();
            Biome b2 = NMSUtil.getOriginalBiome(player.getLocation());
            if (b1.equals(b2)) {
                M.MSG_BIOMELOC.print(player, BiomeTools.biome2Str(b1));
            } else {
                M.MSG_BIOMELOC2.print(player, BiomeTools.biome2Str(b1), BiomeTools.biome2Str(b2));
            }
        }
        return true;
    }

}
