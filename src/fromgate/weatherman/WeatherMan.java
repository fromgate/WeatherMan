/*  
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012, fromgate, fromgate@gmail.com
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

/*
 * TODO 
 * - глобальные биомы, регенерацию чанков под заданный биом
 * - walk-mode ?
 */

package fromgate.weatherman;


import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


public class WeatherMan extends JavaPlugin {

	//конфигурация
	boolean local_weather=true;
	int wand = 370; //слезы гаста
	int dradius = 5;
	boolean smoke = true;
	int smoke_chance = 50;
	boolean meltsnow = true;
	boolean meltice = true;
	Biome dbiome = Biome.ICE_PLAINS;
	int maxrcmd=250;
	int maxrwand=15;
	int maxrsign=100;
	boolean nethermob = true;
	String unsnowbiomes = "taiga";
	String unicebiomes = "taiga";
	String coldbiomes = "iceplains,taiga,icemountains,taigahills,frozenriver,frozenocean";  // холодные биомы нужно ли выносить в конфиг?! 
	String outdatedbiomes = "rainforest,seasonalforest,savanna,shrubland,icedesert,tundra";	//RAINFOREST, SEASONAL_FOREST, SAVANNA, SHRUBLAND, ICE_DESERT, TUNDRA - устаревшие биомы
	String language = "english";
	boolean language_save = false;
	boolean vcheck = true;
	boolean uselibigot = false;

	//текущие переменные
	char c1 = 'a';
	char c2 = '2';
	boolean consolecolored = false;
	public FileConfiguration config;
	protected WMUtil u;
	private WMListener l;
	private WMCmd lcmd;
	Logger log = Logger.getLogger("Minecraft");
	PluginDescriptionFile des;
	HashMap<String, Biome> bioms = new HashMap<String,Biome>(); //возможно оставить для алиасов?!
	HashMap<Snowball, BiomeBall> sballs = new HashMap<Snowball, BiomeBall>();  //TODO перевести на Metadata?
	HashMap<String, Cfg> pcfg = new HashMap<String, Cfg>();  //TODO перевести все на Metadata
	WMQueue regenqueue; // глобальная очередь для регена, надо подумать можно ли будет её "персонализировать" 
	WorldEditPlugin worldedit;
	WorldGuardPlugin worldguard;
	boolean worldedit_active=false;
	boolean worldguard_active=false;
	/////////////////////////////////////////////////
	protected WMLocalWeather lw;



	public boolean ConnectWorldEdit(){
		Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
		if ((worldEdit != null)&&(worldEdit instanceof WorldEditPlugin)) {
			worldedit = (WorldEditPlugin)worldEdit;
			return true;
		}
		return false;
	}

	public boolean ConnectWorldGuard(){
		Plugin worldGuard = getServer().getPluginManager().getPlugin("WorldGuard");
		if ((worldGuard != null)&&(worldGuard instanceof WorldGuardPlugin)) {
			worldguard = (WorldGuardPlugin)worldGuard;
			return true;
		}
		return false;
	}

	@Override
	public void onEnable() {

		config = this.getConfig();
		LoadCfg();
		SaveCfg();		
		u = new WMUtil(this, this.vcheck, this.language_save, this.language, "weatherman", "WeatherMan", "wm", "&b[&3WM&b]&f ");
		u.setConsoleColored(consolecolored);
		WMSysTools.setUseLibigot(uselibigot);
		WMSysTools.init();
		

		if (!WMSysTools.isBlocked()){
			if (!WMSysTools.isTestedVersion()) {
				log.info("[WeatherMan] +-------------------------------------------------------------------+");
				log.info("[WeatherMan] + This version of WeatherMan was not tested with CraftBukkit "+WMSysTools.getMinecraftVersion().replace('_', '.')+" +");
				log.info("[WeatherMan] + Check updates at http://dev.bukkit.org/server-mods/weatherman/    +");
				log.info("[WeatherMan] + or use this version at your own risk                              +");
				log.info("[WeatherMan] +-------------------------------------------------------------------+");
			}
		} else {
			log.info("[WeatherMan] +----------------------------------------------------------------------+");
			log.info("[WeatherMan] + This version of WeatherMan is not compatible with CraftBukkit "+WMSysTools.getMinecraftVersion().replace('_', '.')+" +");
			log.info("[WeatherMan] + Features depended to craftbukkit version will be disabled!           +");
			log.info("[WeatherMan] + It is strongly recommended to update WeatherMan to latest version!   +");
			log.info("[WeatherMan] + Check updates at http://dev.bukkit.org/server-mods/weatherman/       +");
			log.info("[WeatherMan] + or use this version at your own risk                                 +");
			log.info("[WeatherMan] +----------------------------------------------------------------------+");
			this.local_weather = false;
		}


		InitBioms();
		lcmd = new WMCmd (this);
		getCommand("wm").setExecutor(lcmd);
		getCommand("wth").setExecutor(lcmd);
		l = new WMListener (this);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(l, this);
		if (this.local_weather){
			lw = new WMLocalWeather(this);
			pm.registerEvents(lw, this);			
		} else log.info("[WeatherMan] Local weather feactures disabled!");

		worldedit_active = ConnectWorldEdit();
		worldguard_active = ConnectWorldGuard();
		regenqueue = new WMQueue (this);
		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
		}
	}



	public void LoadCfg(){
		wand = config.getInt("WeatherMan.wand-item-id",370); //слезы гаста
		dradius = config.getInt("WeatherMan.default-radius",5);
		meltsnow = config.getBoolean("WeatherMan.melt-snow",true);
		meltice = config.getBoolean("WeatherMan.melt-ice",true);
		smoke = config.getBoolean("WeatherMan.smoke-effect",true);
		smoke_chance=config.getInt("WeatherMan.smoke-chance",50);
		String bstr = config.getString("WeatherMan.default-biome", "iceplains");
		if (bioms.containsKey(bstr)) dbiome = Str2Biome (bstr);
		maxrcmd=config.getInt("WeatherMan.maximum-command-radius",250);
		maxrwand=config.getInt("WeatherMan.maximum-wand-radius",15);
		maxrsign=config.getInt("WeatherMan.maximum-sign-radius",100);
		nethermob=config.getBoolean("WeatherMan.spawn-nether-mobs-in-normal",true);
		language=config.getString("WeatherMan.language","english").toLowerCase();
		language_save=config.getBoolean("WeatherMan.language-save",false);
		consolecolored=config.getBoolean("WeatherMan.color-in-console",false);
		uselibigot=config.getBoolean("WeatherMan.libigot-enabled",false);
		unsnowbiomes=config.getString("WeatherMan.biomes.disable-snow-forming","");
		unicebiomes=config.getString("WeatherMan.biomes.disable-ice-forming","");
		vcheck=config.getBoolean("WeatherMan.check-updates",true);
		local_weather=config.getBoolean("WeatherMan.local-weather.enable",true);
	}

	public void SaveCfg(){
		config.set("WeatherMan.wand-item-id",wand); //слезы гаста
		config.set("WeatherMan.default-radius",dradius);
		config.set("WeatherMan.melt-snow",meltsnow);
		config.set("WeatherMan.melt-ice",meltice);
		config.set("WeatherMan.smoke-effect",smoke);
		config.set("WeatherMan.smoke-chance",smoke_chance);
		config.set("WeatherMan.default-biome", Biome2Str(dbiome));
		config.set("WeatherMan.maximum-command-radius",maxrcmd);
		config.set("WeatherMan.maximum-wand-radius",maxrwand);
		config.set("WeatherMan.maximum-sign-radius",maxrsign);
		config.set("WeatherMan.spawn-nether-mobs-in-normal",nethermob);
		config.set("WeatherMan.language",language);
		config.set("WeatherMan.language-save",language_save);
		config.set("WeatherMan.color-in-console",consolecolored);
		config.set("WeatherMan.biomes.disable-snow-forming",unsnowbiomes);
		config.set("WeatherMan.biomes.disable-ice-forming",unicebiomes);
		config.set("WeatherMan.check-updates",vcheck);
		config.set("WeatherMan.libigot-enabled",uselibigot);
		config.set("WeatherMan.local-weather.enable",local_weather);
		this.saveConfig();
	}


	public void InitBioms(){
		bioms.clear();
		bioms.put("original", null);
		Biome [] bm = Biome.values();
		if (bm.length>0){
			for (int i = 0; i<bm.length;i++){
				String bstr = Biome2Str (bm[i]);
				if (!(u.isWordInList(bstr, outdatedbiomes))) bioms.put(bstr, bm[i]);
			}
		}
	}

	public String Biome2Str (Biome b){
		String bstr = "original";
		if (b!= null) bstr = b.name().toLowerCase().replace("_", "");
		return bstr; 
	}

	public Biome Str2Biome (String bs){
		if (bioms.containsKey(bs)) return bioms.get(bs);
		else return null;
	} 


	public String getBiomeList(){
		String str="";
		Iterator<String> itr = bioms.keySet().iterator();
		while (itr.hasNext()) str = str+ ", " + itr.next();
		str = str.replaceFirst(", ", "");
		return str;
	}

	public WMResult ReplaceBiome (Biome b1, Biome tobiome, Location loc1, Location loc2){
		Long starttime = System.currentTimeMillis();
		WMQueue queue = new WMQueue(this);
		Biome b2 = tobiome;
		World w = loc1.getWorld();
		for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x<=Math.max(loc1.getBlockX(), loc2.getBlockX()); x++)
			for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z<=Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++){
				if (w.getBiome(x, z).equals(b1)) {
					queue.add(w,x,z,b2);
				}
			}
		WMResult result = queue.processQueue(); 
		result.calcTime(starttime);
		return result;
	}

	public WMResult SetBiome (Biome biome, Location loc1, Location loc2){
		Long starttime = System.currentTimeMillis();
		WMQueue queue = new WMQueue(this);
		Biome b = biome;

		World w = loc1.getWorld();
		for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x<=Math.max(loc1.getBlockX(), loc2.getBlockX()); x++)
			for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z<=Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++){
				queue.add(w,x,z,b);
			}
		WMResult result = queue.processQueue(); 
		result.calcTime(starttime);
		return result;
	}

	/*
	 *  Пока не используется. Возможно и не будет... 
	 */
	public void setBiomeDelayed (Biome biome, Location loc1, Location loc2){
		WMQueue queue = new WMQueue(this);
		Biome b = biome;

		World w = loc1.getWorld();
		for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x<=Math.max(loc1.getBlockX(), loc2.getBlockX()); x++)
			for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z<=Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++){
				queue.add(w,x,z,b);
			}
		queue.processQueueDelayed(true, 5000);
	}
	

	public WMResult SetBiome (Biome biome, Location loc, int r){
		Long starttime = System.currentTimeMillis();
		WMQueue queue = new WMQueue(this);
		World w = loc.getWorld();

		Location c=loc;
		c.setX(loc.getBlockX());
		c.setY(0);
		c.setZ(loc.getBlockZ());
		int cx = c.getBlockX();
		int cz = c.getBlockZ();

		for (int i = 0; i<=r; i++){
			int mj = (int) Math.sqrt(r*r-i*i);
			for (int j = 0; j<=mj; j++){
				queue.add(w,cx+i, cz+j,biome);
				queue.add(w,cx+i, cz-j,biome);
				queue.add(w,cx-i, cz+j,biome);
				queue.add(w,cx-i, cz-j,biome);
			}
		}

		WMResult result = queue.processQueue(); 
		result.calcTime(starttime);
		return result;
	}


	public String checkBiomes(String biomelist){
		String str = "";
		if (!biomelist.isEmpty()){
			String [] ln = biomelist.split(",");
			if (ln.length>0){
				for (int i = 0; i<ln.length;i++)
					if (bioms.containsKey(ln[i])) str = str+","+ln[i];
				str=str.replaceFirst(",", "");
			}
		}
		return str;
	}

	public Block getHighestBlock (World w, int x, int z){
		Block b = w.getHighestBlockAt(x, z);
		while ((b.getY()>1)&&(u.isIdInList(b.getTypeId(), "0,17,18"))){
			b = b.getRelative(BlockFace.DOWN);
		}
		return b;
	}


	//coldbiomes : "iceplains,taiga,icemountains,taigahills,frozenriver,frozenocean";
	public void meltSnow(World w, int x, int z){
		Block b = getHighestBlock(w, x, z);
		if (u.isWordInList(Biome2Str(b.getBiome()), coldbiomes)) return;
		if (meltsnow&&(b.getType()==Material.SNOW)) {
			b.setType(Material.AIR);
			b = getHighestBlock(w, x, z);
			if (b.getType()==Material.SNOW) b.setType(Material.AIR); 
		}
		else if (meltice&&(b.getType()==Material.ICE)) b.setType(Material.WATER);
	}

	public void meltSnow (Location loc){
		meltSnow (loc.getWorld(), loc.getBlockX(), loc.getBlockZ());
	}

	/*
	 * Не поддерживает биомы по умолчанию (чтобы не было зацикливания)
	 * Не использует очередь, поскольку всё равно дёргает каждый блок в процессе работы
	 * - будет тормозить просто из-за этого
	 */
	protected WMResult FloodFill (Location loc, Biome biome){
		Long starttime = System.currentTimeMillis();
		WMResult result = new WMResult (0,0);
		int blockcount =0;
		List<Block> ffill = new ArrayList<Block>();
		Set<Block> queueblock = new HashSet<Block>();
		Set<Chunk> chunks = new HashSet<Chunk>();
		World w = loc.getWorld();
		Biome tbiome = w.getBiome(loc.getBlockX(), loc.getBlockZ());
		if ((biome == null)||tbiome.equals(biome)) {
			result.calcTime(starttime);
			return result;
		}
		ffill.add(w.getBlockAt(loc.getBlockX(),1,loc.getBlockZ()));
		while (ffill.size()>0){
			for (int i = ffill.size()-1; i>=0;i--){
				Block b = ffill.get(i);
				ScanBC(b,tbiome,ffill);
				queueblock.add(b);
			}
			ffill.removeAll(queueblock);

			for (Block b : queueblock){
				b.setBiome(biome);
				blockcount++;
				if (meltsnow||meltice)	meltSnow (b.getWorld(),b.getX(),b.getZ());				
				chunks.add(b.getChunk());
			}
			queueblock.clear();
		}
		for (Chunk ch : chunks){
			w.refreshChunk(ch.getX(), ch.getZ());
			WMSysTools.saveChunk(ch);
		}
		result = new WMResult (chunks.size(),blockcount); 
		result.calcTime(starttime);
		return result;
	}

	private void addBC (Block b, Biome tb, List<Block> ffill){
		if ((!ffill.contains(b))&&(b.getBiome().equals(tb)))
			ffill.add(b);
	}

	private void ScanBC(Block b, Biome tb, List<Block> ffill){
		addBC (b.getRelative(BlockFace.NORTH), tb, ffill);
		addBC (b.getRelative(BlockFace.SOUTH), tb, ffill);
		addBC (b.getRelative(BlockFace.EAST), tb, ffill);
		addBC (b.getRelative(BlockFace.WEST), tb, ffill);
		ffill.remove(b);
	}
}
