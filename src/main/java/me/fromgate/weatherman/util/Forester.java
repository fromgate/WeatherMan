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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Snowball;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Forester {

    private static final Map<String, String> biomeTrees = new HashMap<>();
    private static Random random;

    public static void init() {
        load();
        if (biomeTrees.isEmpty()) {
            initDefaultTrees();
        }
        save();
        random = new Random();
    }

    public static void load() {
        YamlConfiguration cfg = new YamlConfiguration();
        File f = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "forester-brush.yml");
        if (!f.exists()) return;
        biomeTrees.clear();
        try {
            cfg.load(f);
            for (String key : cfg.getKeys(false)) {
                biomeTrees.put(key, cfg.getString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void save() {
        YamlConfiguration cfg = new YamlConfiguration();
        File f = new File(WeatherMan.getPlugin().getDataFolder() + File.separator + "forester-brush.yml");
        if (f.exists()) f.delete();
        try {
            f.createNewFile();
            for (String biomeStr : biomeTrees.keySet()) {
                if (BiomeTools.isBiomeExists(biomeStr))
                    cfg.set(biomeStr, biomeTrees.get(biomeStr));
            }
            cfg.save(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isTreeExists(String treeStr, boolean defrnd) {
        if (treeStr.equalsIgnoreCase("default")) return defrnd;
        else if (treeStr.equalsIgnoreCase("random")) return defrnd;
        else {
            for (TreeType treeType : TreeType.values())
                if (treeType.name().equalsIgnoreCase(treeStr)) return true;
        }
        return false;
    }

    public static TreeType getTreeByBiome(Biome biome) {
        if (biome == null) return TreeType.TREE;
        String biomeStr = biome.name();
        if (!biomeTrees.containsKey(biomeStr.toUpperCase())) return TreeType.TREE;
        String treesStr = biomeTrees.get(biomeStr.toUpperCase());
        String[] treeLn = treesStr.split(",");
        if (treeLn.length == 0) return TreeType.TREE;
        String treeStr = treeLn[random.nextInt(treeLn.length)];
        return getTreeByName(treeStr, biome);
    }

    public static TreeType getTreeByName(String treeName, Biome biome) {
        if (treeName.equalsIgnoreCase("random")) return getRandomTree();
        else if (treeName.equalsIgnoreCase("default")) return getTreeByBiome(biome);
        else
            for (TreeType tree : TreeType.values()) {
                if (tree.name().equalsIgnoreCase(treeName)) return tree;
            }
        return TreeType.TREE;
    }

    public static TreeType getRandomTree() {
        return TreeType.values()[random.nextInt(TreeType.values().length)];
    }


    public static void initDefaultTrees() {
        biomeTrees.put("BEACH", "");
        biomeTrees.put("BIRCH_FOREST", "BIRCH,TALL_BIRCH");
        biomeTrees.put("BIRCH_FOREST_HILLS", "BIRCH,TALL_BIRCH");
        biomeTrees.put("BIRCH_FOREST_HILLS_MOUNTAINS", "BIRCH");
        biomeTrees.put("BIRCH_FOREST_MOUNTAINS", "BIRCH,TALL_BIRCH");
        biomeTrees.put("COLD_BEACH", "TREE");
        biomeTrees.put("COLD_TAIGA", "REDWOOD,TALL_REDWOOD,MEGA_REDWOOD");
        biomeTrees.put("COLD_TAIGA_HILLS", "REDWOOD,TALL_REDWOOD");
        biomeTrees.put("COLD_TAIGA_MOUNTAINS", "REDWOOD");
        biomeTrees.put("DEEP_OCEAN", "TREE");
        biomeTrees.put("DESERT", "TREE");
        biomeTrees.put("DESERT_HILLS", "TREE");
        biomeTrees.put("DESERT_MOUNTAINS", "TREE");
        biomeTrees.put("EXTREME_HILLS", "REDWOOD,TREE");  //BIG_TREE
        biomeTrees.put("EXTREME_HILLS_MOUNTAINS", "");
        biomeTrees.put("EXTREME_HILLS_PLUS", "TREE");
        biomeTrees.put("EXTREME_HILLS_PLUS_MOUNTAINS", "TREE");
        biomeTrees.put("FLOWER_FOREST", "TREE,BIG_TREE");
        biomeTrees.put("FOREST", "TREE,BIRCH,BIG_TREE,TALL_BIRCH");
        biomeTrees.put("FOREST_HILLS", "TREE,BIRCH,BIG_TREE,TALL_BIRCH");
        biomeTrees.put("FROZEN_OCEAN", "TREE");
        biomeTrees.put("FROZEN_RIVER", "TREE");
        biomeTrees.put("HELL", "TREE");
        biomeTrees.put("ICE_MOUNTAINS", "TREE");
        biomeTrees.put("ICE_PLAINS", "TREE,REDWOOD");
        biomeTrees.put("ICE_PLAINS_SPIKES", "TREE");
        biomeTrees.put("JUNGLE", "SMALL_JUNGLE,JUNGLE,JUNGLE_BUSH");
        biomeTrees.put("JUNGLE_EDGE", "SMALL_JUNGLE,JUNGLE,JUNGLE_BUSH");
        biomeTrees.put("JUNGLE_EDGE_MOUNTAINS", "SMALL_JUNGLE,JUNGLE,JUNGLE_BUSH");
        biomeTrees.put("JUNGLE_HILLS", "SMALL_JUNGLE,JUNGLE,JUNGLE_BUSH");
        biomeTrees.put("JUNGLE_MOUNTAINS", "JUNGLE,JUNGLE_BUSH");
        biomeTrees.put("MEGA_SPRUCE_TAIGA", "REDWOOD,TALL_REDWOOD");
        biomeTrees.put("MEGA_SPRUCE_TAIGA_HILLS", "REDWOOD,TALL_REDWOOD");
        biomeTrees.put("MEGA_TAIGA", "REDWOOD,TALL_REDWOOD");
        biomeTrees.put("MEGA_TAIGA_HILLS", "REDWOOD,TALL_REDWOOD");
        biomeTrees.put("MESA", "TREE");
        biomeTrees.put("MESA_BRYCE", "TREE");
        biomeTrees.put("MESA_PLATEAU", "TREE");
        biomeTrees.put("MESA_PLATEAU_FOREST", "TREE");
        biomeTrees.put("MESA_PLATEAU_FOREST_MOUNTAINS", "");
        biomeTrees.put("MESA_PLATEAU_MOUNTAINS", "TREE");
        biomeTrees.put("MUSHROOM_ISLAND", "BROWN_MUSHROOM,RED_MUSHROOM");
        biomeTrees.put("MUSHROOM_SHORE", "BROWN_MUSHROOM,RED_MUSHROOM");
        biomeTrees.put("OCEAN", "TREE");
        biomeTrees.put("PLAINS", "TREE");
        biomeTrees.put("RIVER", "TREE");
        biomeTrees.put("ROOFED_FOREST", "DARK_OAK");
        biomeTrees.put("ROOFED_FOREST_MOUNTAINS", "DARK_OAK");
        biomeTrees.put("SAVANNA", "ACACIA");
        biomeTrees.put("SAVANNA_MOUNTAINS", "ACACIA");
        biomeTrees.put("SAVANNA_PLATEAU", "ACACIA");
        biomeTrees.put("SAVANNA_PLATEAU_MOUNTAINS", "ACACIA");
        biomeTrees.put("SKY", "TREE");
        biomeTrees.put("SMALL_MOUNTAINS", "TREE");
        biomeTrees.put("STONE_BEACH", "TREE");
        biomeTrees.put("SUNFLOWER_PLAINS", "TREE");
        biomeTrees.put("SWAMPLAND", "SWAMP");
        biomeTrees.put("SWAMPLAND_MOUNTAINS", "SWAMP");
        biomeTrees.put("TAIGA", "REDWOOD,TALL_REDWOOD");
        biomeTrees.put("TAIGA_HILLS", "REDWOOD,TALL_REDWOOD");
        biomeTrees.put("TAIGA_MOUNTAINS", "REDWOOD,TALL_REDWOOD");
    }

    public static void growTree(Location loc, String treeType) {
        TreeType tree = getTreeByName(treeType, loc.getBlock().getBiome());
        if (tree == null) tree = TreeType.TREE;
        if (loc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) loc = loc.add(0, -1, 0);
        loc.getWorld().generateTree(loc, tree);
    }


    public static void growTree(Snowball sb) {
        String treeType = "default";
        if (sb.hasMetadata("WeatherMan-forester"))
            treeType = sb.getMetadata("WeatherMan-forester").get(0).asString();
        growTree(sb.getLocation(), treeType);
    }

    public static String getTreeStr(CommandSender sender) {
        String treeStr = "";
        for (TreeType tree : TreeType.values())
            treeStr = treeStr.isEmpty() ? tree.name() : treeStr + ", " + tree.name();
        return treeStr;
    }


}
