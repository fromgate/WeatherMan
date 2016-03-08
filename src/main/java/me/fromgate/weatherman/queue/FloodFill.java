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

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Biome;

public class FloodFill {
	
	public static Set<BiomeBlock> scanArea (Location loc, Biome toBiome){
		Biome fromBiome = loc.getBlock().getBiome();
		Set<BiomeBlock> result = new HashSet<BiomeBlock>();
		if (toBiome == fromBiome) return result;
		Set<BiomeBlock> scanFrom = new HashSet<BiomeBlock>();
		scanFrom.add(new BiomeBlock (loc,toBiome));
		do {
			Set<BiomeBlock> nextScan= new HashSet<BiomeBlock>();
			for (BiomeBlock scanBlock : scanFrom){
				Set<BiomeBlock> scanLine = scanLine (scanBlock, toBiome);
				result.addAll(scanLine);
				Set<BiomeBlock> next = findNext (scanLine, fromBiome);
				for (BiomeBlock nextBlock : next){
					if (!result.contains(nextBlock)) nextScan.add(nextBlock);
				}
				/*if ((next[0]!=null)&&(!result.contains(next[0]))) nextScan.add(next[0]);
				if ((next[1]!=null)&&(!result.contains(next[1]))) nextScan.add(next[1]);*/
			}
			scanFrom.clear();
			scanFrom.addAll(nextScan);
		} while (!scanFrom.isEmpty());
		return result;
	}
	
	
	public static Set<BiomeBlock> findNext(Set<BiomeBlock> line, Biome fromBiome){
		Set<BiomeBlock> tmp = new HashSet<BiomeBlock>();
		Set<BiomeBlock> next = new HashSet<BiomeBlock>();
		for (BiomeBlock biomeBlock : line){
			Location tmpLoc = biomeBlock.getLocation().add(0,0,1);
			if (tmpLoc.getBlock().getBiome() == fromBiome) tmp.add(new BiomeBlock (tmpLoc, biomeBlock.biome));
			tmpLoc = biomeBlock.getLocation().add(0,0,-1);
			if (tmpLoc.getBlock().getBiome() == fromBiome) tmp.add(new BiomeBlock (tmpLoc, biomeBlock.biome));
		}
		
		for (BiomeBlock bb : tmp){
			BiomeBlock b1 = new BiomeBlock (bb.getLocation().add(1, 0, 0),bb.biome);
			BiomeBlock b2 = new BiomeBlock (bb.getLocation().add(-1, 0, 0),bb.biome);
			if (!tmp.contains(b1)) next.add(bb);
			if (!tmp.contains(b2)) next.add(bb);
		}
		
		return next;
	}
	
	/*public static BiomeBlock[] getLeftRight(Set<BiomeBlock> line){
		BiomeBlock[] lnr = {null,null};
		
		if (line.isEmpty()) return lnr;
		for (BiomeBlock biomeBlock : line){
			if (lnr[0]==null) {
				lnr[0] = biomeBlock;
				lnr[1] = biomeBlock;
			}
			if (biomeBlock.x<lnr[0].x) lnr[0].x=biomeBlock.x;
			if (biomeBlock.x>lnr[1].x) lnr[1].x=biomeBlock.x;
		}
		return lnr;
		
	}*/

	
	public static Set<BiomeBlock> scanLine (BiomeBlock scanFrom, Biome toBiome){
		Set<BiomeBlock> line = new HashSet<BiomeBlock>();
		Location loc = scanFrom.getLocation();
		Biome fromBiome = loc.getBlock().getBiome();

		while (loc.getBlock().getBiome()==fromBiome){
			line.add(new BiomeBlock (loc,toBiome));
			loc = loc.add(1,0,0);
		}
		
		loc = scanFrom.getLocation();
		
		while (loc.getBlock().getBiome()==fromBiome){
			line.add(new BiomeBlock (loc,toBiome));
			loc = loc.add(-1,0,0);
		}
		
		
		/*while (loc.getBlock().getRelative(1, 0, 0).getBiome().equals(fromBiome)){
			line.add(new BiomeBlock (loc.getBlock().getRelative(1, 0, 0).getLocation(),toBiome));
			loc.add(1, 0, 0);
		}
		
		loc = scanFrom.getLocation();
		while (loc.getBlock().getRelative(-1, 0, 0).getBiome().equals(fromBiome)){
			line.add(new BiomeBlock (loc.getBlock().getRelative(-1, 0, 0).getLocation(),toBiome));
			loc.add(-1, 0, 0);
		} */
		
		return line;
	}
	
	

}
