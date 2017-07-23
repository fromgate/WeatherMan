/*
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012-2016, fromgate, fromgate@gmail.com
 *  https://dev.bukkit.org/projects/weatherman
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

package me.fromgate.weatherman.util.lang;

import java.util.Map;

public interface Messenger {

    String colorize(String text);

    boolean broadcast(String colorize);

    boolean log(String text);

    String clean(String text);

    boolean tip(int seconds, Object sender, String text);

    boolean tip(Object sender, String text);

    boolean print(Object sender, String text);

    boolean broadcast(String permission, String text);

    String toString(Object obj, boolean fullFloat);

    Map<String, String> load(String language);

    void save(String langugage, Map<String, String> message);

    boolean isValidSender(Object send);

}
