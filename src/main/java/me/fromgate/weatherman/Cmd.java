/*  
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/weatherman/
 *    
 *  This file is part of WeatherMan.
 *  
 *  WeatherMan is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WeatherMan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WeatherMan.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package me.fromgate.weatherman;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;


public class Cmd implements CommandExecutor {
    WeatherMan plg;
    Util u;

    public Cmd(WeatherMan plg) {
        this.plg = plg;
        this.u = plg.u;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        String arg0 = "";
        if (args.length > 0) arg0 = args[0];
        if (cmdLabel.equalsIgnoreCase("wth")) {
            if (!plg.localWeather) return u.returnMSG(true, sender, "wth_sorrydisabled", 'c', '6', "/wth cfg weather");
            arg0 = "weather_" + arg0;
        }
        if ((args.length > 0) && (u.checkCmdPerm(sender, arg0))) {
            Player p = null;
            if (sender instanceof Player) p = (Player) sender;
            if (arg0.equalsIgnoreCase("populate")) return executePopulateCmd(sender, args);
            else if (arg0.equalsIgnoreCase("set")) return executeSetBiomeCmd(sender, args);
            else if (arg0.equalsIgnoreCase("replace")) return executeReplaceBiomeCmd(sender, args);
            else if (arg0.equals("wand")) return executeWandCmd(p, args);
            else if (arg0.equalsIgnoreCase("help")) return executeHelpCmd(sender, args);
            else if (arg0.equalsIgnoreCase("give")) return executeGiveCmd(sender, args);
            else if (arg0.equalsIgnoreCase("list")) return executeListCmd(sender, args);
            else if (arg0.equalsIgnoreCase("check")) return executeCheckCmd(sender, args);
            else if (arg0.equalsIgnoreCase("info")) return executeInfoCmd(sender, args);
            else if (p != null) {
                // TODO
                if (args.length == 1) return ExecuteCmd(p, arg0);
                if (args.length == 2) return ExecuteCmd(p, arg0, args[1]);
                if (args.length == 3) return ExecuteCmd(p, arg0, args[1], args[2]);
            }
        }
        return false;
    }


    private boolean executeInfoCmd(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return u.returnMSG(true, sender, "msg_cmdneedplayer", 'c');
        Player player = (Player) sender;
        boolean newmode = !PlayerConfig.isWalkInfoMode(player);
        PlayerConfig.setWalkInfoMode(player, newmode);
        u.printEnDis(player, "msg_walkinfo", newmode);
        if (newmode) {
            Biome b1 = player.getLocation().getBlock().getBiome();
            Biome b2 = NMSUtil.getOriginalBiome(player.getLocation());
            if (b1.equals(b2)) u.printMSG(player, "msg_biomeloc", BiomeTools.biome2Str(b1));
            else u.printMSG(player, "msg_biomeloc2", BiomeTools.biome2Str(b1), BiomeTools.biome2Str(b2));
        }
        return true;
    }

    private boolean executeCheckCmd(CommandSender sender, String[] args) {
        Map<String, String> params = ParamUtil.parseParams(args, 1, "param");
        Location loc = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc", ParamUtil.getParam(params, "loc1", "")));
        if (loc == null) {
            if (sender instanceof Player) loc = ((Player) sender).getLocation();
            else return u.returnMSG(true, sender, "msg_cmdneedplayer", 'c');
        }
        Biome b1 = loc.getBlock().getBiome();
        Biome b2 = NMSUtil.getOriginalBiome(loc);
        if (b1.equals(b2)) u.printMSG(sender, "msg_biomeloc", BiomeTools.colorBiomeName(BiomeTools.biome2Str(b1)));
        else
            u.printMSG(sender, "msg_biomeloc2", BiomeTools.colorBiomeName(BiomeTools.biome2Str(b1)), BiomeTools.colorBiomeName(BiomeTools.biome2Str(b2)));
        return true;
    }

    private boolean executeListCmd(CommandSender sender, String[] args) {
        String arg = args.length > 1 ? args[1] : "";
        if (arg.isEmpty()) return u.returnMSG(true, sender, "msg_biomelist", BiomeTools.getBiomeList(""));
        if (arg.equalsIgnoreCase("tree") || arg.equalsIgnoreCase("tree"))
            return u.returnMSG(true, sender, "msg_treelist", Forester.getTreeStr(sender));
        return u.returnMSG(true, sender, "msg_biomelist", BiomeTools.getBiomeList(arg));
    }

    private boolean executeGiveCmd(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return u.returnMSG(true, sender, "msg_cmdneedplayer", 'c');
        String arg = args.length > 1 ? args[1] : "";
        Player player = (Player) sender;
        if (arg.equalsIgnoreCase(Brush.BIOME.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.BIOME.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.WOODCUTTER.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.WOODCUTTER.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.DEPOPULATOR.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.DEPOPULATOR.getItemStr()));
        else if (arg.equalsIgnoreCase(Brush.FORESTER.name()))
            ItemUtil.giveItemOrDrop(player, ItemUtil.parseItemStack(Brush.FORESTER.getItemStr()));
        else arg = "";
        if (arg.isEmpty()) return u.returnMSG(true, sender, "msg_wandlist", "BIOME, WOODCUTTER, DEPOPULATOR, FORESTER");
        u.printMSG(player, "msg_wanditemgiven", arg.toUpperCase());
        return true;
    }

    private boolean executeHelpCmd(CommandSender sender, String[] args) {
        int page = 1;
        int lpp = (sender instanceof Player) ? 10 : 1000;
        if ((args.length > 1) && args[1].matches("[1-9]+[0-9]*")) page = Integer.parseInt(args[1]);
        u.PrintHlpList(sender, page, lpp);
        return true;
    }

    private boolean executeReplaceBiomeCmd(CommandSender player, String[] args) {
        return BiomeTools.replaceBiomeCommand(player, ParamUtil.parseParams(args, 1, "param"));
    }

    private boolean executeWandCmd(Player player, String[] args) {
        if (args.length == 1) {
            PlayerConfig.toggleWandMode(player);
            u.printMSG(player, "msg_wandconfig", u.EnDis(PlayerConfig.isWandMode(player)), BiomeTools.biome2Str(PlayerConfig.getBiomeBall(player).biome), PlayerConfig.getBiomeBall(player).radius, PlayerConfig.getTree(player));
        } else {
            Map<String, String> params = ParamUtil.parseParams(args, 1, "param");
            String biomeStr = ParamUtil.getParam(params, "biome", BiomeTools.biome2Str(PlayerConfig.getBiomeBall(player).biome));
            int radius = ParamUtil.getParam(params, "radius", PlayerConfig.getBiomeBall(player).radius);
            PlayerConfig.setBiomeBallCfg(player, biomeStr, radius);
            String treeStr = ParamUtil.getParam(params, "tree", PlayerConfig.getTree(player));
            PlayerConfig.setTree(player, treeStr);
            u.printMSG(player, "msg_wandconfig", u.EnDis(PlayerConfig.isWandMode(player)), BiomeTools.biome2Str(PlayerConfig.getBiomeBall(player).biome), PlayerConfig.getBiomeBall(player).radius, PlayerConfig.getTree(player));
        }
        return true;
    }

    private boolean executePopulateCmd(CommandSender player, String[] args) {
        return Repopulator.populateCommand(player, ParamUtil.parseParams(args, 1, "param"));
    }

    private boolean executeSetBiomeCmd(CommandSender player, String[] args) {
        return BiomeTools.setBiomeCommand(player, ParamUtil.parseParams(args, 1, "param"));
    }


    // Без параметров
    public boolean ExecuteCmd(Player player, String cmd) {
        if (cmd.equals("weather_player")) {
            plg.lw.printPlayerList(player, 1);
        } else if (cmd.equals("weather_biome")) {
            plg.lw.printBiomeList(player, 1);
        } else if (cmd.equals("weather_world")) {
            plg.lw.printWorldList(player, 1);
        } else if (cmd.equals("weather_region")) {
            plg.lw.printRegionList(player, 1);
        } else return false;
        return true;
    }

    // Один параметр
    public boolean ExecuteCmd(Player p, String cmd, String arg) {
        if (cmd.equals("weather_player")) {
            int page = 1;
            if (u.isIntegerGZ(arg)) page = Integer.parseInt(arg);
            plg.lw.printPlayerList(p, page);
        } else if (cmd.equals("weather_biome")) {
            int page = 1;
            if (u.isIntegerGZ(arg)) page = Integer.parseInt(arg);
            plg.lw.printBiomeList(p, page);
        } else if (cmd.equals("weather_world")) {
            int page = 1;
            if (u.isIntegerGZ(arg)) page = Integer.parseInt(arg);
            plg.lw.printWorldList(p, page);
        } else if (cmd.equals("weather_region")) {
            int page = 1;
            if (u.isIntegerGZ(arg)) page = Integer.parseInt(arg);
            plg.lw.printRegionList(p, page);
        } else return false;
        return true;
    }


    // Два параметра
    public boolean ExecuteCmd(Player p, String cmd, String arg1, String arg2) {
        if (cmd.equals("weather_player")) {
            Player player = getPlayerExact(arg1);
            if ((player == null) || (!player.isOnline()))
                return u.returnMSG(true, p, "wth_unknownplayer", 'c', '4', arg1);
            if (arg2.equalsIgnoreCase("rain")) {
                plg.lw.setPlayerRain(player, true);
                u.printMSG(p, "wth_playerweather", player.getName(), "rain");
                if (!p.equals(player)) u.printMSG(player, "wth_playerweather", player.getName(), "rain");
            } else if (arg2.equalsIgnoreCase("sun") || arg2.equalsIgnoreCase("clear")) {
                plg.lw.setPlayerRain(player, false);
                u.printMSG(p, "wth_playerweather", player.getName(), "clear");
                if (!p.equals(player)) u.printMSG(player, "wth_playerweather", player.getName(), "clear");
            } else if (arg2.equalsIgnoreCase("remove")) {
                plg.lw.clearPlayerRain(player);
                u.printMSG(p, "wth_playerweatherremoved", player.getName());
                if (!p.equals(player)) u.printMSG(player, "wth_playerweatherremoved", player.getName());
            } else u.printMSG(p, "wth_unknownweather", 'c', '4', arg2);
        } else if (cmd.equals("weather_biome")) {
            Biome biome = BiomeTools.str2Biome(arg1);
            if (biome == null) return u.returnMSG(true, p, "wth_unknownbiome", 'c', '4', arg1);
            if (arg2.equalsIgnoreCase("rain")) {
                plg.lw.setBiomeRain(biome, true);
                u.printMSG(p, "wth_biomeweather", BiomeTools.biome2Str(biome), "rain");
                plg.lw.updatePlayersRain(p.getWorld(), 10);
            } else if (arg2.equalsIgnoreCase("sun") || arg2.equalsIgnoreCase("clear")) {
                plg.lw.setBiomeRain(biome, false);
                u.printMSG(p, "wth_biomeweather", BiomeTools.biome2Str(biome), "clear");
                plg.lw.updatePlayersRain(p.getWorld(), 10);
            } else if (arg2.equalsIgnoreCase("remove")) {
                plg.lw.clearBiomeRain(biome);
                u.printMSG(p, "wth_biomeweatherremoved", BiomeTools.biome2Str(biome));
                plg.lw.updatePlayersRain(p.getWorld(), 10);
            } else u.printMSG(p, "wth_unknownweather", 'c', '4', arg2);
        } else if (cmd.equals("weather_region")) {
            if (!WMWorldEdit.isRegionExists(arg1)) {
                plg.lw.clearRegionRain(arg1);
                return u.returnMSG(true, p, "wth_unknownregion", 'c', '4', arg1);
            }
            if (arg2.equalsIgnoreCase("rain")) {
                plg.lw.setRegionRain(arg1, true);
                u.printMSG(p, "wth_regionweather", arg1, "rain");
                plg.lw.updatePlayersRain(p.getWorld(), 10);
            } else if (arg2.equalsIgnoreCase("sun") || arg2.equalsIgnoreCase("clear")) {
                plg.lw.setRegionRain(arg1, false);
                u.printMSG(p, "wth_regionweather", arg1, "clear");
                plg.lw.updatePlayersRain(p.getWorld(), 10);
            } else if (arg2.equalsIgnoreCase("remove")) {
                plg.lw.clearRegionRain(arg1);
                u.printMSG(p, "wth_regionweatherremoved", arg1);
                plg.lw.updatePlayersRain(p.getWorld(), 10);
            } else u.printMSG(p, "wth_unknownweather", 'c', '4', arg2);
        } else if (cmd.equals("weather_world")) {
            World world = Bukkit.getWorld(arg1);
            if (world == null) return u.returnMSG(true, p, "wth_unknownworld", 'c', '4', arg1);
            if (arg2.equalsIgnoreCase("rain")) {
                plg.lw.setWorldRain(world, true);
                u.printMSG(p, "wth_worldweather", world.getName(), "rain");
            } else if (arg2.equalsIgnoreCase("sun") || arg2.equalsIgnoreCase("clear")) {
                plg.lw.setWorldRain(world, false);
                u.printMSG(p, "wth_worldweather", world.getName(), "clear");
            } else if (arg2.equalsIgnoreCase("remove")) {
                plg.lw.clearWorldRain(world);
                u.printMSG(p, "wth_worldweatherremoved", world.getName());
            } else u.printMSG(p, "wth_unknownweather", 'c', '4', arg2);
        } else return false;
        return true;
    }

    private Player getPlayerExact(String name) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getName().equalsIgnoreCase(name)) return player;
        return null;
    }


}
