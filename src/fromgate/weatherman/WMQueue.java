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
 *  along with WeatherMan.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package fromgate.weatherman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;


public class WMQueue {
	WeatherMan plg;
	HashMap<WMChunk, Set<WMBiomeBlock>> queue = new HashMap<WMChunk, Set<WMBiomeBlock>>();

	public WMQueue(WeatherMan plg){
		this.plg = plg;
		this.queue = new HashMap<WMChunk, Set<WMBiomeBlock>>();
	}

	public void clear(){
		this.queue.clear();
	}

	public void add(WMBiomeBlock bb){
		WMChunk wch = new WMChunk (bb.world, bb.x,bb.z);
		if (!this.queue.containsKey(wch)) this.queue.put(wch, new HashSet<WMBiomeBlock>());
		this.queue.get(wch).add(bb);
	}

	public void add(World w, int x, int z, Biome biome){
		WMBiomeBlock bb = new WMBiomeBlock (w,x,z,biome);
		add(bb);
	}


	public WMResult processQueue(){
		return processQueue(true);
	}
	public WMResult processQueue(boolean usesmoke){
		int chcnt = queue.size();
		int blckcnt = 0;
		for (WMChunk wch : this.queue.keySet()){
			boolean chunkloaded = wch.getChunk().isLoaded();
			World w = wch.world;
			for (WMBiomeBlock bb : this.queue.get(wch)){
				blckcnt++;
				bb.setBiome();
				if (usesmoke){
					if (plg.meltsnow||plg.meltice) plg.meltSnow(bb.world,bb.x,bb.z);
					if (plg.smoke&&(plg.u.random.nextInt(100)<plg.smoke_chance)){
						Location loc = bb.world.getHighestBlockAt(bb.x, bb.z).getLocation();
						if (plg.u.isPlayerAround(loc, 18)) w.playEffect(loc, Effect.SMOKE, 4);
					}
				}
			}
			w.refreshChunk(wch.x, wch.z);
			saveChunk(wch.getChunk());
			if (!chunkloaded) wch.getChunk().unload();
		}
		this.queue.clear();
		return new WMResult (blckcnt,chcnt); 
	}


	private void saveChunk(Chunk ch){
		WMSysTools.saveChunk(ch);
		//plg.saveChunk(ch);
	}

	/*public void meltSnow(World w, int x, int z){
		Block b = plg.getHighestBlock(w, x, z);
		if (!(plg.u.isWordInList(plg.Biome2Str(b.getBiome()), plg.coldbiomes))){
			if (plg.meltsnow&&(b.getType() == Material.SNOW)) b.setType(Material.AIR);
			else if ((plg.meltice&&b.getType()==Material.AIR)&&(b.getRelative(BlockFace.DOWN).getType()==Material.ICE)) {
				b.getRelative(BlockFace.DOWN).setType(Material.WATER);			
			}
		}
	}*/

	public boolean isChunkInQueue(Chunk ch){
		return this.queue.containsKey(new WMChunk (ch));
	}

	public void setBiomeChunk (Chunk ch){
		WMChunk wch = new WMChunk (ch);
		if (this.queue.containsKey(wch)){
			for (WMBiomeBlock bb : this.queue.get(wch)){
				bb.setBiome();
			}
			this.queue.remove(wch);
		}
	}


	// пока не используется
	public void processStepOfQueue(final WeatherMan plg, final boolean usesmoke, final List<WMBiomeBlock> wmblx, final int blockspersecond){
		if (wmblx.size()>0){
			Bukkit.getScheduler().runTaskLater(plg, new Runnable(){
				public void run(){
					int imin = Math.max(0, wmblx.size()-blockspersecond);
					for (int i = wmblx.size()-1; i>=imin;i--){
						WMBiomeBlock bb = wmblx.get(i);
						bb.setBiome();
						if (usesmoke){
							if (plg.meltsnow||plg.meltice) plg.meltSnow(bb.world,bb.x,bb.z);
							if (plg.smoke&&(plg.u.random.nextInt(100)<plg.smoke_chance)){
								Location loc = bb.world.getHighestBlockAt(bb.x, bb.z).getLocation();
								if (plg.u.isPlayerAround(loc, 18)) bb.world.playEffect(loc, Effect.SMOKE, 4);
							}
						}
						wmblx.remove(i);
					}
					if (wmblx.size()>0) processStepOfQueue(plg, usesmoke, wmblx, blockspersecond);
				}
			}, 30);
		}
	}
	
	// пока не используется	
	public void processQueueDelayed(boolean usesmoke, int blockspersecond){
		List<WMBiomeBlock> wmblx = new ArrayList<WMBiomeBlock>();
		for (WMChunk wch : queue.keySet())
			for (WMBiomeBlock bb : queue.get(wch))
				wmblx.add(bb);
		
		Bukkit.getServer().broadcastMessage(ChatColor.RED+"wmblx.size(): "+wmblx.size());
		if (wmblx.size()>0)
			processStepOfQueue(plg, usesmoke, wmblx, blockspersecond);
		this.queue.clear();
	}



}
