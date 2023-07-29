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

import me.fromgate.weatherman.WeatherMan;
import me.fromgate.weatherman.util.lang.M;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


//import Repopulator;


public class Queue {


    // Config
    private boolean active = false;
    private boolean finished = false;
    private final int chunksPerTick = 32;
    private final CommandSender sender;
    private boolean setBiomeNotRepopulate = true;
    private Biome filterBiome = null;

    //Staticstic
    long startTime = 0L;
    int chunkNum = 0;
    int blockNum = 0;


    List<BiomeBlock> queue = new ArrayList<>();
    HashMap<WMChunk, Set<BiomeBlock>> queueSource = new HashMap<>();

    public Queue(CommandSender sender, boolean setBiomeNotRepopulate) {
        this.sender = sender;
        this.setBiomeNotRepopulate = setBiomeNotRepopulate;
    }

    public Queue(CommandSender sender, List<BiomeBlock> biomeBlocks, Biome filterBiome) {
        this(sender, biomeBlocks, true, filterBiome);
    }

    public Queue(CommandSender sender, List<BiomeBlock> biomeBlocks, boolean setBiomeNotRepopulate, Biome filterBiome) {
        this(sender, setBiomeNotRepopulate);
        this.filterBiome = filterBiome;
        addBiomeBlock(biomeBlocks);

    }

    public void addBiomeBlock(BiomeBlock biomeBlock) {
        if (!this.queueSource.containsKey(biomeBlock.wmChunk))
            this.queueSource.put(biomeBlock.wmChunk, new HashSet<>());
        this.queueSource.get(biomeBlock.wmChunk).add(biomeBlock);
    }

    public void addBiomeBlock(List<BiomeBlock> biomeBlocks) {
        for (BiomeBlock biomeBlock : biomeBlocks) {
            addBiomeBlock(biomeBlock);
        }
    }


    public void processQueue() {
        if (finished) return;
        if (!active) {
            active = true;
            startTime = System.currentTimeMillis();
            chunkNum = queueSource.size();
            for (WMChunk chunk : queueSource.keySet()) {
                blockNum += queueSource.get(chunk).size();
            }
        }

        Set<WMChunk> chunksToProcess = new HashSet<>();
        for (WMChunk wmChunk : queueSource.keySet()) {
            chunksToProcess.add(wmChunk);
            for (BiomeBlock biomeBlock : queueSource.get(wmChunk)) {
                if ((this.filterBiome != null) && (!biomeBlock.getLocation().getBlock().getBiome().equals(this.filterBiome)))
                    continue;
                biomeBlock.processBiomeBlock(this.setBiomeNotRepopulate); //////////////////
            }
            if (chunksToProcess.size() >= this.chunksPerTick) break;
        }
        for (WMChunk wmChunk : chunksToProcess) {
            wmChunk.processChunk(this.setBiomeNotRepopulate);
            this.queueSource.remove(wmChunk);
        }

        if (!chunksToProcess.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(WeatherMan.getPlugin(), this::processQueue, 3);

        } else {
            this.finished = true;
            this.active = false;
            if (sender != null) {
                long time = System.currentTimeMillis() - this.startTime;
                float seconds = ((float) time) / 1000;
                int minutes = (int) seconds / 60;
                String timeStr = (minutes > 0) ? M.MINSEC.getText(minutes, (int) seconds) :
                        M.SEC.getText(((float) Math.round(seconds * 1000 / 1000)) / 1000);
                if (this.setBiomeNotRepopulate)
                    M.MSG_QUEUEBIOMEFINISH.print(sender, timeStr, this.chunkNum, this.blockNum);
                else
                    M.MSG_QUEUEPOPULATEFINISH.print(sender, timeStr, this.chunkNum, this.blockNum);
                QueueManager.restartQueues();
            }
        }
    }


    public boolean isActive() {
        return this.active;
    }

    public boolean isFinished() {
        return this.finished;
    }


}
