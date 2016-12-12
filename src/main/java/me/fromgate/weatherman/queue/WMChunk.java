/*  
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012-2015, fromgate, fromgate@gmail.com
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

import me.fromgate.weatherman.util.NMSUtil;
import me.fromgate.weatherman.WeatherMan;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;


public class WMChunk {
    World world;
    int x;
    int z;


    public WMChunk(Chunk ch) {
        this.world = ch.getWorld();
        this.x = ch.getX();
        this.z = ch.getZ();
    }

    public WMChunk(World world, int bx, int bz, boolean setBiomeOrDepopulate) {
        this.world = world;
        this.x = bx >> 4;
        this.z = bz >> 4;
    }

    public Chunk getChunk() {
        return world.getChunkAt(this.x, this.z);
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
        if (!(obj instanceof WMChunk))
            return false;

        WMChunk other = (WMChunk) obj;
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


    public void processChunk(boolean setBiomeOrDepopulate) {
        Chunk chunk = getChunk();
        if (setBiomeOrDepopulate) {
            NMSUtil.saveChunk(chunk);
            //world.refreshChunk(this.x, this.z);
            NMSUtil.refreshChunk(chunk);
        } else {
            NMSUtil.repopulateChunk(chunk);
        }
    }

    public void entityUpdate(final Chunk chunk) {
        Bukkit.getScheduler().runTaskLater(WeatherMan.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Entity e : chunk.getEntities())
                    e.teleport(e);
            }
        }, 1);

    }


}
