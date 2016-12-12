package me.fromgate.weatherman.commands.wm;

import me.fromgate.weatherman.util.Brush;
import me.fromgate.weatherman.util.ItemUtil;
import me.fromgate.weatherman.commands.Cmd;
import me.fromgate.weatherman.commands.CmdDefine;
import me.fromgate.weatherman.util.M;
import org.bukkit.entity.Player;

@CmdDefine(command = "wm", subCommands = "give", permission = "wm.wandbiome",
        description = M.CMD_GIVE, shortDescription = "/wm give [biome|woodcutter|depopulator]")
public class WmGive extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        String arg = args.length > 1 ? args[1] : "";
        if (arg.equalsIgnoreCase(Brush.BIOME.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.BIOME.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.WOODCUTTER.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.WOODCUTTER.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.DEPOPULATOR.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.DEPOPULATOR.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.FORESTER.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.FORESTER.getItemStr()));
        else arg = "";
        if (arg.isEmpty()) {
            return M.MSG_WANDLIST.print(player, "BIOME, WOODCUTTER, DEPOPULATOR, FORESTER");
        }
        return M.MSG_WANDITEMGIVEN.print(player, arg.toUpperCase());
    }
}
