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

import java.util.HashMap;
import java.util.Map;

public class ParamUtil {


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Парсер
    // преобразуем строку вида <команда> <параметр:значение> <параметр:значение> <параметр=значение> в Map {команда="", параметр=значеие....}

    public static Map<String, String> parseParams(String[] args, int index, String defaultKey) {
        Map<String, String> params = new HashMap<>();
        if (args.length <= index) return params;
        for (int i = index; i < args.length; i++) {
            String key = args[i];
            String value = "";
            if (args[i].contains(":")) {
                key = args[i].substring(0, args[i].indexOf(":"));
                value = args[i].substring(args[i].indexOf(":") + 1);
            } else {
                value = key;
                key = defaultKey;
            }
            params.put(key, value);
        }
        return params;
    }

    public static Map<String, String> parseParams(String param) {
        return parseParams(param, "param", true);
    }

    public static Map<String, String> parseParams(String param, String defaultKey, boolean includeLine) {
        Map<String, String> params = new HashMap<>();
        if (param.isEmpty()) return params;
        String[] ln = param.split(" ");
        params = parseParams(ln, 0, defaultKey);
        if (includeLine) params.put("param-line", param);
        return params;
    }

    public static String getParam(Map<String, String> params, String key, String defparam) {
        if (!params.containsKey(key)) return defparam;
        return params.get(key);
    }

    public static int getParam(Map<String, String> params, String key, int defparam) {
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[1-9]+[0-9]*")) return defparam;
        //if (!u().isIntegerGZ(str)) return defparam;
        return Integer.parseInt(str);
    }

    public static float getParam(Map<String, String> params, String key, float defparam) {
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[0-9]+\\.?[0-9]*")) return defparam;
        return Float.parseFloat(str);
    }

    public static double getParam(Map<String, String> params, String key, double defparam) {
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[0-9]+\\.?[0-9]*")) return defparam;
        return Double.parseDouble(str);
    }


    public static boolean getParam(Map<String, String> params, String key, boolean defparam) {
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        return (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("on") || str.equalsIgnoreCase("yes"));
    }

    public static String toString(Map<String, String> params) {
        StringBuilder str = new StringBuilder();
        for (String key : params.keySet())
            str.append(key).append("[").append(params.get(key)).append("] ");
        return (str.length() == 0) ? "empty" : str.toString();
    }

    public static boolean isParamExists(Map<String, String> params, String key) {
        return params.containsKey(key);
    }


}
