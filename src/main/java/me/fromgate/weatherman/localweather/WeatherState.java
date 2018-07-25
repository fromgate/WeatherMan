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

package me.fromgate.weatherman.localweather;

/**
 * Created by Igor on 16.12.2016.
 */
public enum WeatherState {

    UNSET,
    RAIN,
    CLEAR;

    public static WeatherState getByName(String name) {
        if (name != null) {
            switch (name.toLowerCase()) {
                case "rain":
                    return RAIN;
                case "clear":
                    return CLEAR;
            }
        }
        return UNSET;
    }

}
