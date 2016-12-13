package me.fromgate.weatherman.localtime;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 12.12.2016.
 */
public class LocalTime {

    private static Map<String, Long> player;
    private static Map<String, Long> regions;
    private static Map<String, Long> biomes;
    private static Map<String, Long> worlds;

    public static void init() {
        player = new HashMap<>();
        regions = new HashMap<>(); // true  - дождь
        biomes = new HashMap<>();
        worlds = new HashMap<>();
        loadLocalTime();
    }

    private static void loadLocalTime() {


    }


}
