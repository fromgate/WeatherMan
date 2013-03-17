package fromgate.weatherman;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;


public class WMPCfg {
	public static void clearPlayerConfig (WeatherMan plg, Player p){
		if (p.hasMetadata("weatherman.infomode")) p.removeMetadata("weatherman.infomode", plg);
		if (p.hasMetadata("weatherman.wandmode")) p.removeMetadata("weatherman.wandmode", plg);
		
		//LocalWeather
		if (p.hasMetadata("weatherman.last-weather")) p.removeMetadata("weatherman.last-weather", plg);
		if (p.hasMetadata("weatherman.personal-weather")) p.removeMetadata("weatherman.personal-weather", plg);
		
	}
	
	
	/*
	 * Local Weather
	 */
	public static boolean getLastWeather (Player p){
		if (p.hasMetadata("weatherman.last-weather"))	return p.getMetadata("weatherman.last-weather").get(0).asBoolean();
		else return p.getWorld().hasStorm();
	}
	public static void setLastWeather (WeatherMan plg, Player p, boolean rain){
		p.setMetadata("weatherman.last-weather", new FixedMetadataValue (plg, rain));
	}
	
	public static boolean isWeatherChanged (WeatherMan plg, Player p, boolean newrain){
		boolean lastrain =getLastWeather(p);
		setLastWeather (plg, p, newrain);
		return (newrain!=lastrain);
	}


	/*
	 * Personal Weather
	 */
	public static int getPersonalWeather (Player p){
		if (!p.hasMetadata("weatherman.personal-weather")) return -1;
        if(p.getMetadata("weatherman.personal-weather").get(0).asBoolean()) return 1;
		return 0;
	}
	
	public static void setPersonalWeather (WeatherMan plg, Player p, boolean rain){
		p.setMetadata("weatherman.personal-weather", new FixedMetadataValue (plg, rain));
	}
	
	public static void removePersonalWeather (WeatherMan plg, Player p){
		if (p.hasMetadata("weatherman.personal-weather")) p.removeMetadata("weatherman.personal-weather", plg);
	}

	
	/*
	 *  WalkInfo mode
	 */
	public static boolean isWalkInfoMode (Player p){
		return (p.hasMetadata("weatherman.infomode")&&(p.getMetadata("weatherman.infomode").get(0).asBoolean()));
	}
	public static void setWalkInfoMode (WeatherMan plg, Player p, boolean mode){
		p.setMetadata("weatherman.infomode", new FixedMetadataValue (plg, mode));
	}
	public static void toggleWalkInfoMode (WeatherMan plg, Player p){
		p.setMetadata("weatherman.infomode", new FixedMetadataValue (plg, !isWalkInfoMode(p)));
	}
	
	
	/*
	 *  Wand mode
	 */
	public static boolean isWandMode (Player p){
		return (p.hasMetadata("weatherman.wandmode")&&(p.getMetadata("weatherman.wandmode").get(0).asBoolean()));
	}
	public static void setWandMode (WeatherMan plg, Player p, boolean mode){
		p.setMetadata("weatherman.wandmode", new FixedMetadataValue (plg, mode));
	}
	public static void toggleWandMode (WeatherMan plg, Player p){
		p.setMetadata("weatherman.wandmode", new FixedMetadataValue (plg, !isWandMode(p)));
	}
	

}
