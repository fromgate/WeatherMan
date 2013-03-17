/*  
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012, fromgate, fromgate@gmail.com
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

package fromgate.weatherman;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WMCmd implements CommandExecutor{
	WeatherMan plg;
	WMUtil u;

	public WMCmd (WeatherMan plg){
		this.plg = plg;
		this.u = plg.u;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		String pn = sender.getName();//p.getName();
		String arg0 = "";
		if (args.length>0) arg0 = args[0];
		if (cmdLabel.equalsIgnoreCase("wth")) {
			arg0 = "weather_"+arg0;
			if (!plg.local_weather) {
				u.printMSG(sender, "wth_sorrydisabled",'c','6',"/wth cfg weather");
				return true;
			}
		}

		if ((args.length>0)&&(u.checkCmdPerm(sender, arg0))){
			if (sender instanceof Player){
				Player p = (Player) sender;
				if (!plg.pcfg.containsKey(pn)) plg.pcfg.put(pn, new Cfg());
				if (args.length == 1) return ExecuteCmd(p, arg0);
				if (args.length == 2) return ExecuteCmd(p, arg0, args[1]);
				if (args.length == 3) return ExecuteCmd(p, arg0, args[1], args[2]);
			} else {
				//здесь вызов консольных команд
				if (args.length == 1) return executeConsoleCmd (sender, arg0);
				if (args.length == 3) return executeConsoleCmd (sender, arg0, args[1], args[2]); //set (region)
				if (args.length == 4) return executeConsoleCmd (sender, arg0, args[1], args[2], args[3]); //replace (region)
				if (args.length == 6) return executeConsoleCmd (sender, arg0, args[1], args[2], args[3], args[4], args[5]); //set (radius)
				if (args.length == 7) return executeConsoleCmd (sender, arg0, args[1], args[2], args[3], args[4], args[5], args[6]); //set
				if (args.length == 8) return executeConsoleCmd (sender, arg0, args[1], args[2], args[3], args[4], args[5], args[6],args[7]); //replace

			}

		} else u.printMSG(sender, "cmd_cmdpermerr",'c'); 
		//} else sender.sendMessage("[WeatherMan] Sorry but you can use this command in-game only!");
		return false;
	}

	private boolean executeConsoleCmd(CommandSender sender, String cmd) {
		if (cmd.equalsIgnoreCase("list"))
			u.printMsg(sender, u.getMSG("msg_biomelist")+" &2"+plg.getBiomeList());
		else if (cmd.equalsIgnoreCase("help")){
			u.PrintHlpList(sender, 1,1000);
		} else if (cmd.equals("cfg")){
			u.PrintCfg(sender);
		} else return false;

		return true;
	}



	public boolean executeConsoleCmd(CommandSender sender, String cmd, String biome, String world, String x, String z, String radius){
		if (cmd.equalsIgnoreCase("set")){
			if (plg.bioms.containsKey(biome)){
				Biome biom = plg.Str2Biome(biome); 
				World w = Bukkit.getWorld(world);
				if (w != null){
					if ((x.matches("-?[0-9]+[0-9]*"))&&
							(z.matches("-?[0-9]+[0-9]*"))){
						if (radius.matches("[1-9]+[0-9]*")){
							Location loc = new Location (w,Integer.parseInt(x),0,Integer.parseInt(z));
							WMResult r =plg.SetBiome(biom, loc, Math.min(Integer.parseInt(radius),plg.maxrcmd));
							u.printMSG(sender, "msg_biomearoundloc",biome.toLowerCase(),w.getName(),x,z);
							u.printMSG(sender, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
						} else u.printMSG(sender, "msg_wrongradius",'c','4',radius.toLowerCase());
					} else u.printMSG(sender, "msg_wrongarea",'c','4',"["+world+"] ("+x+", "+z+")");
				} else u.printMSG(sender, "msg_worldunknown",'c','4',world);
			} else u.printMSG(sender, "msg_biomeunknown",'c','4',biome);
		} else return false;
		return true;
	}

	public boolean executeConsoleCmd(CommandSender sender, String cmd, String biome1, String biome2, String world, String x1, String z1, String x2, String z2){
		if (cmd.equalsIgnoreCase("replace")){
			Biome b1 = plg.Str2Biome(biome1);
			Biome b2 = plg.Str2Biome(biome2);

			if (b1==null) {
				u.printMSG(sender, "msg_wrongbiome",biome1);
				return true;
			}
			if ((b2==null)&&(!biome2.equalsIgnoreCase("original"))){
				u.printMSG(sender, "msg_wrongbiome",biome2);
				return true;
			}
			World w = Bukkit.getWorld(world);
			if (w != null){
				if (u.isIntegerSigned(x1,z1,x2,z2)){
					int xmin =  Math.min(Integer.parseInt(x1), Integer.parseInt(x2));
					int xmax =  Math.max(Integer.parseInt(x1), Integer.parseInt(x2));
					int zmin =  Math.min(Integer.parseInt(z1), Integer.parseInt(z2));
					int zmax =  Math.max(Integer.parseInt(z1), Integer.parseInt(z2));
					Location loc1 = new Location (w, xmin, 0, zmin);
					Location loc2 = new Location (w, xmax, 0, zmax);
					WMResult r = plg.ReplaceBiome(b1,b2, loc1, loc2);
					u.printMSG(sender, "msg_biomereplacearea",plg.Biome2Str(b1),plg.Biome2Str(b2),w.getName(),x1,z1,x2,z2);
					u.printMSG(sender, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
				} else u.printMSG(sender, "msg_wrongarea",'c','4',"["+world+"] ("+x1+", "+z1+")x("+x2+", "+z2+")");
			} else u.printMSG(sender, "msg_worldunknown",'c','4',world);
		} else return false;
		return true;
	}

	public boolean executeConsoleCmd(CommandSender sender, String cmd, String biome, String world, String x1, String z1, String x2, String z2){
		if (cmd.equalsIgnoreCase("set")){
			if (plg.bioms.containsKey(biome)){
				Biome biom = plg.Str2Biome(biome); 
				World w = Bukkit.getWorld(world);
				if (w != null){
					if ((x1.matches("-?[0-9]+[0-9]*"))&&
							(z1.matches("-?[0-9]+[0-9]*"))&&
							(x2.matches("-?[0-9]+[0-9]*"))&&
							(z2.matches("-?[0-9]+[0-9]*"))){
						int xmin =  Math.min(Integer.parseInt(x1), Integer.parseInt(x2));
						int xmax =  Math.max(Integer.parseInt(x1), Integer.parseInt(x2));
						int zmin =  Math.min(Integer.parseInt(z1), Integer.parseInt(z2));
						int zmax =  Math.max(Integer.parseInt(z1), Integer.parseInt(z2));
						Location loc1 = new Location (w, xmin,0,zmin);
						Location loc2 = new Location (w, xmax,0,zmax);
						WMResult r = plg.SetBiome(biom, loc1, loc2);
						//plg.setBiomeDalayed(biom, loc1, loc2);
						u.printMSG(sender, "msg_biomearea",biome.toLowerCase(),w.getName(),x1,z1,x2,z2);
						u.printMSG(sender, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
					} else u.printMSG(sender, "msg_wrongarea",'c','4',"["+world+"] ("+x1+", "+z1+")x("+x2+", "+z2+")");
				} else u.printMSG(sender, "msg_worldunknown",'c','4',world);
			} else u.printMSG(sender, "msg_biomeunknown",'c','4',biome);
		} else return false;
		return true;
	}

	public boolean executeConsoleCmd(CommandSender sender, String cmd, String arg1, String arg2){
		if (cmd.equalsIgnoreCase("set")){
			if (plg.bioms.containsKey(arg1)){
				Biome biome = plg.Str2Biome(arg1);
				int rgcount = 0;
				if (plg.worldguard_active){
					for (World w : Bukkit.getWorlds()){
						ProtectedRegion region = plg.worldguard.getRegionManager(w).getRegionExact(arg2);
						if (region != null){
							Location loc1 = new Location (w, region.getMinimumPoint().getBlockX(), 0, region.getMinimumPoint().getBlockZ());
							Location loc2 = new Location (w, region.getMaximumPoint().getBlockX(), 0, region.getMaximumPoint().getBlockZ());
							WMResult r = plg.SetBiome(biome, loc1, loc2);
							u.printMSG(sender, "msg_biomeregion",arg2,arg1);
							u.printMSG(sender, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
							rgcount ++;
						} 
					}
					if (rgcount ==0) u.printMSG(sender, "wg_unknownregion",'c','4',arg2); 
				} else u.printMSG(sender, "wg_notfound",'c');
			} else u.printMSG(sender, "msg_biomeunknown",'c','4',arg1);
		} else if (cmd.equalsIgnoreCase("cfg")){
			return cfgSet(sender, arg1, arg2);
		} else return false;
		return true;
	}

	public boolean executeConsoleCmd(CommandSender sender, String cmd, String biome1, String biome2, String region){
		if (cmd.equalsIgnoreCase("replace")){
			Biome b1 = plg.Str2Biome(biome1);
			Biome b2 = plg.Str2Biome(biome2);
			if (b1==null) {
				u.printMSG(sender, "msg_wrongbiome",biome1);
				return true;
			}
			if ((b2==null)&&(!biome2.equalsIgnoreCase("original"))){
				u.printMSG(sender, "msg_wrongbiome",biome2);
				return true;
			}

			if (plg.worldguard_active){
				int rgcount = 0;
				for (World w : Bukkit.getWorlds()){
					ProtectedRegion rg = plg.worldguard.getRegionManager(w).getRegionExact(region);
					if (region != null){
						Location loc1 = new Location (w, rg.getMinimumPoint().getBlockX(), 0, rg.getMinimumPoint().getBlockZ());
						Location loc2 = new Location (w, rg.getMaximumPoint().getBlockX(), 0, rg.getMaximumPoint().getBlockZ());
						WMResult r = plg.ReplaceBiome(b1,b2, loc1, loc2);
						u.printMSG(sender, "msg_biomereplaceregion",biome1.toLowerCase(),biome2.toLowerCase(),region);
						u.printMSG(sender, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
						rgcount ++;
					} 
				}
				if (rgcount ==0) u.printMSG(sender, "wg_unknownregion",'c','4',region); 
			} else u.printMSG(sender, "wg_notfound",'c');
		} else return false;
		return true;
	}



	// Без параметров
	public boolean ExecuteCmd(Player p, String cmd){
		String pn = p.getName();
		if (cmd.equals("list")){
			u.printMsg(p, u.getMSG("msg_biomelist")+" &2"+plg.getBiomeList());
			/*} else if (cmd.equals("test")){
			plg.lw.setPlayerRain(p, true);*/
		} else if (cmd.equals("check")){
			Biome b1 = p.getLocation().getBlock().getBiome();
			Biome b2 = WMSysTools.getOriginalBiome(p.getLocation());
			if (b1.equals(b2)) u.printMSG(p, "msg_biomeloc", plg.Biome2Str(b1));
			else u.printMSG(p, "msg_biomeloc2", plg.Biome2Str(b1),plg.Biome2Str(b2));
		} else if (cmd.equalsIgnoreCase("biome")){
			plg.pcfg.get(pn).biome = p.getLocation().getBlock().getBiome();
			u.printMSG(p, "msg_curbiome", plg.Biome2Str(plg.pcfg.get(pn).biome));
		} else if (cmd.equalsIgnoreCase("radius")){
			plg.pcfg.get(pn).radius = plg.dradius;
			u.printMSG(p, "def_radius", Integer.toString(plg.pcfg.get(pn).radius));
		} else if (cmd.equals("wand")){
			plg.pcfg.get(pn).wand =!plg.pcfg.get(pn).wand;
			u.printMSG(p, "msg_wandmode",u.EnDis(plg.pcfg.get(pn).wand));
			if (plg.pcfg.get(pn).wand) u.printMSG(p,"msg_biomeradius", plg.Biome2Str(plg.pcfg.get(pn).biome),Integer.toString(plg.pcfg.get(pn).radius));
		} else if (cmd.equals("info")){
			boolean newmode = !WMPCfg.isWalkInfoMode(p);
			WMPCfg.setWalkInfoMode(plg, p, newmode);
			u.printEnDis (p, "msg_walkinfo",newmode);
			if (newmode){
				Biome b1 = p.getLocation().getBlock().getBiome();
				Biome b2 = WMSysTools.getOriginalBiome(p.getLocation());
				if (b1.equals(b2)) u.printMSG(p, "msg_biomeloc", plg.Biome2Str(b1));
				else u.printMSG(p, "msg_biomeloc2", plg.Biome2Str(b1),plg.Biome2Str(b2));
			}
		} else if (cmd.equals("cfg")){
			p.sendMessage("");
			u.PrintCfg(p);
		} else if (cmd.equals("help")){
			u.PrintHlpList(p, 1,10);
		} else if (cmd.equals("weather_player")){
			plg.lw.printPlayerList(p, 1);
		} else if (cmd.equals("weather_biome")){
			plg.lw.printBiomeList(p, 1);
		} else if (cmd.equals("weather_world")){
			plg.lw.printWorldList(p, 1);
		} else if (cmd.equals("weather_region")){
			plg.lw.printRegionList(p, 1);
		} else return false;
		return true;
	}

	// Один параметр
	public boolean ExecuteCmd(Player p, String cmd, String arg){
		String pn=p.getName();
		if (cmd.equalsIgnoreCase("set")){
			if (plg.worldedit_active){
				Selection sel = plg.worldedit.getSelection(p);
				if (sel != null){
					Biome b = plg.Str2Biome(arg);

					if ((b!=null)||arg.equalsIgnoreCase("original")){
						WMResult r = plg.SetBiome(b, sel.getMinimumPoint(), sel.getMaximumPoint());
						u.printMSG(p, "msg_biomeset",plg.Biome2Str(b));
						u.printMSG(p, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
					} else u.printMSG(p, "msg_wrongbiome",arg);
				} else u.printMSG(p, "msg_selectregion");
			} else u.printMSG(p, "need_worldedit");	
		} else if (cmd.equalsIgnoreCase("fill")){
			if (plg.bioms.containsKey(arg)&&(!arg.equalsIgnoreCase("original"))){
				WMResult r = plg.FloodFill(p.getLocation(), plg.Str2Biome(arg));
				u.printMSG(p, "msg_curbiome",arg);
				u.printMSG(p, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
			} else u.printMSG(p, "msg_biomeunknown",arg);
		} else if (cmd.equalsIgnoreCase("biome")){
			if (plg.bioms.containsKey(arg)){
				plg.pcfg.get(pn).biome = plg.Str2Biome(arg);
				u.printMSG(p, "msg_curbiome",arg);
			} else u.printMSG(p, "msg_biomeunknown",arg);
		} else if (cmd.equalsIgnoreCase("radius")){
			if (arg.matches("[1-9]+[0-9]*")){
				plg.pcfg.get(pn).radius = Math.min(Integer.parseInt(arg), plg.maxrwand);
				u.printMSG(p, "msg_curradius",Integer.toString(plg.pcfg.get(pn).radius));
			} else u.printMSG(p, "msg_wrongradius");
		} else if (cmd.equalsIgnoreCase("help")){
			int page = 1;
			if (arg.matches("[1-9]+[0-9]*")) page = Integer.parseInt(arg);
			u.PrintHlpList(p, page,10);
		} else if (arg.equals("nosnow")){
			plg.unsnowbiomes ="";
			u.printMSG(p, "cfg_nosnow_empty");
		} else if (arg.equals("noice")){
			plg.unicebiomes ="";
			u.printMSG(p, "cfg_noice_empty");
		} else if (cmd.equals("weather_player")){
			int page = 1;
			if (arg.matches("[1-9]+[0-9]*")) page = Integer.parseInt(arg);
			plg.lw.printPlayerList(p, page);
		} else if (cmd.equals("weather_biome")){
			int page = 1;
			if (arg.matches("[1-9]+[0-9]*")) page = Integer.parseInt(arg);
			plg.lw.printBiomeList(p, page);
		} else if (cmd.equals("weather_world")){
			int page = 1;
			if (arg.matches("[1-9]+[0-9]*")) page = Integer.parseInt(arg);
			plg.lw.printWorldList(p, page);
		} else if (cmd.equals("weather_region")){
			int page = 1;
			if (arg.matches("[1-9]+[0-9]*")) page = Integer.parseInt(arg);
			plg.lw.printRegionList(p, page);
		} else return false;

		return true;
	}


	// Два параметра
	public boolean ExecuteCmd(Player p, String cmd, String arg1, String arg2){
		if (cmd.equalsIgnoreCase("set")){
			if (plg.bioms.containsKey(arg1)){
				Biome biome = plg.Str2Biome(arg1);

				if (arg2.matches("[1-9]+[0-9]*")) {
					WMResult r =plg.SetBiome(biome, p.getLocation(), Math.min(Integer.parseInt(arg2),plg.maxrcmd));
					u.printMSG(p, "msg_biomearound",arg1);
					u.printMSG(p, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
				} else if (plg.worldguard_active){
					World w = p.getWorld();
					ProtectedRegion region = plg.worldguard.getRegionManager(w).getRegionExact(arg2);
					if (region != null){
						Location loc1 = new Location (w, region.getMinimumPoint().getBlockX(), 0, region.getMinimumPoint().getBlockZ());
						Location loc2 = new Location (w, region.getMaximumPoint().getBlockX(), 0, region.getMaximumPoint().getBlockZ());
						WMResult r = plg.SetBiome(biome, loc1, loc2);
						u.printMSG(p, "msg_biomeregion",arg2,arg1);
						u.printMSG(p, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
					} else u.printMSG(p, "wg_unknownregion",'c','4',arg2); 
				} else u.printMSG(p, "wg_notfound",'c');
				return true;
			} else u.printMSG(p, "msg_biomeunknown",arg1);
			u.printMsg(p, "&c/wm set <biomename> <radius|region>");

		} else if (cmd.equalsIgnoreCase("replace")){
			if (plg.worldedit_active){
				Selection sel = plg.worldedit.getSelection(p);
				if (sel != null){
					Biome b1 = plg.Str2Biome(arg1);
					Biome b2 = plg.Str2Biome(arg2);

					if (b1==null) {
						u.printMSG(p, "msg_wrongbiome",arg1);
						return true;
					}
					if ((b2==null)&&(!arg2.equalsIgnoreCase("original"))){
						u.printMSG(p, "msg_wrongbiome",arg2);
						return true;
					}
					WMResult r = plg.ReplaceBiome(b1,b2, sel.getMinimumPoint(), sel.getMaximumPoint());
					u.printMSG(p, "msg_biomereplace",plg.Biome2Str(b1),plg.Biome2Str(b2));
					u.printMSG(p, "msg_biomestats",  r.getTimeSec(),r.blocks,r.chunks);
				} else u.printMSG(p, "msg_selectregion");

			} else u.printMSG(p, "need_worldedit");	

		} else if (cmd.equalsIgnoreCase("cfg")){
			return cfgSet(p, arg1, arg2);
		} else if (cmd.equals("weather_player")){
			Player player = Bukkit.getPlayerExact(arg1);
			if ((player==null)||(!player.isOnline())){
				u.printMSG(p, "wth_unknownplayer",'c','4',arg1);
				return true;
			}

			if (arg2.equalsIgnoreCase("rain")){
				plg.lw.setPlayerRain(player, true);
				u.printMSG(p, "wth_playerweather",player.getName(),"rain");
				if (!p.equals(player)) u.printMSG(player, "wth_playerweather",player.getName(),"rain");
			} else if (arg2.equalsIgnoreCase("sun")||arg2.equalsIgnoreCase("clear")){
				plg.lw.setPlayerRain(player, false);
				u.printMSG(p, "wth_playerweather",player.getName(),"clear");
				if (!p.equals(player)) u.printMSG(player, "wth_playerweather",player.getName(),"clear");
			} else if (arg2.equalsIgnoreCase("remove")){
				plg.lw.clearPlayerRain(player);
				u.printMSG(p, "wth_playerweatherremoved",player.getName());
				if (!p.equals(player)) u.printMSG(player, "wth_playerweatherremoved",player.getName());
			} else u.printMSG(p, "wth_unknownweather",'c','4',arg2);
		} else if (cmd.equals("weather_biome")){
			Biome biome = plg.Str2Biome(arg1);
			if (biome==null){
				u.printMSG(p, "wth_unknownbiome",'c','4',arg1);
				return true;
			}
			if (arg2.equalsIgnoreCase("rain")){
				plg.lw.setBiomeRain(biome, true);
				u.printMSG(p, "wth_biomeweather",plg.Biome2Str(biome),"rain");
				plg.lw.updatePlayersRain(p.getWorld(), 10);
			} else if (arg2.equalsIgnoreCase("sun")||arg2.equalsIgnoreCase("clear")){
				plg.lw.setBiomeRain(biome, false);
				u.printMSG(p, "wth_biomeweather",plg.Biome2Str(biome),"clear");
				plg.lw.updatePlayersRain(p.getWorld(), 10);
			} else if (arg2.equalsIgnoreCase("remove")){
				plg.lw.clearBiomeRain(biome);
				u.printMSG(p, "wth_biomeweatherremoved",plg.Biome2Str(biome));
				plg.lw.updatePlayersRain(p.getWorld(), 10);
			} else u.printMSG(p, "wth_unknownweather",'c','4',arg2);
		} else if (cmd.equals("weather_region")){
			if (!plg.lw.isRegionExists(arg1)){
				u.printMSG(p, "wth_unknownregion",'c','4',arg1);
				plg.lw.clearRegionRain(arg1);
				return true;
			}
			if (arg2.equalsIgnoreCase("rain")){
				plg.lw.setRegionRain(arg1, true);
				u.printMSG(p, "wth_regionweather",arg1,"rain");
				plg.lw.updatePlayersRain(p.getWorld(), 10);
			} else if (arg2.equalsIgnoreCase("sun")||arg2.equalsIgnoreCase("clear")){
				plg.lw.setRegionRain(arg1, false);
				u.printMSG(p, "wth_regionweather",arg1,"clear");
				plg.lw.updatePlayersRain(p.getWorld(), 10);
			} else if (arg2.equalsIgnoreCase("remove")){
				plg.lw.clearRegionRain(arg1);
				u.printMSG(p, "wth_regionweatherremoved",arg1);
				plg.lw.updatePlayersRain(p.getWorld(), 10);
			} else u.printMSG(p, "wth_unknownweather",'c','4',arg2);

		} else if (cmd.equals("weather_world")){
			World world = Bukkit.getWorld(arg1);
			if (world==null){
				u.printMSG(p, "wth_unknownworld",'c','4',arg1);
				return true;
			}
			if (arg2.equalsIgnoreCase("rain")){
				plg.lw.setWorldRain(world, true);
				u.printMSG(p, "wth_worldweather",world.getName(),"rain");
			} else if (arg2.equalsIgnoreCase("sun")||arg2.equalsIgnoreCase("clear")){
				plg.lw.setWorldRain(world, false);
				u.printMSG(p, "wth_worldweather",world.getName(),"clear");
			} else if (arg2.equalsIgnoreCase("remove")){
				plg.lw.clearWorldRain(world);
				u.printMSG(p, "wth_worldweatherremoved",world.getName());
			} else u.printMSG(p, "wth_unknownweather",'c','4',arg2);
		} else return false;
		return true;
	}

	private boolean cfgSet(CommandSender p, String arg1, String arg2){
		if (arg1.equals("wand")){
			if (arg2.matches("[1-9]+[0-9]*")){
				plg.wand = Integer.parseInt(arg2);
				u.printMSG(p, "msg_wandset",Integer.toString(plg.wand)+" "+Material.getMaterial(plg.wand).name());
			} else u.printMSG(p, "msg_wrongwand",arg2);
		} else if (arg1.equals("smokechance")){
			if ((arg2.matches("[1-9]+[0-9]*")&&(Integer.parseInt(arg2)<100))){
				plg.smoke_chance = Integer.parseInt(arg2);
				u.printMSG(p, "msg_smokechance",Integer.toString(plg.smoke_chance));
			} else u.printMSG(p, "msg_wrongsmch",arg2);
		} else if (arg1.equals("radius")){
			if (arg2.matches("[1-9]+[0-9]*")){
				plg.dradius = Integer.parseInt(arg2);
				u.printMSG(p, "msg_defradiusset",Integer.toString(plg.dradius));
			} else u.printMSG(p, "msg_defradiuswrong",arg2);
		} else if (arg1.equals("biome")){
			if (plg.bioms.containsKey(arg2)){
				plg.dbiome = plg.Str2Biome(arg2);
				u.printMSG(p, "msg_defbiomeset",plg.Biome2Str(plg.dbiome));
			} else u.printMSG(p, "msg_defbiomewrong",arg2);
		} else if (arg1.equals("weather")){
			plg.local_weather= ((arg2.equalsIgnoreCase("true"))||(arg2.equalsIgnoreCase("on")));
			u.printMSG(p, "wth_enabled", u.EnDis(plg.local_weather));
		} else if (arg1.equals("nethermob")){
			plg.nethermob = ((arg2.equalsIgnoreCase("true"))||(arg2.equalsIgnoreCase("on")));
			u.printMSG(p, "msg_mobspawn", u.EnDis(plg.nethermob));
		} else if (arg1.equals("smoke")){
			plg.smoke = ((arg2.equalsIgnoreCase("true"))||(arg2.equalsIgnoreCase("on")));
			u.printMSG(p, "msg_smoke", u.EnDis(plg.smoke));

		} else if (arg1.equals("meltsnow")){
			plg.meltsnow = ((arg2.equalsIgnoreCase("true"))||(arg2.equalsIgnoreCase("on")));
			u.printMSG(p, "msg_meltsnow", u.EnDis(plg.meltsnow));
		} else if (arg1.equals("meltice")){
			plg.meltice = ((arg2.equalsIgnoreCase("true"))||(arg2.equalsIgnoreCase("on")));
			u.printMSG(p, "msg_meltice", u.EnDis(plg.meltice));

		} else if (arg1.equals("maxrcmd")){
			if (arg2.matches("[1-9]+[0-9]*")){
				plg.maxrcmd = Integer.parseInt(arg2);
				u.printMSG(p, "msg_maxradcmd",Integer.toString(plg.maxrcmd));
			} else u.printMSG(p, "msg_maxradwrong",arg2);
		} else if (arg1.equals("maxrwand")){
			if (arg2.matches("[1-9]+[0-9]*")){
				plg.maxrwand = Integer.parseInt(arg2);
				u.printMSG(p, "msg_maxradwand",Integer.toString(plg.maxrwand));
			} else u.printMSG(p, "msg_maxradwrong",arg2);
		} else if (arg1.equals("maxrsign")){
			if (arg2.matches("[1-9]+[0-9]*")){
				plg.maxrsign = Integer.parseInt(arg2);
				u.printMSG(p, "msg_maxradsign",Integer.toString(plg.maxrsign));
			} else u.printMSG(p, "msg_maxradwrong",arg2);
		} else if (arg1.equals("nosnow")){
			plg.unsnowbiomes =plg.checkBiomes(arg2);
			if (plg.unsnowbiomes.isEmpty()){
				u.printMSG(p, "cfg_nosnow_empty");
			} else {
				u.printMSG(p, "cfg_nosnow", plg.unsnowbiomes);
			}
		} else if (arg1.equals("noice")){
			plg.unicebiomes =plg.checkBiomes(arg2);
			if (plg.unicebiomes.isEmpty()){
				u.printMSG(p, "cfg_noice_empty");
			} else {
				u.printMSG(p, "cfg_noice", plg.unicebiomes);
			}
		} else return false;

		plg.SaveCfg();
		return true;
	}


}
