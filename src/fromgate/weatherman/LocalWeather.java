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


package fromgate.weatherman;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class LocalWeather implements Listener {
	WeatherMan plg;

	private HashMap<String,Boolean> regions = new HashMap<String,Boolean>(); // true  - дождь
	private HashMap<String,Boolean> biomes = new HashMap<String,Boolean>();  
	private HashMap<String,Boolean> worlds = new HashMap<String,Boolean>();  

	public LocalWeather (WeatherMan plg){
		this.plg = plg;
		this.loadLocalWeather();
	}

	public boolean getRain (Player p){
		int r = PlayerConfig.getPersonalWeather(p);
		if (r<0) r = getRegionRain (p);
		if (r<0) r = getBiomeRain(p.getLocation().getBlock().getBiome());
		if (r==0) return false;
		if (r==1) return true;
		return getWorldRain(p.getWorld());
	}

	public boolean getRain (Player p, boolean world_to_rain){
		int r = PlayerConfig.getPersonalWeather(p);
		if (r<0) r = getRegionRain (p);
		if (r<0) r = getBiomeRain(p.getLocation().getBlock().getBiome());
		if (r==0) return false;
		if (r==1) return true;
		return world_to_rain;
	}

	public boolean getRain (Location loc){
		int r = getRegionRain (loc);
		if (r<0) r = getBiomeRain(loc.getBlock().getBiome());
		if (r==0) return false;
		if (r==1) return true;
		return getWorldRain(loc.getWorld());		
	}

	public void sendWeather(Player p, boolean rain){
		WeatherType newpw = rain ? WeatherType.DOWNFALL : WeatherType.CLEAR;
		if (p.getPlayerWeather()!=newpw) p.setPlayerWeather(newpw);
	}

	/*
	 * Player 
	 */
	public void setPlayerRain (Player p, boolean rain){
		if (PlayerConfig.isWeatherChanged(p, rain)) sendWeather(p, rain);
		PlayerConfig.setPersonalWeather(p, rain);
	}

	public void clearPlayerRain (Player p){
		PlayerConfig.removePersonalWeather(p);
		sendWeather(p, getRain(p));
	}

	/*
	 * Biome Weather 
	 */
	public void setBiomeRain (Biome biome, boolean rain){
		setBiomeRain (BiomeTools.biome2Str(biome),rain);
	}

	public void setBiomeRain (String biome, boolean rain){
		biomes.put(biome, rain);
		saveLocalWeather();
	}

	public void clearBiomeRain (Biome biome){
		clearBiomeRain (BiomeTools.biome2Str(biome));
	}

	public void clearBiomeRain (String biome){
		if (biomes.containsKey(biome)) biomes.remove(biome);
		saveLocalWeather();
	}

	//0 - clear, 1 - rain, -1 - error/default
	public int getBiomeRain(Biome biome){
		if (biome==null) return -1;
		return getBiomeRain(BiomeTools.biome2Str(biome));		
	}

	public int getBiomeRain(String biome){
		if (!biomes.containsKey(biome)) return -1;
		if (biomes.get(biome)) return 1;
		return 0;
	}

	/*
	 * Regions 
	 */
	//0 - clear, 1 - rain, -1 - error/default
	public int getRegionRain (Player p){
		return getRegionRain(p.getLocation());
	}


	public int getRegionRain (Location loc){
		List<String> rgList = WMWorldEdit.getRegions(loc);
		for (String rgStr : rgList)
			if (regions.containsKey(rgStr)) return (regions.get(rgStr) ? 1 : 0); 
		return -1;
	}


	//0 - clear, 1 - rain, -1 - error/default
	public int getRegionRain (String region){
		if (!regions.containsKey(region)) return -1;
		if (regions.get(region)) return 1;
		return 0;
	}

	public void setRegionRain (String region, boolean rain){
		regions.put(region, rain);
		saveLocalWeather();
	}

	public void clearRegionRain (String region){
		if (regions.containsKey(region)) regions.remove(region);
		saveLocalWeather();
	}

	/*
	 * World weather
	 */
	//0 - clear, 1 - rain, -1 - error/default
	public boolean getWorldRain(String world){
		World w = Bukkit.getWorld(world);
		if (w == null) w = Bukkit.getWorlds().get(0); // if given wrong world, will use first world. Not good solution, but better than NPE
		return getWorldRain (w);
	}

	public boolean getWorldRain(World world){
		String w = world.getName();
		if (worlds.containsKey(w)) return worlds.get(w);
		else return world.hasStorm();
	}

	public void setWorldRain (World world, boolean rain){
		setWorldRain (world.getName(), rain);
		world.setStorm(rain);
	}

	public void setWorldRain (String world, boolean rain){
		worlds.put(world, rain);
		saveLocalWeather();
	}

	public void clearWorldRain(World world){
		clearWorldRain (world.getName());
		updatePlayersRain(world, 10);
	}

	public void clearWorldRain(String world){
		if (worlds.containsKey(world)) worlds.remove(world);
		saveLocalWeather();
	}

	/*
	 * Listeners
	 */
	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onWeatherChange (WeatherChangeEvent event){
		if (!worlds.containsKey(event.getWorld().getName())){
			updatePlayersRain(event.getWorld(),20,event.toWeatherState());
		} else {
			final boolean worldstorm = worlds.get(event.getWorld().getName());
			if (event.toWeatherState() != worldstorm) event.setCancelled(true);
			else updatePlayersRain(event.getWorld(),20,worldstorm);
		}

	}

	public void updatePlayersRain(final World w, int delay, boolean to_weather){
		final boolean to_wstate = to_weather;
		Bukkit.getScheduler().runTaskLater(plg, new Runnable(){
			public void run(){
				for (Player p : w.getPlayers()){
					boolean newrain = getRain (p,to_wstate);
					sendWeather(p, newrain);
				}
			}
		}, delay);
	}

	public void updatePlayersRain(final World w, int delay){
		Bukkit.getScheduler().runTaskLater(plg, new Runnable(){
			public void run(){
				for (Player p : w.getPlayers()){
					boolean newrain = getRain (p);
					sendWeather(p, newrain);
				}
			}
		}, delay);
	}

	public void updatePlayerRain(Player p){
		boolean newrain = getRain(p);
		if (PlayerConfig.isWeatherChanged(p, newrain))
			sendWeather (p,newrain);
	}

	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerMove (PlayerMoveEvent event){
		updatePlayerRain (event.getPlayer());
	}

	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerJoin (PlayerJoinEvent event){
		updatePlayerRain (event.getPlayer());
	}

	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerRespawn (PlayerRespawnEvent event){
		updatePlayerRain (event.getPlayer());
	}

	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleport (PlayerTeleportEvent event){
		updatePlayerRain (event.getPlayer());
	}


	public void saveLocalWeather(){
		try {
			File f = new File (plg.getDataFolder()+File.separator+"localweather.yml");
			if (f.exists()) f.delete();
			f.createNewFile();
			YamlConfiguration cfg = new YamlConfiguration();
			if (worlds.size()>0){
				for (String wname : worlds.keySet())
					cfg.set("worlds."+wname, worlds.get(wname));
			}
			if (biomes.size()>0){
				for (String b : biomes.keySet())
					cfg.set("biomes."+b, biomes.get(b));
			}
			if (regions.size()>0){
				for (String r : regions.keySet())
					cfg.set("regions."+r, regions.get(r));
			}
			cfg.save(f);
		} catch (Exception e){
		}
	}

	public void loadLocalWeather(){
		try {
			File f = new File (plg.getDataFolder()+File.separator+"localweather.yml");
			if (f.exists()){
				YamlConfiguration cfg = new YamlConfiguration();
				cfg.load(f);
				worlds.clear();
				biomes.clear();
				regions.clear();
				for (String key : cfg.getKeys(true)){
					if (key.contains(".")){
						String [] kln = key.split("\\.");
						if (kln.length==2){
							String type = kln[0];
							String keyfield = kln[1];
							if (type.equalsIgnoreCase("worlds")) worlds.put(keyfield, cfg.getBoolean(key));
							if (type.equalsIgnoreCase("biomes")) biomes.put(keyfield, cfg.getBoolean(key));
							if (type.equalsIgnoreCase("regions")) regions.put(keyfield, cfg.getBoolean(key));
						}
					}
				}
			}
		} catch (Exception e){
		}

	}

	public void printPlayerList(Player p, int page){
		List<String> plst = new ArrayList<String>();
		for (Player pp : Bukkit.getOnlinePlayers()){
			if (!pp.isOnline()) continue;
			int pw = PlayerConfig.getPersonalWeather(pp);
			if (pw>=0)
				plst.add("&6"+p.getName()+"&e : "+((pw==1) ? plg.u.getMSGnc("rain") : plg.u.getMSGnc("clear")));
		}
		if (plst.size()>0) plg.u.printPage(p, plst, page, "wth_playerlist", "", true);
		else plg.u.printMSG(p, "wth_playerlistempty",'c');
	}

	public void printBiomeList(Player p, int page){
		if (biomes.size()>0){
			List<String> blst = new ArrayList<String>();
			for (String b : biomes.keySet())
				blst.add("&6"+b+"&e : "+((biomes.get(b)) ? plg.u.getMSGnc("rain") : plg.u.getMSGnc("clear")));
			plg.u.printPage(p, blst, page, "wth_biomelist", "", true);
		} else plg.u.printMSG(p, "wth_biomelistempty",'c'); 
	}

	public void printRegionList(Player p, int page){
		if (regions.size()>0){
			List<String> blst = new ArrayList<String>();
			for (String b : regions.keySet())
				blst.add("&6"+b+"&e : "+((regions.get(b)) ? plg.u.getMSGnc("rain") : plg.u.getMSGnc("clear")));
			plg.u.printPage(p, blst, page, "wth_regionlist", "", true);
		} else plg.u.printMSG(p, "wth_regionlistempty",'c'); 
	}

	public void printWorldList(Player p, int page){
		if (worlds.size()>0){
			List<String> blst = new ArrayList<String>();
			for (String b : worlds.keySet())
				blst.add("&6"+b+"&e : "+((worlds.get(b)) ? plg.u.getMSGnc("rain") : plg.u.getMSGnc("clear")));
			plg.u.printPage(p, blst, page, "wth_worldslist", "", true);
		} else plg.u.printMSG(p, "wth_worldlistempty",'c'); 
	}
}
