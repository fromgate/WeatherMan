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


import org.bukkit.Location;

import java.util.regex.Pattern;

public class Util {

    private static final Pattern INTEGER_GZ = Pattern.compile("[1-9]+[0-9]*");

    public static boolean isIntegerGZ(String str) {
        return INTEGER_GZ.matcher(str).matches();
    }


    public static boolean isWordInList(String word, String str) {
        String[] ln = str.split(",");
        for (String element : ln) {
            if (element.equalsIgnoreCase(word)) return true;
        }
        return false;
    }


    public static boolean isIdInList(int id, String str) {
        String[] ln = str.split(",");
        for (String element : ln) {
            if ((!element.isEmpty()) && element.matches("[0-9]*") && (Integer.parseInt(element) == id)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSameBlocks(Location loc1, Location loc2) {
        if (!loc1.getWorld().equals(loc2.getWorld())) return false;
        if (loc1.getBlockX() != loc2.getBlockX()) return false;
        if (loc1.getBlockZ() != loc2.getBlockZ()) return false;
        return loc1.getBlockY() == loc2.getBlockY();
    }

}
