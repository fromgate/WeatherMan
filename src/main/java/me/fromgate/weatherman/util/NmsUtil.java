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

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class NmsUtil {

    private static final Logger log;
    private static final String[] testedVersions = {"v1_13_R1"};
    private static String version = "";
    private static boolean blocked = false;
    private static String cboPrefix = "org.bukkit.craftbukkit.";
    private static String nmsPrefix = "net.minecraft.server.";
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
    private static Method BiomeBase_getTemperature;
    private static Class<?> CraftChunk;
    private static Method craftChunk_getHandle;
    private static Field field_NmsChunk_done;
    private static Field nms_chunk_world;
    private static Method getChunkProvider;
    private static Class<?> NmsChunk;
    private static Class<?> ChunkProviderServer;

    private static Method saveChunk;
    private static boolean saveChunkBool;

    private static Method saveChunkNOP;
    private static Class<?> BlockPosition;
    private static Constructor<?> constructBlockPosition;
    private static Class<?> CraftPlayer;
    private static Method craftPlayer_getHandle;
    private static Class<?> Packet;
    private static Class<?> PacketPlayOutMapChunk;
    private static Constructor<?> newPacketOutChunk;
    private static Class<?> PacketPlayOutUnloadChunk;
    private static Constructor<?> newPacketUnloadChunk;
    private static Class<?> EntityPlayer;
    private static Field playerConnection;
    private static Class<?> PlayerConnection;
    private static Method sendPacket;

    static {
        log = Logger.getLogger("Minecraft");
        try {
            Object s = Bukkit.getServer();
            Method m = s.getClass().getMethod("getHandle");
            Object cs = m.invoke(s);
            String className = cs.getClass().getName();
            String[] v = className.split("\\.");
            if (v.length == 5) {
                version = v[3];
                cboPrefix = "org.bukkit.craftbukkit." + version + ".";
                nmsPrefix = "net.minecraft.server." + version + ".";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Class<?> craftWorld = cboClass("CraftWorld");
            craftWorld_getHandle = craftWorld.getMethod("getHandle");
            NmsWorld = nmsClass("World");
            field_worldProvider = NmsWorld.getDeclaredField("worldProvider");
            WorldProvider = nmsClass("WorldProvider");
            field_WorldProvider_d = WorldProvider.getDeclaredField("c");
            field_WorldProvider_d.setAccessible(true);
            BlockPosition = nmsClass("BlockPosition");
            constructBlockPosition = BlockPosition.getConstructor(int.class, int.class, int.class);
            WorldChunkManager = nmsClass("WorldChunkManager");
            getBiome = WorldChunkManager.getDeclaredMethod("getBiome", BlockPosition);
            CraftBlock = cboClass("block.CraftBlock");
            BiomeBase = nmsClass("BiomeBase");
            biomeBaseToBiome = CraftBlock.getDeclaredMethod("biomeBaseToBiome", BiomeBase);
            biomeToBiomeBase = CraftBlock.getDeclaredMethod("biomeToBiomeBase", Biome.class);
            BiomeBase_getTemperature = BiomeBase.getDeclaredMethod("getTemperature");
            CraftChunk = cboClass("CraftChunk");
            craftChunk_getHandle = CraftChunk.getMethod("getHandle");
            NmsChunk = nmsClass("Chunk");
            field_NmsChunk_done = NmsChunk.getDeclaredField("done");
            nms_chunk_world = NmsChunk.getDeclaredField("world");
            NmsWorldServer = nmsClass("WorldServer");
            getChunkProvider = NmsWorldServer.getMethod("getChunkProvider");
            ChunkProviderServer = nmsClass("ChunkProviderServer");

            try {
                saveChunk = ChunkProviderServer.getDeclaredMethod("saveChunk", NmsChunk);
                saveChunkBool = false;
            } catch (Exception e) {
                saveChunk = ChunkProviderServer.getDeclaredMethod("saveChunk", NmsChunk, boolean.class);
                saveChunkBool = true;
            }

            saveChunkNOP = ChunkProviderServer.getDeclaredMethod("saveChunkNOP", NmsChunk);
            CraftPlayer = cboClass("entity.CraftPlayer");
            craftPlayer_getHandle = CraftPlayer.getMethod("getHandle");
            EntityPlayer = nmsClass("EntityPlayer");
            playerConnection = EntityPlayer.getField("playerConnection");
            Packet = nmsClass("Packet");
            PacketPlayOutMapChunk = nmsClass("PacketPlayOutMapChunk");
            newPacketOutChunk = PacketPlayOutMapChunk.getConstructor(NmsChunk, int.class);
            PacketPlayOutUnloadChunk = nmsClass("PacketPlayOutUnloadChunk");
            newPacketUnloadChunk = PacketPlayOutUnloadChunk.getConstructor(int.class, int.class);
            PlayerConnection = nmsClass("PlayerConnection");
            sendPacket = PlayerConnection.getMethod("sendPacket", Packet);
        } catch (Exception e) {
            blocked = true;
            log.info("[WeatherMan] his version of WeatherMan is not compatible with CraftBukkit " + Bukkit.getVersion());
            log.info("[WeatherMan] Features depended to craftbukkit version will be disabled!");
            log.info("[WeatherMan] + It is strongly recommended to update WeatherMan to latest version!");
            log.info("[WeatherMan] + Check updates at http://dev.bukkit.org/server-mods/wm/");
            log.info("[WeatherMan] + or use this version at your own risk.");
            e.printStackTrace();
        }
        if ((!blocked) && (!isTestedVersion())) {
            log.info("[WeatherMan] +---------------------------------------------------------------------+");
            log.info("[WeatherMan] + This version of WeatherMan was not tested with CraftBukkit " + getMinecraftVersion().replace('_', '.') + " +");
            log.info("[WeatherMan] + Check updates at http://dev.bukkit.org/server-mods/wm/      +");
            log.info("[WeatherMan] + or use this version at your own risk                                +");
            log.info("[WeatherMan] +---------------------------------------------------------------------+");
        }
    }

    private static Class<?> nmsClass(String classname) throws Exception {
        return Class.forName(nmsPrefix + classname);
    }

    private static Class<?> cboClass(String classname) throws Exception {
        return Class.forName(cboPrefix + classname);
    }

    public static String getMinecraftVersion() {
        return version;
    }

    public static boolean isTestedVersion() {
        for (String testedVersion : testedVersions) {
            if (testedVersion.equalsIgnoreCase(version)) return true;
        }
        return false;
    }

    public static boolean isBlocked() {
        return blocked;
    }

    public static Biome getOriginalBiome(Location loc) {
        return getOriginalBiome(loc.getBlockX(), loc.getBlockZ(), loc.getWorld());
    }

    public static float getBiomeTemperature(Biome biome) {
        if (blocked) return 100;
        try {
            Object biomebase = biomeToBiomeBase.invoke(null, biome);
            Object temperature = BiomeBase_getTemperature.invoke(biomebase);
            return (Float) temperature;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 100;
    }

    public static Biome getOriginalBiome(int x, int z, World w) {
        if (blocked) return null;
        try {
            Object nmsWorldServer = craftWorld_getHandle.invoke(w);
            Object worldProvider = field_worldProvider.get(nmsWorldServer);
            Object d = field_WorldProvider_d.get(worldProvider);
            Object blockPosition = constructBlockPosition.newInstance(x, 0, z);
            Object biomeBase = getBiome.invoke(d, blockPosition);
            Object biome = biomeBaseToBiome.invoke(null, biomeBase);
            return (Biome) biome;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static void repopulateChunk(final Chunk chunk) {
        if (blocked) return;
        try {
            Object craftchunk = CraftChunk.cast(chunk);
            Object nmsChunk = craftChunk_getHandle.invoke(craftchunk);
            field_NmsChunk_done.setAccessible(true);
            field_NmsChunk_done.set(nmsChunk, false);
            saveChunk(chunk);
            chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveChunk(Chunk ch) {
        if (blocked) return;
        if (ch == null) return;
        try {
            Object nms_chunk = craftChunk_getHandle.invoke(ch);
            Object nms_world = nms_chunk_world.get(nms_chunk);
            getChunkProvider.invoke(nms_world);
            Object chunkProvider = getChunkProvider.invoke(nms_world);
            if (saveChunkBool) {
                saveChunk.invoke(chunkProvider, nms_chunk, false);
            } else {
                saveChunk.invoke(chunkProvider, nms_chunk);
            }

            saveChunkNOP.invoke(chunkProvider, nms_chunk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static void refreshChunk(Chunk ch) {
        World w = ch.getWorld();
        switch (Cfg.chunkUpdateMethod) {
            case 1:
                w.refreshChunk(ch.getX(), ch.getZ());
                ch.unload();
                ch.load();
                break;
            case 2:
                w.getPlayers().forEach(player -> {
                    Location loc = ch.getBlock(7, player.getLocation().getBlockY(), 7).getLocation();
                    if (player.getLocation().distance(loc) <= Bukkit.getServer().getViewDistance() * 16) {
                        try {
                            Object nmsPlayer = craftPlayer_getHandle.invoke(player);
                            Object nmsChunk = craftChunk_getHandle.invoke(ch);
                            Object nmsPlayerConnection = playerConnection.get(nmsPlayer);
                            Object unloadPacket = newPacketUnloadChunk.newInstance(ch.getX(), ch.getZ());
                            Object chunkPacket = newPacketOutChunk.newInstance(nmsChunk, 65535);
                            sendPacket.invoke(nmsPlayerConnection, unloadPacket);
                            sendPacket.invoke(nmsPlayerConnection, chunkPacket);
                        } catch (Exception ignored) {
                        }
                    }
                });
                break;
            default:
                w.refreshChunk(ch.getX(), ch.getZ());
                break;
        }
    }
}