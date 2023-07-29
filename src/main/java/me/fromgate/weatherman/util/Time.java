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

public class Time {

    public static Long parseTime(String timeStr) {
        if (timeStr == null) {
            return null;
        } else if (timeStr.matches("(?i)day|noon")) {
            return 6000L;
        } else if (timeStr.matches("(?i)night|midnight")) {
            return 18000L;
        } else if (timeStr.matches("\\d{1,2}:\\d{2}")) {
            String[] ln = timeStr.split(":");
            int hours = Integer.parseInt(ln[0]);
            int minutes = Integer.parseInt(ln[1]);
            if (hours < 24 && minutes < 60) {
                hours = hours - 6;
                if (hours < 0) hours = hours + 24;
                return (hours * 1000L) + (minutes / 60 * 1000L);
            }
        }
        return null;
    }

    public static String timeToString(Long time) {
        if (time == null) {
            return "UNSET";
        }
        int hours = (int) ((time / 1000 + 6) % 24);
        int minutes = (int) (60 * (time % 1000) / 1000);
        return String.format("%02d:%02d", hours, minutes);
    }

}
