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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class NMSUtil {

	private static Logger log;
	private static String [] tested_versions = {"v1_5_R2","v1_5_R3","v1_6_R1","v1_6_R2","v1_6_R3","v1_7_R1"};
	private static String [] version_e = {"v1_6_R1","v1_6_R2","v1_6_R3","v1_7_R1"};
	private static String version ="";
	private static boolean blocked = false;
	private static String cboPrefix = "org.bukkit.craftbukkit.";
	private static String nmsPrefix = "net.minecraft.server.";
	private static Class<?> CraftWorld;
	private static Method craftWorld_getHandle;
	private static Class<?> NmsWorld;
	private static Class<?> NmsWorldServer;
	private static Field field_worldProvider;
	private static Class<?> WorldProvider;
	private static Class<?> WorldChunkManager;
	private static Field field_WorldProvider_d;
	private static Method getBiome;
	private static Class<?> CraftBlock;
	private static Class<?> BiomeBase; 
	private static Method biomeBaseToBiome;
	private static Method biomeToBiomeBase;
	private static Field field_BiomeBase_temperature;	
	private static Class<?> CraftChunk;
	private static Method craftchunk_getHandle;
	private static Field field_NmsChunk_done;
	private static Class<?> EmptyChunk;
	private static Field nms_chunk_world;
	private static Field nmsword_chunkProviderServer;
	private static Class<?> NmsChunk;
	private static Class<?> ChunkProviderServer;
	private static Method saveChunk;
	private static Method saveChunkNOP;

	//private static Field dimensionField;
	//private static Class<?> CraftServer;
	//private static Method getHandleCS;
	//private static Class<?> Packet;
	//private static Class<?> PacketPlayOutMapChunk; //Packet51MapChunk;
	
	//private static Constructor<?> newPacket;
	//private static Class<?> DedicatedPlayerList;
	//private static Method sendPacketNearby;

	public static void init(){
		log = Logger.getLogger("Minecraft");
		try{
			Object s = Bukkit.getServer();
			Method m = s.getClass().getMethod("getHandle");
			Object cs = m.invoke(s);
			String className = cs.getClass().getName();
			String [] v = className.split("\\.");
			if (v.length==5){
				version = v[3];
				cboPrefix = "org.bukkit.craftbukkit."+version+".";
				nmsPrefix = "net.minecraft.server."+version+".";;
			}
		} catch (Exception e){
		}


		try {
			CraftWorld = cboClass("CraftWorld");
			craftWorld_getHandle = CraftWorld.getMethod("getHandle");
			NmsWorld = nmsClass("World");
			field_worldProvider = NmsWorld.getDeclaredField("worldProvider");
			WorldProvider = nmsClass("WorldProvider");
			field_WorldProvider_d = WorldProvider.getDeclaredField(getWorldChunkManagerField());
			WorldChunkManager = nmsClass("WorldChunkManager");
			getBiome = WorldChunkManager.getDeclaredMethod("getBiome", int.class, int.class);
			CraftBlock = cboClass("block.CraftBlock");
			BiomeBase = nmsClass("BiomeBase");
			biomeBaseToBiome = CraftBlock.getDeclaredMethod("biomeBaseToBiome", BiomeBase);
			biomeToBiomeBase= CraftBlock.getDeclaredMethod("biomeToBiomeBase", Biome.class);
			field_BiomeBase_temperature = BiomeBase.getDeclaredField("temperature");
			CraftChunk = cboClass("CraftChunk");
			craftchunk_getHandle = CraftChunk.getMethod("getHandle");
			EmptyChunk = nmsClass("EmptyChunk");
			NmsChunk=nmsClass("Chunk");
			field_NmsChunk_done = NmsChunk.getDeclaredField("done"); 
			nms_chunk_world = NmsChunk.getDeclaredField("world");
			NmsWorldServer = nmsClass("WorldServer");
			nmsword_chunkProviderServer = NmsWorldServer.getField("chunkProviderServer");
			ChunkProviderServer = nmsClass("ChunkProviderServer");
			saveChunk = ChunkProviderServer.getDeclaredMethod("saveChunk",NmsChunk);
			saveChunkNOP = ChunkProviderServer.getDeclaredMethod("saveChunkNOP",NmsChunk);
			
			
			//Packet = Class.forName(nmsPrefix+"Packet");
			//dimensionField = NmsWorldServer.getField("dimension");
			//PacketPlayOutMapChunk = Class.forName(nmsPrefix+((version.startsWith("v1_6")||version.startsWith("v1_5")) ? "Packet51MapChunk": "PacketPlayOutMapChunk"));
			//newPacket = PacketPlayOutMapChunk.getConstructor(NmsChunk,boolean.class,int.class);
			//DedicatedPlayerList = Class.forName(nmsPrefix+"DedicatedPlayerList");
			//CraftServer = Class.forName(cboPrefix+"CraftServer");
			//getHandleCS = CraftServer.getMethod("getHandle");
			//sendPacketNearby = DedicatedPlayerList.getMethod("sendPacketNearby", double.class, double.class, double.class, double.class, int.class, Packet);
			
			//Packet60Explosion = Class.forName(nmsPrefix+((version.startsWith("v1_6")||version.startsWith("v1_5")) ? "Packet60Explosion": "PacketPlayOutExplosion"));

		} catch (Exception e){
			blocked = true;
			log.info("[WeatherMan] his version of WeatherMan is not compatible with CraftBukkit "+Bukkit.getVersion());
			log.info("[WeatherMan] Features depended to craftbukkit version will be disabled!");
			log.info("[WeatherMan] + It is strongly recommended to update WeatherMan to latest version!");
			log.info("[WeatherMan] + Check updates at http://dev.bukkit.org/server-mods/weatherman/");
			log.info("[WeatherMan] + or use this version at your own risk.");
		}

		if ((!blocked)&&(!isTestedVersion())){
			log.info("[WeatherMan] +-------------------------------------------------------------------+");
			log.info("[WeatherMan] + This version of WeatherMan was not tested with CraftBukkit "+getMinecraftVersion().replace('_', '.')+" +");
			log.info("[WeatherMan] + Check updates at http://dev.bukkit.org/server-mods/weatherman/    +");
			log.info("[WeatherMan] + or use this version at your own risk                              +");
			log.info("[WeatherMan] +-------------------------------------------------------------------+");
		}

	}

	private static String getWorldChunkManagerField(){
		for (String e : version_e)
			if (version.equalsIgnoreCase(e)) return "e";
		return "d";
	}

	private static Class<?> nmsClass(String classname) throws Exception{
		return Class.forName(nmsPrefix+classname);
	}

	private static Class<?> cboClass(String classname) throws Exception{
		return Class.forName(cboPrefix+classname);
	}



	public static String getMinecraftVersion(){
		return version;
	}

	public static boolean isTestedVersion(){
		for (int i = 0; i< tested_versions.length;i++){
			if (tested_versions[i].equalsIgnoreCase(version)) return true;
		}
		return false;
	}

	public static boolean isBlocked(){
		return blocked;
	}

	public static Biome getOriginalBiome (Location loc){
		return getOriginalBiome (loc.getBlockX(), loc.getBlockZ(), loc.getWorld());
	}

	public static float getBiomeTemperature(Biome biome){
		if (blocked) return 100;
		try {
			Object biomebase = biomeToBiomeBase.invoke(null, biome);
			Object temperature = field_BiomeBase_temperature.get(biomebase);
			return (Float) temperature; 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 100;
	}


	
	
	public static Biome getOriginalBiome (int x, int z, World w){
		if (blocked) return null;
		try {
			Object nmsWorldServer = craftWorld_getHandle.invoke(w);
			Object worldProvider = field_worldProvider.get(nmsWorldServer);
			Object d = field_WorldProvider_d.get(worldProvider);
			Object biomeBase = getBiome.invoke(d, x,z);
			Object biome = biomeBaseToBiome.invoke(null, biomeBase);
			Biome b = (Biome) biome;
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void repopulateChunk(Chunk chunk){
		if (blocked) return;
		try{
			Object craftchunk = CraftChunk.cast(chunk);
			Object nmsChunk = craftchunk_getHandle.invoke(craftchunk);
			field_NmsChunk_done.set(nmsChunk, false);
			refreshChunk(chunk);
			//chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
		} catch (Exception e){
			e.printStackTrace();
		}		
	}

	public static void saveChunk(Chunk ch){
		if (blocked) return;
		try{
			Object craftchunk = CraftChunk.cast(ch);
			Object nms_chunk = craftchunk_getHandle.invoke(craftchunk);
			if (EmptyChunk.isInstance(nms_chunk)) return;
			Object nms_world = nms_chunk_world.get(nms_chunk);
			Object chunkProvider = nmsword_chunkProviderServer.get(nms_world);
			saveChunk.invoke(chunkProvider,nms_chunk);
			saveChunkNOP.invoke(chunkProvider,nms_chunk);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	
	
	public static void refreshChunk(Chunk ch){
		ch.unload(true);
		ch.load(false);
		ch.getWorld().refreshChunk(ch.getX(), ch.getZ());
	}
	


}
