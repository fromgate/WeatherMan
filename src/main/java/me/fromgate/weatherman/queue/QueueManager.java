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

package me.fromgate.weatherman.queue;

import java.util.ArrayList;
import java.util.List;

import me.fromgate.weatherman.WMWorldEdit;
import me.fromgate.weatherman.WeatherMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.fromgate.weatherman.Util;

public class QueueManager {

	private static Util u(){
		return WeatherMan.instance.u;
	}

	private static List<Queue> queues = new ArrayList<Queue>();

	public static boolean addQueue(Queue queue){
		queues.add(queue);
		restartQueues();
		return true;
	}

	public static boolean addQueue(CommandSender sender, List<BiomeBlock> blocks, boolean biomeOrPopulate, Biome filterBiome){
		if (blocks == null) return false;
		if (blocks.isEmpty()) return false;
		Queue queue = new Queue(sender, blocks,biomeOrPopulate, filterBiome);
		return addQueue(queue);
	}

	public static boolean addQueue(CommandSender sender, Location loc1, Location loc2, Biome biome, boolean biomeOrPopulate, Biome filterBiome){
		if (loc1 == null) return u().returnMSG(false,sender, "msg_wronglocation",'c');
		if (loc2 == null) return u().returnMSG(false,sender, "msg_wronglocation",'c');
		List<BiomeBlock> blocks = new ArrayList<BiomeBlock>();
		for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x<=Math.max(loc1.getBlockX(), loc2.getBlockX()); x++)
			for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z<=Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++)
				blocks.add(new BiomeBlock (loc1.getWorld(),x,z,biome));
		return addQueue (sender,blocks,biomeOrPopulate,filterBiome);
	}

	public static boolean addQueueSelection(CommandSender sender, Biome biome, boolean biomeOrPopulate, Biome filterBiome){
		Player player = null;
		if (sender instanceof Player) player = (Player) sender; 
		if (player == null) return u().returnMSG (true,sender, "msg_cmdneedplayer",'c');
		if (!WMWorldEdit.isWE()) return u().returnMSG(true, player, "need_worldedit",'c');
		if (!WMWorldEdit.isSelected(player)) return u().returnMSG(true, player, "msg_selectregion",'c');
		Location loc1 = WMWorldEdit.getSelectionMinPoint(player);
		Location loc2 = WMWorldEdit.getSelectionMaxPoint(player);		
		return addQueue (sender, loc1,loc2,biome, biomeOrPopulate, filterBiome);
	}


	public static boolean addQueueRegion(CommandSender sender, String region, Biome biome, boolean biomeOrPopulate, Biome filterBiome){
		if (!WMWorldEdit.isWG()) return u().returnMSG(true,sender, "wg_notfound",'c');
		if (region.isEmpty()) u().returnMSG(true,sender, "wg_unknownregion",'c','4',"<empty>");
		int rgcount=0;
		for (World w : Bukkit.getWorlds()){
			if (WMWorldEdit.isRegionExists(w, region)){
				Location loc1 = WMWorldEdit.getMinPoint(w, region);
				Location loc2 = WMWorldEdit.getMaxPoint(w, region);
				if (addQueue (sender, loc1,loc2,biome, biomeOrPopulate, filterBiome)) rgcount ++;
			}
		}
		if (rgcount ==0) return u().returnMSG(false,sender, "wg_unknownregion",'c','4',region);		
		return true; 
	}


	public static boolean addQueue(CommandSender sender, Location loc, int radius, Biome biome, boolean biomeOrPopulate, Biome filterBiome){
		if (loc==null) return u().returnMSG(false,sender, "msg_wronglocation",'c');
		List<BiomeBlock> blocks = new ArrayList<BiomeBlock>();
		if (radius<=0) blocks.add(new BiomeBlock (loc,biome));
		else for (int i = 0; i<=radius; i++){
			int mj = (int) Math.sqrt(radius*radius-i*i);
			for (int j = 0; j<=mj; j++){
				blocks.add(new BiomeBlock (loc.getBlock().getLocation().add(i, 0, j),biome));
				blocks.add(new BiomeBlock (loc.getBlock().getLocation().add(-i, 0, j),biome));
				blocks.add(new BiomeBlock (loc.getBlock().getLocation().add(-i, 0, -j),biome));
				blocks.add(new BiomeBlock (loc.getBlock().getLocation().add(i, 0, -j),biome));
			}
		}
		return addQueue (sender, blocks, biomeOrPopulate, filterBiome);
	}



	public static void restartQueues(){
		Bukkit.getScheduler().runTaskLater(WeatherMan.instance, new Runnable(){
			@Override
			public void run() {
				if (queues.isEmpty()) return;
				boolean started = false;
				for (int i = queues.size()-1; i>=0; i--){
					if (queues.get(i).isActive()) started = true;
					if (queues.get(i).isFinished()) queues.remove(i);
				}
				if (!started && queues.size()>0) queues.get(0).processQueue();
			}
		}, 3);
	}



}
