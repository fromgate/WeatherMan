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

package me.fromgate.weatherman.playerconfig;

import me.fromgate.weatherman.localweather.WeatherState;
import me.fromgate.weatherman.util.BiomeTools;
import me.fromgate.weatherman.util.Cfg;
import me.fromgate.weatherman.util.Time;
import org.bukkit.block.Biome;

public class PlayerData {

    boolean stateInfoMode;
    boolean stateWand;
    boolean stateLastWeather;


    // Save
    String ballTree;
    Biome ballBiome;
    int ballRadius;


    WeatherState personalWeather;

    Long personalTime;


    public PlayerData() {
        stateInfoMode = false;
        stateWand = false;
        stateLastWeather = false;
        ballTree = "default";
        ballBiome = Cfg.defaultBiome;
        ballRadius = Cfg.defaultRadius;
        personalWeather = WeatherState.UNSET;
        personalTime = null;
    }


    public boolean isStateInfoMode() {
        return stateInfoMode;
    }

    public void setStateInfoMode(boolean stateInfoMode) {
        this.stateInfoMode = stateInfoMode;
    }

    public boolean isStateWand() {
        return stateWand;
    }

    public void setStateWand(boolean stateWand) {
        this.stateWand = stateWand;
    }

    public boolean isStateLastWeather() {
        return stateLastWeather;
    }

    public void setStateLastWeather(boolean stateLastWeather) {
        this.stateLastWeather = stateLastWeather;
    }

    public String getBallTree() {
        return ballTree;
    }

    public void setBallTree(String ballTree) {
        this.ballTree = ballTree;
    }

    public Biome getBallBiome() {
        return ballBiome;
    }

    public void setBallBiome(Biome ballBiome) {
        this.ballBiome = ballBiome;
    }

    public void setBallBiome(String biomeStr) {
        Biome biome = BiomeTools.biomeByName(biomeStr);
        this.ballBiome = biome == null ? Cfg.defaultBiome : biome;
    }


    public int getBallRadius() {
        return ballRadius;
    }

    public void setBallRadius(int ballRadius) {
        this.ballRadius = ballRadius;
    }

    public WeatherState getPersonalWeather() {
        return personalWeather;
    }

    public void setPersonalWeather(WeatherState personalWeather) {
        this.personalWeather = personalWeather;
    }

    public void setPersonalWeather(String personalWeather) {
        setPersonalWeather(WeatherState.getByName(personalWeather));
    }


    public Long getPersonalTime() {
        return personalTime;
    }

    public void setPersonalTime(Long personalTime) {
        this.personalTime = personalTime;
    }

    public void setPersonalTime(String timeStr) {
        personalTime = Time.parseTime(timeStr);
    }

    public String getPersonalTimeStr() {
        return Time.timeToString(personalTime);
    }

    public void setPersonalWeather(boolean rain) {
        setPersonalWeather(rain ? WeatherState.RAIN : WeatherState.CLEAR);
    }
}
