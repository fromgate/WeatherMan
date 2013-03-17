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

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class WMBiomeBlock {
	World world;
	int x;
	int z;
	Biome biome;

	public WMBiomeBlock (Location loc, Biome b){
		this.world=loc.getWorld();
		this.x=loc.getBlockX();
		this.z=loc.getBlockZ();
		this.biome = b;
	}


	public WMBiomeBlock (World world, int x, int z, Biome b){
		this.world=world;
		this.x=x;
		this.z=z;
		this.biome = b;
	}
	
	public void setBiome(){
		Biome b = this.biome;
		if (this.biome == null) b = WMSysTools.getOriginalBiome (this.x, this.z, this.world);
		if (b!=null) this.world.setBiome(this.x, this.z, b);
	}
	
	public Location getLocation(){
		return new Location (world, x,0,z);
	}
	
	public boolean isInChunk (Chunk ch){
		return (getLocation().getChunk().equals(ch));
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WMBiomeBlock))
			return false;
		WMBiomeBlock other = (WMBiomeBlock) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}


}
