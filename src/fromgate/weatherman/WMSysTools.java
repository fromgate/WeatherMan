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
 *  along with WeatherMan.  If not, see <http://www.gnorg/licenses/>.
 * 
 */


package fromgate.weatherman;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class WMSysTools {
	private static String [] tested_versions = {"v1_4_6","v1_4_R1","v1_5_R1"};
	private static String version ="";
	private static boolean blocked = false;
	private static boolean uselibigot = false;

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
	private static Class<?> CraftChunk;
	private static Method craftchunk_getHandle;
	private static Class<?> EmptyChunk;
	private static Field nms_chunk_world;
	private static Field nmsword_chunkProviderServer;
	private static Class<?> NmsChunk;
	private static Class<?> ChunkProviderServer;
	private static Method saveChunk;
	private static Method saveChunkNOP;
	private static Class<?> Packet;
	private static Class<?> Packet70Bed;
	private static Constructor<?> newPacket70Bed;
	private static Method sendPacket;
	private static Class<?> CraftEntity;
	private static Field CraftEntity_entity;
	private static Class<?> EntityPlayer;
	private static Field entityPlayer_netServerHandler;
	private static Class<?> NetServerHandler;

	public static void init(){
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
			field_WorldProvider_d = WorldProvider.getDeclaredField("d");
			WorldChunkManager = nmsClass("WorldChunkManager");
			getBiome = WorldChunkManager.getDeclaredMethod("getBiome", int.class, int.class);
			CraftBlock = cboClass("block.CraftBlock");
			BiomeBase = nmsClass("BiomeBase");
			biomeBaseToBiome = CraftBlock.getDeclaredMethod("biomeBaseToBiome", BiomeBase);
			CraftChunk = cboClass("CraftChunk");
			craftchunk_getHandle = CraftChunk.getMethod("getHandle");
			EmptyChunk = nmsClass("EmptyChunk");
			NmsChunk=nmsClass("Chunk");;
			nms_chunk_world = NmsChunk.getDeclaredField("world");
			NmsWorldServer = nmsClass("WorldServer");
			nmsword_chunkProviderServer = NmsWorldServer.getField("chunkProviderServer");
			ChunkProviderServer = nmsClass("ChunkProviderServer");
			saveChunk = ChunkProviderServer.getDeclaredMethod("saveChunk",NmsChunk);
			saveChunkNOP = ChunkProviderServer.getDeclaredMethod("saveChunkNOP",NmsChunk);

			Packet = nmsClass("Packet");
			Packet70Bed = nmsClass("Packet70Bed");
			newPacket70Bed = Packet70Bed.getConstructor(int.class,int.class);

			EntityPlayer = nmsClass("EntityPlayer");
			String playerConnectionClass = "PlayerConnection";
			String playerConnectionField = "playerConnection";
			if ((version.isEmpty()&&(!uselibigot))||version.equalsIgnoreCase("v1_4_5")){
				playerConnectionClass = "NetServerHandler";
				playerConnectionField = "netServerHandler";
			}

			CraftEntity = cboClass("entity.CraftEntity");
			CraftEntity_entity = CraftEntity.getDeclaredField("entity");
			CraftEntity_entity.setAccessible(true);

			entityPlayer_netServerHandler = EntityPlayer.getField(playerConnectionField);
			NetServerHandler = nmsClass (playerConnectionClass);
			sendPacket = NetServerHandler.getMethod("sendPacket", Packet);


		} catch (Exception e){
			blocked = true;
		}
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
	
	public static void setUseLibigot (boolean ulb){
		uselibigot = ulb;
	}

	public static boolean isTestedVersion(){
		if (version.isEmpty()) return uselibigot;
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


	public static void sendWeather (Player p, boolean rain){
		if (blocked) return;
		try {
			Object packet = newPacket70Bed.newInstance(rain ? 1 : 2,0);
			Object craftEntity = p;
			Object nmsPlayer = CraftEntity_entity.get(craftEntity);
			Object netSenderHandler = entityPlayer_netServerHandler.get(nmsPlayer);
			sendPacket.invoke(netSenderHandler, packet);
		} catch (Exception e){
		}
	}


}
