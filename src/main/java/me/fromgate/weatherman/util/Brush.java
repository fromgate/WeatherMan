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

import me.fromgate.weatherman.WeatherMan;
import me.fromgate.weatherman.playerconfig.PlayerConfig;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

public enum Brush {
    BIOME("&6WeatherMan wand&1&0&2$GHAST_TEAR", "biome"),
    WOODCUTTER("&6Woodcutter&1&0&2$GHAST_TEAR", "woodcutter"),
    DEPOPULATOR("&4Depopulator&1&0&2$GHAST_TEAR", "depopulator"),
    FORESTER("&2Forester&1&0&2$GHAST_TEAR", "forester");

    private String item;
    private final String tag;

    private static WeatherMan plug() {
        return WeatherMan.getPlugin();
    }

    private static final Map<Snowball, BiomeBall> snowBalls = new HashMap<>();


    Brush(String item, String tag) {
        this.item = item;
        this.tag = tag;
    }

    public String getItemStr() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }


    public static void save(FileConfiguration fileConfiguration) {
        fileConfiguration.set("brush.biome.item", Brush.BIOME.getItemStr());
        fileConfiguration.set("brush.woodcutter.item", Brush.WOODCUTTER.getItemStr());
        fileConfiguration.set("brush.depopulator.item", Brush.DEPOPULATOR.getItemStr());
        fileConfiguration.set("brush.forester.item", Brush.FORESTER.getItemStr());
    }

    public static void load(FileConfiguration fileConfiguration) {
        Brush.BIOME.setItem(fileConfiguration.getString("brush.biome.item", Brush.BIOME.getItemStr()));
        Brush.WOODCUTTER.setItem(fileConfiguration.getString("brush.woodcutter.item", Brush.WOODCUTTER.getItemStr()));
        Brush.DEPOPULATOR.setItem(fileConfiguration.getString("brush.depopulator.item", Brush.DEPOPULATOR.getItemStr()));
        Brush.FORESTER.setItem(fileConfiguration.getString("brush.forester.item", Brush.FORESTER.getItemStr()));
    }


    public boolean isBrushInHand(Player player) {
        ItemStack item = player.getInventory().getItemInOffHand();
        if (item == null || item.getType() == Material.AIR) {
            item = player.getInventory().getItemInMainHand();
        }
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        return ItemUtil.compareItemStr(item, this.item);
    }

    public static void shootWand(Player player) {
        Brush brush = Brush.getBrushInHand(player);
        if (brush == null) return;
        Snowball sb = brush.shoot(player);
        if (brush == Brush.BIOME || brush == Brush.DEPOPULATOR) snowBalls.put(sb,
                PlayerConfig.getBiomeBall(player));
        else if (brush == Brush.FORESTER) {
            sb.setMetadata("WeatherMan-forester", new FixedMetadataValue(WeatherMan.getPlugin(), PlayerConfig.getTree(player)));
        }

    }

    public Snowball shoot(Player player) {
        if (!isBrushInHand(player)) return null;
        Snowball sb = player.launchProjectile(Snowball.class);
        player.getWorld().playEffect(player.getLocation(), Effect.GHAST_SHOOT, 0);
        sb.setMetadata("WeatherMan", new FixedMetadataValue(WeatherMan.getPlugin(), this.tag));
        sb.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(2.5));
        return sb;
    }

    public static Brush getBrushInHand(Player player) {
        for (Brush brush : Brush.values()) {
            if (brush.isBrushInHand(player)) return brush;
        }
        return null;
    }

    public static Brush getBySnowBall(Snowball sb) {
        if (!sb.hasMetadata("WeatherMan")) return null;
        String tag = sb.getMetadata("WeatherMan").get(0).asString();
        for (Brush brush : Brush.values()) {
            if (brush.name().equalsIgnoreCase(tag)) return brush;
        }
        return null;
    }

    public static void processSnowball(Snowball sb) {
        Brush brush = Brush.getBySnowBall(sb);
        if (brush == null) return;
        switch (brush) {
            case BIOME:
                if (!snowBalls.containsKey(sb)) return;
                BiomeBall bb = snowBalls.get(sb);
                BiomeTools.setBiomeRadius(null, sb.getLocation(), bb.biome, Math.min(bb.radius, Cfg.maxRadiusWand), null);
                snowBalls.remove(sb);
                break;
            case DEPOPULATOR:
                if (!snowBalls.containsKey(sb)) return;
                Repopulator.depopulateNatural(Repopulator.getSnowballHitBlock(sb).getLocation(), snowBalls.get(sb).radius);
                break;
            case WOODCUTTER:
                Repopulator.depopulateNatural(Repopulator.getSnowballHitBlock(sb).getLocation(), true);
                break;
            case FORESTER:
                Forester.growTree(sb);
                break;
            default:
                break;
        }

    }

}
