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

package me.fromgate.weatherman;

import me.fromgate.weatherman.queue.BiomeBlock;
import me.fromgate.weatherman.queue.FloodFill;
import me.fromgate.weatherman.queue.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeTools {

	private static WeatherMan plg(){
		return WeatherMan.instance;
	}

	private static HashMap<String, Biome> bioms = new HashMap<String,Biome>(); //возможно оставить для алиасов?!
	private static String outdatedBiomes = "rainforest,seasonalforest,savanna,shrubland,icedesert,tundra";	//RAINFOREST, SEASONAL_FOREST, SAVANNA, SHRUBLAND, ICE_DESERT, TUNDRA - устаревшие биомы

	public static boolean replaceBiomeCommand(CommandSender sender, Map<String,String> params){
		Player player = null;
		if (sender instanceof Player) player = (Player) sender;
		if (params.isEmpty()) return false;
		String biomeStr = ParamUtil.getParam(params, "biome", "");
		if (!isBiomeExists(biomeStr)) return false;
		Location loc1 = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc", ParamUtil.getParam(params, "loc1", "")));
		Location loc2 = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc2", ""));
		Biome biome = BiomeTools.str2Biome(biomeStr);
		if (ParamUtil.getParam(params, "fill", false)){
			if (loc1!=null) return BiomeTools.floodFill(sender, loc1, biome);
			else if (player!=null) return BiomeTools.floodFill(sender, player.getLocation(), biome);
			else return false;
		}

		String sourceBiomeStr = ParamUtil.getParam(params, "source", "");
		if (!isBiomeExists(sourceBiomeStr)) return false;

		Biome sourceBiome = BiomeTools.str2Biome(sourceBiomeStr);
		int radius = Math.min(ParamUtil.getParam(params, "radius", -1), plg().maxRadiusCmd);
		if (radius>0){
			if (loc1!=null) return setBiomeRadius (sender, loc1, biome, radius, sourceBiome);
			else return setBiomeRadius (player, biome, radius, sourceBiome);	
		}
		if (loc1!=null&&loc2!=null) return setBiomeArea(sender, loc1, loc2, biome, sourceBiome);
		if (ParamUtil.isParamExists(params, "region")) return setBiomeRegion (sender, biome, ParamUtil.getParam(params, "region", ""),sourceBiome);
		return setBiomeSelection (sender, biome, sourceBiome);
	}

	public static boolean setBiomeCommand(CommandSender sender, Map<String,String> params){
		if (params.isEmpty()) return false;
		String biomeStr = ParamUtil.getParam(params, "biome", "");
		if (!isBiomeExists(biomeStr)) return false;
		Biome biome = BiomeTools.str2Biome(biomeStr);
		Location loc1 = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc", ParamUtil.getParam(params, "loc1", "")));
		Location loc2 = BiomeTools.parseLocation(ParamUtil.getParam(params, "loc2", ""));
		if (ParamUtil.isParamExists(params, "radius")){
			int radius = Math.min(ParamUtil.getParam(params, "radius", -1), plg().maxRadiusCmd);
			if (loc1==null)	return setBiomeRadius (sender, biome, radius,null);
			else return setBiomeRadius(sender, loc1, biome, radius, null);
		}
		if (loc1!=null&&loc2!=null) return BiomeTools.setBiomeArea(sender, loc1, loc2, biome, null);
		if (ParamUtil.isParamExists(params, "region")) return setBiomeRegion (sender, biome, ParamUtil.getParam(params, "region", ""),null);
		return setBiomeSelection (sender, biome, null);
	}

	public static boolean setBiomeRegion(CommandSender sender, Biome biome, String region, Biome replaceBiome) {
		return QueueManager.addQueueRegion(sender, region, biome, true, replaceBiome);
	}

	public static boolean setBiomeRadius(CommandSender sender, Biome biome,int radius, Biome replaceBiome) {
		if (sender instanceof Player) return QueueManager.addQueue(sender, ((Player) sender).getLocation(), radius, biome, true, replaceBiome);
		return plg().u.returnMSG(true, sender, "msg_cmdneedplayer",'c'); 
	}


	public static boolean setBiomeRadius(CommandSender sender, Location loc, Biome biome, int radius, Biome replaceBiome) {
		if (loc == null) return plg().u.returnMSG(true, sender, "msg_wronglocation",'c'); 
		return QueueManager.addQueue(sender, loc, radius, biome, true, replaceBiome);
	}

	public static boolean setBiomeSelection(CommandSender sender, Biome biome, Biome replaceBiome) {
		return QueueManager.addQueueSelection(sender, biome, true, replaceBiome);
	}

	public static boolean setBiomeArea(CommandSender sender, Location loc1, Location loc2, Biome biome, Biome replaceBiome) {
		return QueueManager.addQueue(sender, loc1, loc2, biome, true, replaceBiome);
	}

	public static boolean isBiomeExists(String biomeStr){
		if (biomeStr == null) return false;
		if (biomeStr.isEmpty()) return false;
		for (String biome : bioms.keySet())
			if (biome.equalsIgnoreCase(biomeStr)) return true;
		return false;
	}

	public static Biome str2Biome (String bs){
		if (bioms.containsKey(bs)) return bioms.get(bs);
		return null;
	} 

	public static String getBiomeList(String mask){
		List<String> cold = new ArrayList<String>();
		List<String> medium= new ArrayList<String>();
		List<String> warm = new ArrayList<String>();
		List<String> nullbiome = new ArrayList<String>();
		for (String key : bioms.keySet()){
			if (mask.isEmpty()||key.toLowerCase().contains(mask.toLowerCase())) {
				Biome b =  str2Biome(key);
				if (b==null) nullbiome.add(colorBiomeName(key));
				else if (getBiomeTemperature(b)==Temperature.COLD) cold.add(colorBiomeName(key));
				else if (getBiomeTemperature(b)==Temperature.MEDIUM) medium.add(colorBiomeName(key));
				else warm.add(colorBiomeName(key));
			}
		}
		List<String> blist = new ArrayList<String>();
		blist.addAll(cold);
		blist.addAll(medium);
		blist.addAll(warm);
		blist.addAll(nullbiome);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i<blist.size();i++){
			if (sb.toString().isEmpty()) sb.append(blist.get(i));
			else sb.append("&2, ").append(blist.get(i));
		}
		return sb.toString();
	}

	public enum Temperature{
		COLD,
		MEDIUM,
		WARM;
	}

	public static String colorBiomeName(String biomestr){
		Biome biome = str2Biome (biomestr);
		if (biome==null) return "&d"+biomestr;
		Temperature t = getBiomeTemperature(biome); 
		switch (t){
		case COLD: return "&9"+biomestr;
		case MEDIUM: return "&2"+biomestr;
		case WARM: return "&e"+biomestr;
		}
		return "&d"+biomestr;
	}

	public static String colorBiomeName(Biome biome){
		if (biome==null) return "&d"+biome2Str(biome);
		Temperature t = getBiomeTemperature(biome); 
		switch (t){
		case COLD: return "&9"+biome2Str(biome);
		case MEDIUM: return "&2"+biome2Str(biome);
		case WARM: return "&e"+biome2Str(biome);
		}
		return "&d"+biome2Str(biome);
	}

	private static Temperature getBiomeTemperature (Biome biome){
		double temperature = NMSUtil.getBiomeTemperature(biome);
		if (temperature<0.2) return Temperature.COLD;
		if (temperature<1.0) return Temperature.MEDIUM;
		return Temperature.WARM;
	}

	public static String biome2Str (Biome b){
		String bstr = "original";
		if (b!= null) bstr = b.name().toLowerCase();
		bstr = bstr.replaceAll(" ", "_");
		return bstr; 
	}

	public static void meltSnow(World w, int x, int z){
		Block b = getHighestBlock(w, x, z);
		if (getBiomeTemperature(b.getBiome())==Temperature.COLD) return;
		if (plg().meltSnow&&(b.getType()==Material.SNOW)) {
			b.setType(Material.AIR);
			b = getHighestBlock(w, x, z);
			if (b.getType()==Material.SNOW) b.setType(Material.AIR); 
		}
		else if (plg().meltIce&&(b.getType()==Material.ICE)) b.setType(Material.WATER);
	}

	public static void meltSnow (Location loc){
		meltSnow (loc.getWorld(), loc.getBlockX(), loc.getBlockZ());
	}

	@SuppressWarnings("deprecation")
	public static Block getHighestBlock (World w, int x, int z){
		Block b = w.getHighestBlockAt(x, z);
		while ((b.getY()>1)&&(plg().u.isIdInList(b.getTypeId(), "0,17,18"))){
			b = b.getRelative(BlockFace.DOWN);
		}
		return b;
	}

	public static void initBioms(){
		bioms.clear();
		bioms.put("original", null);
		Biome [] bm = Biome.values();
		if (bm.length>0){
			for (int i = 0; i<bm.length;i++){
				String bstr = BiomeTools.biome2Str (bm[i]);
				if (!(plg().u.isWordInList(bstr, outdatedBiomes))) bioms.put(bstr, bm[i]);
			}
		}
	}

	protected static boolean floodFill (CommandSender sender, Location loc, Biome toBiome){
		if (loc==null) return false;
		List<BiomeBlock> blocks = new ArrayList<BiomeBlock>();
		blocks.addAll(FloodFill.scanArea(loc, toBiome));
		if (blocks.isEmpty()) return false;
		return QueueManager.addQueue(sender, blocks, true,null);
	}

	public static boolean replaceBiome (CommandSender sender, Biome b1, Biome tobiome, Location loc1, Location loc2){
		List<BiomeBlock> blocks = new ArrayList<BiomeBlock>();
		Biome b2 = tobiome;
		World w = loc1.getWorld();
		for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x<=Math.max(loc1.getBlockX(), loc2.getBlockX()); x++)
			for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z<=Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++)
				if (w.getBiome(x, z).equals(b1)) blocks.add(new BiomeBlock (w, x, z, b2));
		return QueueManager.addQueue(sender, blocks, true,null);
	}

	public static String checkBiomes(String biomelist){
		String str = "";
		if (!biomelist.isEmpty()){
			String [] ln = biomelist.split(",");
			if (ln.length>0){
				for (int i = 0; i<ln.length;i++)
					if (BiomeTools.isBiomeExists(ln[i])) str = str+","+ln[i];
				str=str.replaceFirst(",", "");
			}
		}
		return str;
	}

	public static List<BiomeBlock> refilter (List<BiomeBlock> blocks, Biome filterBiome){
		if (filterBiome == null) return blocks;
		List<BiomeBlock> newBlocks = new ArrayList<BiomeBlock>();
		for (BiomeBlock biomeBlock : blocks)
			if (biomeBlock.getBiome().equals(filterBiome)) newBlocks.add(biomeBlock);
		return newBlocks;
	}

	public static Location parseLocation (String strloc){
		Location loc = null;
		if (strloc.isEmpty()) return null;
		String [] ln = strloc.split(",");
		if (!((ln.length==3)||(ln.length==4)||(ln.length==6))) return null;
		World w = Bukkit.getWorld(ln[0]);
		if (w==null) return null;
		for (int i = 1; i<ln.length; i++)
			if (!(ln[i].matches("-?[0-9]+[0-9]*\\.[0-9]+")||ln[i].matches("-?[0-9]+[0-9]*"))) return null;
		loc = new Location (w, Double.parseDouble(ln[1]),ln.length==3 ? 0 : Double.parseDouble(ln[2]),ln.length==3 ? Double.parseDouble(ln[2]) : Double.parseDouble(ln[3]));
		if (ln.length==6){
			loc.setYaw(Float.parseFloat(ln[4]));
			loc.setPitch(Float.parseFloat(ln[5]));
		}
		return loc;
	}
}
