/*  
 *  WeatherMan, Minecraft bukkit plugin
 *  ©2012-2018, fromgate, fromgate@gmail.com
 *  https://www.spigotmc.org/resources/weatherman.43379/
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

package me.fromgate.weatherman.util;

import me.fromgate.weatherman.WeatherMan;
import me.fromgate.weatherman.queue.QueueManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.BlockIterator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Repopulator {
    private static int worldHeight = 128;
    private static final Set<String> clearBlocks = new HashSet<>();
    private static final Set<String> treeBlocks = new HashSet<>();

    public static void save() {
        File f = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "repopulator.yml");
        YamlConfiguration cfg = new YamlConfiguration();
        List<String> clearBlocksList = new ArrayList<>();
        clearBlocksList.addAll(clearBlocks);
        List<String> treeBlocksList = new ArrayList<>();
        treeBlocksList.addAll(treeBlocks);
        cfg.set("depopulator.clear-max-height", worldHeight);
        cfg.set("depopulator.all-natural-blocks", clearBlocksList);
        cfg.set("depopulator.tree-blocks", treeBlocksList);
        try {
            cfg.save(f);
        } catch (Exception ignored) {
        }
    }

    public static void load() {
        File f = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "repopulator.yml");
        YamlConfiguration cfg = new YamlConfiguration();
        try {
            cfg.load(f);
            worldHeight = cfg.getInt("depopulator.clear-max-height", worldHeight);
            List<String> clearBlocksList = cfg.getStringList("depopulator.all-natural-blocks");
            clearBlocks.addAll(clearBlocksList);
            List<String> treeBlocksList = cfg.getStringList("depopulator.depopulator.tree-blocks");
            treeBlocks.addAll(treeBlocksList);
        } catch (Exception ignored) {
        }
        if (clearBlocks.isEmpty()) {
            String clearBlockList = "LOG,LOG_2,LEAVES,LEAVES_2,LONG_GRASS,DEAD_BUSH," +
                    "YELLOW_FLOWER,RED_ROSE,BROWN_MUSHROOM,RED_MUSHROOM,SNOW,CACTUS," +
                    "SUGAR_CANE_BLOCK,PUMPKIN,HUGE_MUSHROOM_1,HUGE_MUSHROOM_2," +
                    "MELON_BLOCK,VINE,COCOA,PACKED_ICE,WATER_LILY,DOUBLE_PLANT,SNOW";
            Collections.addAll(clearBlocks, clearBlockList.split(","));
        }
        if (treeBlocks.isEmpty()) {

            Tag.LOGS.getValues().forEach(log -> {
                treeBlocks.add(log.name());
            });

            Tag.LEAVES.getValues().forEach(leaves -> {
                treeBlocks.add(leaves.name());
            });

            treeBlocks.add(Material.VINE.name());
        }
    }

    public static void init() {
        load();
        save();

    }

    public static boolean populateCommand(CommandSender sender, Map<String, String> params) {
        if (params.isEmpty()) return populateSelection(sender);
        int radius = Math.min(ParamUtil.getParam(params, "radius", -1), Cfg.maxRadiusCmd);
        Location loc1 = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc", ParamUtil.getParam(params, "loc1", "")));
        Location loc2 = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc2", ""));

        if (radius > 0) {
            if (loc1 == null) return populateRadius(sender, radius);
            else return populateRadius(sender, loc1, radius);
        }

        if (loc1 != null && loc2 != null) return populateArea(sender, loc1, loc2);

        return populateRegion(sender, ParamUtil.getParam(params, "region", ""));
    }

    private static boolean populateArea(CommandSender sender, Location loc1, Location loc2) {
        return QueueManager.addQueue(sender, loc1, loc2, null, false, null);
    }

    public static boolean populateSelection(CommandSender sender) {
        if (sender instanceof Player) return QueueManager.addQueueSelection(sender, null, false, null);
        return false;
    }

    public static boolean populateRegion(CommandSender sender, String region) {
        return QueueManager.addQueueRegion(sender, region, null, false, null);
    }

    public static boolean populateRadius(CommandSender sender, int radius) {
        if (sender instanceof Player)
            return QueueManager.addQueue(sender, ((Player) sender).getLocation(), radius, null, false, null);
        return false;
    }

    public static boolean populateRadius(CommandSender sender, Location loc, int radius) {
        return QueueManager.addQueue(sender, loc, radius, null, false, null);
    }

    public static void rePopulate(Chunk chunk) {
        NmsUtil.repopulateChunk(chunk);
    }

    /*
    public static int findTree (Chunk chunk) {
        int count = 0;
        for (int x = 0; x<16; x++) {
            for (int z = 0; z < 16; z++){
                for (int y = 64; y<worldHeight; y++) {
                    Block b = chunk.getBlock(x, y, z);
                    b.setType(b.getType());
                    if (clearBlocks.contains(b.getType().name())) {
                        count++;
                    }
                }
            }
        }
        return count;
    } */


    public static void rePopulate(Set<Chunk> chunks) {
        for (Chunk chunk : chunks) rePopulate(chunk);
    }

    public static void depopulateColumn(Location loc) {
        depopulateColumn(loc.getWorld(), loc.getBlockX(), loc.getBlockZ());
    }

    @SuppressWarnings("deprecation")
    public static void depopulateColumn(World world, int x, int z) {
        Block b = world.getBlockAt(x, worldHeight, z);
        if (clearBlocks.contains(b.getType().name())) {
            b.setType(Material.AIR, false);
        }
        do {
            b = b.getRelative(BlockFace.DOWN);
            if (clearBlocks.contains(b.getType().name())) {
                b.setType(Material.AIR, false);
            }
            if (b.getY() == 0) break;
        } while (b.getType() == Material.AIR);
    }

    public static void depopulateColumnTree(Location loc) {
        depopulateColumnTree(loc.getWorld(), loc.getBlockX(), loc.getBlockZ());
    }

    @SuppressWarnings("deprecation")
    public static void depopulateColumnTree(World world, int x, int z) {
        Block b = world.getBlockAt(x, worldHeight, z);
        if (clearBlocks.contains(b.getType().name())) {
            b.setType(Material.AIR, false);
        }
        do {
            b = b.getRelative(BlockFace.DOWN);
            if (clearBlocks.contains(b.getType().name())) {
                Repopulator.depopulateNatural(b.getLocation(), true);
                b.setType(Material.AIR, false);
            }
            if (b.getY() == 0) break;
        } while (b.getType() == Material.AIR);
    }

    public static void depopulateColumns(List<Location> locs) {
        for (Location loc : locs) {
            depopulateColumn(loc.getWorld(), loc.getBlockX(), loc.getBlockZ());
        }
    }

    public static void depopulateArea(Location loc1, Location loc2) {
        for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <= Math.max(loc1.getBlockX(), loc2.getBlockX()); x++)
            for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z <= Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++) {
                depopulateColumn(loc1.getWorld(), x, z);
            }
    }

    public static boolean isClearBlock(Block block, boolean treesOnly) {
        if (treesOnly) return treeBlocks.contains(block.getType().name());
        return clearBlocks.contains(block.getType().name());
    }

    public static boolean depopulateNatural(Location loc, int radius) {
        if (loc == null) return false;
        if (radius <= 0) depopulateColumnTree(loc);
        else {
            List<Location> locs = new ArrayList<>();
            for (int i = 0; i <= radius; i++) {
                int mj = (int) Math.sqrt(radius * radius - i * i);
                for (int j = 0; j <= mj; j++) {
                    locs.add(loc.getBlock().getLocation().add(i, 0, j));
                    locs.add(loc.getBlock().getLocation().add(-i, 0, j));
                    locs.add(loc.getBlock().getLocation().add(-i, 0, -j));
                    locs.add(loc.getBlock().getLocation().add(i, 0, -j));
                }
            }
            for (Location depopLoc : locs) Repopulator.depopulateColumnTree(depopLoc);
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public static boolean depopulateNatural(Location loc, boolean treesOnly) {
        Set<Block> blocks = getNaturalBlocks(loc, treesOnly);
        if (blocks.isEmpty()) return false;
        for (Block block : blocks) {
            block.setType(Material.AIR, false);
        }
        return true;
    }

    public static void addNewBlock(Set<Block> blocks, Set<Block> newblocks, Block block, BlockFace relative, boolean treeOnly) {
        Block newblock = block.getRelative(relative);
        if (!isClearBlock(newblock, treeOnly)) return;
        if (blocks.contains(newblock)) return;
        newblocks.add(newblock);
    }

    public static Set<Block> getNaturalBlocks(Location loc, boolean treesOnly) {
        Set<Block> blocks = new HashSet<>();
        addNewBlock(blocks, blocks, loc.getBlock(), BlockFace.SELF, treesOnly);
        Set<Block> newblocks;
        do {
            newblocks = new HashSet<>();
            for (Block b : blocks) {
                addNewBlock(blocks, newblocks, b, BlockFace.UP, treesOnly);
                addNewBlock(blocks, newblocks, b, BlockFace.DOWN, treesOnly);
                addNewBlock(blocks, newblocks, b, BlockFace.EAST, treesOnly);
                addNewBlock(blocks, newblocks, b, BlockFace.WEST, treesOnly);
                addNewBlock(blocks, newblocks, b, BlockFace.NORTH, treesOnly);
                addNewBlock(blocks, newblocks, b, BlockFace.SOUTH, treesOnly);
            }
            blocks.addAll(newblocks);
        } while (!newblocks.isEmpty());
        return blocks;
    }

    public static Block getSnowballHitBlock(Snowball snowBall) {
        BlockIterator iterator = new BlockIterator(snowBall.getWorld(), snowBall.getLocation().toVector(), snowBall.getVelocity().normalize(), 0.0D, 4);
        Block block = null;
        while (iterator.hasNext()) {
            block = iterator.next();
            if (block.getType() != Material.AIR) return block;
        }
        return snowBall.getLocation().getBlock();
    }

    public static void generateTree(Location loc) {
        loc.getWorld().generateTree(loc, TreeType.TREE);
    }
}