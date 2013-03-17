/*  
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/weatherman/
 *   * 
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
 *  along with WeatherMan.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package fromgate.weatherman;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class WMUtil extends FGUtilCore {
	WeatherMan plg;

	public WMUtil(WeatherMan plugin, boolean vcheck, boolean savelng, String language, String devbukkitname, String version_name, String plgcmd, String px){
		super (plugin, vcheck, savelng, language, devbukkitname, version_name, plgcmd, px);
		this.plg = plugin;
		FillMSG();
		InitCmd();
		if (savelng) this.SaveMSG();
	}

	public void PrintCfg(CommandSender p){
		printMsg(p, "&6&lWeatherMan "+des.getVersion()+" &r&6| "+getMSG("msg_config",'6'));
		printMSG(p, "cfg_plgconfig");
		printMSG(p, "cfg_wanditem",Integer.toString(plg.wand)+" "+Material.getMaterial(plg.wand).name());
		printMSG(p, "cfg_melt",EnDis(plg.meltsnow),EnDis(plg.meltice));
		printMSG(p, "cfg_mobspawn",EnDis(plg.nethermob));
		printMsg(p, getMSG("cfg_defbiome",plg.Biome2Str(plg.dbiome))+" "+ getMSG("cfg_defradius",plg.dradius));
		printMSG(p, "cfg_maxradius");
		printMsg(p, getMSG("cfg_maxrcmd",Integer.toString(plg.maxrcmd))+" "+getMSG("cfg_maxrwand",Integer.toString(plg.maxrwand))+" "+
				getMSG("cfg_maxrsign",Integer.toString(plg.maxrsign)));
		if (plg.unsnowbiomes.isEmpty())	printMSG(p, "cfg_nosnow_empty");
		else printMSG(p, "cfg_nosnow", plg.unsnowbiomes);

		if (plg.unicebiomes.isEmpty()) printMSG(p, "cfg_noice_empty");
		else printMSG(p, "cfg_noice", plg.unicebiomes);
		
		if (p instanceof Player){
			printMSG(p, "cfg_player");
			printMSG(p, "cfg_wandbiomeradius",EnDis(plg.pcfg.get(p.getName()).wand),plg.Biome2Str(plg.pcfg.get(p.getName()).biome),Integer.toString(plg.pcfg.get(p.getName()).radius));
		}
	}



	public void InitCmd(){
		cmds.clear();
		cmdlist = "";
		addCmd("help", "basic","cmd_help","&3/wm help [command]",'b',true);
		addCmd("check", "basic","cmd_check","&3/wm check",'b');
		addCmd("list", "basic","cmd_list","&3/wm list",'b',true);
		addCmd("set", "cmdbiome","cmd_set","&3/wm set <biome> [radius|region]",'3',true); // Консоль в ограниченном режиме
		addCmd("replace", "cmdbiome","cmd_replace","&3/wm replace <biome1> <biome2>",'b',true);
		addCmd("fill", "cmdbiome","cmd_fill","&3/wm fill <biome>",'b');
		addCmd("wand", "wandbiome","cmd_wand","&3/wm wand",'b');
		addCmd("radius", "wandbiome","cmd_radius","&3/wm radius [radius]",'b');
		addCmd("biome", "wandbiome","cmd_biome","&3/wm biome [biome]",'b');
		addCmd("cfg", "config","cmd_cfg","&3/wm cfg [<parameter> <value>]",'b',true);
		addCmd("info", "basic","cmd_walkinfo","&3/wm info",'b');
		
		addCmd("weather_player", "weather","wth_player","&3/wth player [<region> <rain|clear|remove>]",'b');
		addCmd("weather_world", "weather","wth_world","&3/wth world [<region> <rain|clear|remove>]",'b');
		addCmd("weather_biome", "weather","wth_biome","&3/wth biome [<region> <rain|clear|remove>]",'b');
		addCmd("weather_region", "weather","wth_region","&3/wth region [<region> <rain|clear|remove>]",'b');
		
		
		
		
	}

	public void FillMSG(){
		addMSG ("disabled", "disabled");
		addMSG ("enabled", "enabled");
		addMSG ("cmd_biome", "%1% - set the current biome value");
		addMSG ("cmd_cfg", "%1% - configure plugin");
		addMSG ("cmd_check", "%1% - check biome in player location");
		addMSG ("cmd_fill", "%1% - replace current biome with new one");
		addMSG ("cmd_replace", "%1% - replace <biome1> with <biome2> inside selected WorldEdit region");
		addMSG ("cmd_help", "%1% - display help");
		addMSG ("cmd_list", "%1% - list all biome types");
		addMSG ("cmd_radius", "%1% - set the current radius value");
		addMSG ("cmd_set", "%1% - set the biome around the player or at defined WorldGuard region (if radius or region name is skipped it will change biome at selected WorldEdit region)");
		addMSG ("cmd_wand", "%1% - toggles wand mode");
		addMSG ("cmd_walkinfo", "%1% - toggles walk-info mode");
		addMSG ("msg_wrong", "Something wrong (check command, permissions)");
		addMSG ("msg_biomelist", "Biome list:");
		addMSG ("msg_biomeloc", "Biome in your location is set to %1%");
		addMSG ("msg_biomeloc2", "Biome in your location is set to %1%. Original biome: %2%");
		addMSG ("msg_movetobiome", "You enter biome %1%");
		addMSG ("msg_movetobiome2", "You enter biome %1%. Original biome: %2%");
		addMSG ("msg_curbiome", "Current biome is set to: %1%");
		addMSG ("msg_defradius", "Current radius is set to: %1% (default)");
		addMSG ("msg_wandmode", "Wand mode is %1%");
		addMSG ("msg_biomeradius", "Biome: %1% Radius: %2%");
		addMSG ("msg_config", "Configuration");
		addMSG ("cfg_plgconfig", "Plugin configuration:");
		addMSG ("cfg_wanditem", "Wand item id: %1%");
		addMSG ("cfg_melt", "Melt snow: %1% melt ice: %2%");
		addMSG ("cfg_mobspawn", "PigZombies and Ghasts spawning in Normal world: %1%");
		addMSG ("cfg_defbiome", "Default Biome: %1%");
		addMSG ("cfg_defradius", "Default radius: %1%");
		addMSG ("cfg_maxradius", "Maximum radius:");
		addMSG ("cfg_maxrcmd", "Command - %1%");
		addMSG ("cfg_maxrwand", "Wand - %1%");
		addMSG ("cfg_maxrsign", "Sign - %1%");
		addMSG ("cfg_player", "Current player configuration:");
		addMSG ("cfg_wandbiomeradius", "Wand mode: %1% Biome: %2% Radius: %3% ");
		addMSG ("hlp_commands", "Commands: %1%");
		addMSG ("msg_biomeset", "Biome in selected region was set to %1%");
		addMSG ("msg_biomestats","Execution time: %1% sec. Blocks: %2% (Chunks: %3%)");
		addMSG ("msg_biomereplace", "Biome %1% in selected region was changed to %2%");
		addMSG ("msg_biomereplaceregion", "Biome %1% in region %3% was changed to %2%");
		addMSG ("msg_biomereplacearea", "Biome %1% at area [%3%] (%4%, %5%)x(%6%, %7%) was changed to %2%");
		addMSG ("msg_wrongbiome", "Wrong biome name %1% Type /wm list to show all possible biome types");
		addMSG ("msg_selectregion", "Select a region with WorldEdit first");
		addMSG ("msg_needworldedit", "WorldEdit was not found");
		addMSG ("msg_biomeunknown", "Unknown biome: %1%");
		addMSG ("msg_worldunknown", "Unknown world: %1%");
		addMSG ("msg_curradius", "Current radius is set to: %1%");
		addMSG ("msg_wrongradius", "Wrong radius: %1%");
		addMSG ("msg_wrongarea", "Wrong area coordinates: %1%");
		addMSG ("msg_cmdunknown", "Unknown command/parameter %1%");
		addMSG ("msg_biomearound", "Biome around you was changed to %1%");
		addMSG ("msg_biomearoundloc", "Biome around location [%2%] (%3%, %4%) was changed to %1%");
		addMSG ("msg_biomeregion", "Biome at region %1% you was changed to %2%");
		
		addMSG ("msg_biomearea", "Biome at area [%2%] (%3%, %4%)x(%5%, %6%) was changed to %1%");
		addMSG ("msg_wandset", "Wand id is set to: %1%");
		addMSG ("msg_wrongwand", "Wrong wand item id: %1%");
		addMSG ("msg_smokechance", "Smoke chance is set to: %1%");
		addMSG ("msg_wrongsmch", "Wrong smoke chance value: %1%");
		addMSG ("msg_defradiuswrong", "Wrong default radius value: %1%");
		addMSG ("msg_defradiusset", "Default radius is set to: %1%");
		addMSG ("msg_defbiomeset", "Default biome is set to: %1%");
		addMSG ("msg_defbiomewrong", "Wrong default biome value: %1%");
		addMSG ("msg_mobspawn", "Spawning nether mobs in normal worlds: %1%");
		addMSG ("msg_smoke", "Smoke effect is %1%");
		addMSG ("msg_meltsnow", "Melting snow on biome change is %1%");
		addMSG ("msg_meltice", "Melting ice on biome change is %1%");
		addMSG ("msg_maxradcmd", "Maximum radius (command) is set to: %1%");
		addMSG ("msg_maxradwrong", "Wrong maximum radius value: %1%");
		addMSG ("msg_maxradwand", "Maximum radius (wand) is set to: %1%");
		addMSG ("msg_maxradsign", "Maximum radius (sign) is set to: %1%");
		addMSG ("msg_wandmodedisabled", "WeatherMan wand mode was %1%. Type %2% to enable it again");	
		addMSG ("cfg_nosnow", "Snow-forming is disabled at biomes: %1%");
		addMSG ("cfg_noice", "Ice-forming is disabled at biomes: %1%");
		addMSG ("cfg_nosnow_empty", "Snow-forming is enabled at all cold biomes");
		addMSG ("cfg_noice_empty", "Ice-forming is enabled at all cold biomes");
		addMSG ("wg_unknownregion", "Unknown WorldGuard region: %1%");
		addMSG ("wg_notfound", "WorldGuard plugin is not found (Is it installed?)");
		addMSG ("msg_walkinfo", "Walk-info mode");
		
		addMSG ("wth_unknownplayer", "Cannot change the personal weather. Player %1% is unknown.");
		addMSG ("wth_playerweather", "Weather state for player %1% was set to %2%");
		addMSG ("wth_playerweatherremoved", "Personal weather setting for player %1% was removed!");
		addMSG ("wth_biomeweather", "Weather state for biome %1% was set to %2%");
		addMSG ("wth_biomeweatherremoved", "Weather settings for biome %1% was removed!");
		addMSG ("wth_regionweather", "Weather state for region %1% was set to %2%");
		addMSG ("wth_regionweatherremoved", "Weather settings for region %1% was removed!");
		addMSG ("wth_worldweather", "Weather state for world %1% was set to %2%");
		addMSG ("wth_worldweatherremoved", "Weather settings for world %1% was removed!");
		addMSG ("wth_playerlist", "Player weather settings:");
		addMSG ("wth_playerlistempty", "Personal weather list is empty");
		addMSG ("wth_regionlist", "Region weather settings:");
		addMSG ("wth_regionlistempty", "Region weather list is empty");
		addMSG ("wth_biomelist", "Biome weather settings:");
		addMSG ("wth_biomelistempty", "Biome weather list is empty");
		addMSG ("wth_worldlist", "World weather settings:");
		addMSG ("wth_worldlistempty", "World weather list is empty");
		addMSG ("wth_unknownweather", "Unknown weather %1% (must be \"rain\" or \"clear\")");
		addMSG ("wth_unknownbiome", "Cannot change the biome weather. Biome %1% is unknown.");
		addMSG ("wth_unknownregion", "Cannot change the region weather. Region %1% is unknown.");
		addMSG ("wth_unknownworld", "Cannot change the world weather. World %1% is unknown.");
		addMSG ("wth_biome", "%1% - set weather state for defined biome");
		addMSG ("wth_world", "%1% - set weather state for defined world");
		addMSG ("wth_region", "%1% - set weather state for defined WorldGuard region");
		addMSG ("wth_player", "%1% - set weather state for defined player");
		addMSG ("wth_enabled", "Local weather feature: %1%. You need to restart server to take effect.");
		addMSG ("wth_sorrydisabled", "Action declined. Type %1% and restart server to enable local weather features.");
		
	}
}
