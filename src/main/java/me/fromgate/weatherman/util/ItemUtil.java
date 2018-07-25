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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class ItemUtil {
    private static JavaPlugin plugin;
    private static Random random;

    public static void init(JavaPlugin plg) {
        random = new Random();
        plugin = plg;
    }


    public static ItemStack parseItemStack(String itemStr) {
        ItemFactory itemFactory = Bukkit.getItemFactory();

        if (itemStr.isEmpty()) return null;

        String itemAmountStr = itemStr;
        String enchantStr = "";
        String name = "";

        if (itemAmountStr.contains("$")) {
            name = itemAmountStr.substring(0, itemAmountStr.indexOf("$"));
            itemAmountStr = itemAmountStr.substring(name.length() + 1);
        }
        if (itemAmountStr.contains("@")) {
            enchantStr = itemAmountStr.substring(itemAmountStr.indexOf("@") + 1);
            itemAmountStr = itemAmountStr.substring(0, itemAmountStr.indexOf("@"));
        }
        int amount = 1;

        String[] si = itemAmountStr.split("\\*");

        if (si.length > 0) {
            if (si.length == 2) amount = Math.max(getMinMaxRandom(si[1]), 1);
            String materialStr = si[0];
            if (materialStr.contains(":") || materialStr.matches("\\d+")) {
                throw new IllegalStateException("Numerical id/data are not supported. Please update your item definition: " + itemStr);
            }

            Material material = Material.getMaterial(materialStr.toUpperCase());
            if (material == null) {
                material = Material.getMaterial(materialStr.toUpperCase(), true);
            }

            if (material == null) {
                //logOnce("wrongitem"+ti[0], "Could not parse item material name (id) "+ti[0]);
                return null;
            }

            ItemStack item = new ItemStack(material, amount);
            if (!enchantStr.isEmpty()) {
                item = setEnchantments(item, enchantStr);
            }
            if (!name.isEmpty()) {
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name.replace("_", " ")));
                item.setItemMeta(im);
            }
            return item;
        }
        return null;
    }

    public static ItemStack setEnchantments(ItemStack item, String enchants) {
        ItemStack i = item.clone();
        if (enchants.isEmpty()) return i;
        String[] ln = enchants.split(",");
        for (String ec : ln) {
            if (ec.isEmpty()) continue;
            Color clr = colorByName(ec);
            if (clr != null) {
                //if (isIdInList(item.getTypeId(), "298,299,300,301")){
                if (item.hasItemMeta() && (item.getItemMeta() instanceof LeatherArmorMeta)) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
                    meta.setColor(clr);
                    i.setItemMeta(meta);
                }
            } else {
                String ench = ec;
                int level = 1;
                if (ec.contains(":")) {
                    ench = ec.substring(0, ec.indexOf(":"));
                    level = Math.max(1, getMinMaxRandom(ec.substring(ench.length() + 1)));
                }
                Enchantment e = Enchantment.getByName(ench.toUpperCase());
                if (e == null) continue;
                i.addUnsafeEnchantment(e, level);
            }
        }
        return i;
    }


    public static Color colorByName(String colorname) {
        Color[] clr = {Color.WHITE, Color.SILVER, Color.GRAY, Color.BLACK,
                Color.RED, Color.MAROON, Color.YELLOW, Color.OLIVE,
                Color.LIME, Color.GREEN, Color.AQUA, Color.TEAL,
                Color.BLUE, Color.NAVY, Color.FUCHSIA, Color.PURPLE};
        String[] clrs = {"WHITE", "SILVER", "GRAY", "BLACK",
                "RED", "MAROON", "YELLOW", "OLIVE",
                "LIME", "GREEN", "AQUA", "TEAL",
                "BLUE", "NAVY", "FUCHSIA", "PURPLE"};
        for (int i = 0; i < clrs.length; i++)
            if (colorname.equalsIgnoreCase(clrs[i])) return clr[i];
        return null;
    }


    @SuppressWarnings("deprecation")
    public static boolean compareItemStr(ItemStack item, String itemStr) {
        String itemAmountStr = itemStr;
        String name = "";
        if (itemAmountStr.contains("$")) {
            name = itemStr.substring(0, itemAmountStr.indexOf("$"));
            name = ChatColor.translateAlternateColorCodes('&', name.replace("_", " "));
            itemAmountStr = itemStr.substring(name.length() + 1);
        }
        if (itemAmountStr.isEmpty()) return false;
        if (!name.isEmpty()) {
            String itemName = item.hasItemMeta() ? item.getItemMeta().getDisplayName() : "";
            if (!name.equals(itemName)) return false;
        }
        return compareItemStrIgnoreName(item.getType(), item.getAmount(), itemAmountStr); // ;compareItemStr(item, itemstr);
    }

    public static boolean compareItemStrIgnoreName(ItemStack itemStack, String itemstr) {
        return compareItemStrIgnoreName(itemStack.getType(), itemStack.getAmount(), itemstr);
    }

    public static boolean compareItemStrIgnoreName(Material material, int item_amount, String itemStr) {
        if (!itemStr.isEmpty()) {

            int amount = 1;
            String[] si = itemStr.split("\\*");
            if (si.length > 0) {
                if ((si.length == 2) && si[1].matches("[1-9]+[0-9]*")) {
                    amount = Integer.parseInt(si[1]);
                }
                String materialStr = si[0].toUpperCase();
                if (materialStr.contains(":")||materialStr.matches("\\d+")) {
                    throw new IllegalStateException("Numerical id/data are not supported. Please update your item definition: " + itemStr);
                }
                Material id = Material.getMaterial(materialStr);
                if (id == null) {
                    id = Material.getMaterial(materialStr, true);
                }
                if (id != null) {
                    return ((material.equals(id)) && (item_amount >= amount));
                }
            }
        }
        return false;
    }

    public static boolean hasItemInInventory(Inventory inv, String itemstr) {
        ItemStack item = parseItemStack(itemstr);
        if (item == null) return false;
        if (item.getType() == Material.AIR) return false;
        return (countItemInInventory(inv, itemstr) >= item.getAmount());
    }

    public static boolean hasItemInInventory(Inventory inv, String itemstr, int amount) {
        ItemStack item = parseItemStack(itemstr);
        if (item == null) return false;
        if (item.getType() == Material.AIR) return false;
        return (countItemInInventory(inv, itemstr) >= amount);
    }


    public static boolean hasItemInInventory(Player p, String itemstr) {
        return hasItemInInventory(p.getInventory(), itemstr);
    }

    /**
     * Returns true if player has required aumount of item in his inventory
     *
     * @param p       — Player
     * @param itemstr — String representetaion of ItemStack
     * @param amount  — Required amount (amount defined by itemstr will be ignored
     * @return — true if player has items
     */
    public static boolean hasItemInInventory(Player p, String itemstr, int amount) {
        return hasItemInInventory(p.getInventory(), itemstr, amount);
    }


    public static int countItemInInventory(Player p, String itemstr) {
        return countItemInInventory(p.getInventory(), itemstr);
    }

    public static void removeItemInInventory(final Player p, final String itemstr) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> removeItemInInventory(p.getInventory(), itemstr), 1);
    }


    private static boolean itemHasName(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().hasDisplayName();
    }

    private static boolean compareItemName(ItemStack item, String istrname) {
        if (istrname.isEmpty() && (!itemHasName(item))) return true;
        if ((!istrname.isEmpty()) && itemHasName(item)) {
            String name = ChatColor.translateAlternateColorCodes('&', istrname.replace("_", " "));
            return item.getItemMeta().getDisplayName().equals(name);
        }
        return false;
    }

    public static boolean removeItemInHand(Player player, String itemStr) {
        ItemStack slot = player.getItemInHand();
        if (slot == null || slot.getType() == Material.AIR) return false;
        String itemAmountStr = itemStr;
        int amount = 1;
        String name = "";

        if (itemAmountStr.contains("$")) {
            name = itemAmountStr.substring(0, itemAmountStr.indexOf("$"));
            itemAmountStr = itemAmountStr.substring(name.length() + 1);
        }

        String[] si = itemAmountStr.split("\\*");
        if (si.length == 0) return false;
        if ((si.length == 2) && si[1].matches("[1-9]+[0-9]*")) {
            amount = Integer.parseInt(si[1]);
        }


        String materialStr = si[0];

        if (materialStr.contains(":") || materialStr.matches("\\d+")) {
            throw new IllegalStateException("Numerical id/data are not supported. Please update your item definition: " + itemStr);
        }

        Material material = Material.getMaterial(materialStr);
        if (material == null) {
            material = Material.getMaterial(materialStr, true);
        }

        if (!compareItemName(slot, name)) return false;
        if (!material.equals(slot.getType())) return false;
        int slotAmount = slot.getAmount();
        if (slotAmount < amount) return false;
        if (slotAmount == amount) slot.setType(Material.AIR);
        else slot.setAmount(slotAmount - amount);
        return true;
    }

    public static int removeItemInInventory(Inventory inv, String istr) {
        return removeItemInInventory(inv, istr, -1);
    }


    @SuppressWarnings("deprecation")
    public static int removeItemInInventory(Inventory inv, String itemStr, int amount) {
        String itemAmountStr = itemStr;
        int left = 1;
        String name = "";

        if (itemAmountStr.contains("$")) {
            name = itemAmountStr.substring(0, itemAmountStr.indexOf("$"));
            itemAmountStr = itemAmountStr.substring(name.length() + 1);
        }

        String[] si = itemAmountStr.split("\\*");
        if (si.length == 0) return left;
        if ((si.length == 2) && si[1].matches("[1-9]+[0-9]*")) left = (amount < 1 ? Integer.parseInt(si[1]) : amount);

        String materialStr = si[0].toUpperCase();

        if (materialStr.contains(":") || materialStr.matches("\\d+")) {
            throw new IllegalStateException("Numerical id/data are not supported. Please update your item definition: " + itemStr);
        }

        Material material = Material.getMaterial(materialStr);
        if (material == null) {
            material = Material.getMaterial(materialStr, true);
        }

        if (material == null) {
            return left;
        }

        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack slot = inv.getItem(i);
            if (slot == null) continue;
            if (!compareItemName(slot, name)) continue;
            if (!material.equals(slot.getType())) continue;
            int slotamount = slot.getAmount();
            if (slotamount == 0) continue;
            if (slotamount <= left) {
                left = left - slotamount;
                inv.setItem(i, null);
            } else {
                slot.setAmount(slotamount - left);
                left = 0;
            }
            if (left == 0) return 0;
        }
        return left;
    }


    public static int countItemInInventory(Inventory inv, String itemStr) {
        String itemAmountStr = itemStr;
        int count = 0;
        String name = "";
        if (itemAmountStr.contains("$")) {
            name = itemAmountStr.substring(0, itemAmountStr.indexOf("$"));
            itemAmountStr = itemAmountStr.substring(name.length() + 1);
        }

        String[] si = itemAmountStr.split("\\*");
        if (si.length == 0) return 0;

        String materialStr = si[0].toUpperCase();

        if (materialStr.contains(":") || materialStr.matches("\\d+")) {
            throw new IllegalStateException("Numerical id/data are not supported. Please update your item definition: " + itemStr);
        }

        Material material = Material.getMaterial(materialStr);
        if (material == null) {
            material = Material.getMaterial(materialStr, true);
        }

        if (material == null) {
            return 0;
        }

        for (ItemStack slot : inv.getContents()) {
            if (slot == null) continue;
            if (!compareItemName(slot, name)) continue;
            if (material.equals(slot.getType())) {
                count += slot.getAmount();
            }
        }
        return count;
    }

    public static void giveItemOrDrop(Player player, ItemStack itemStack) {
        for (ItemStack i : player.getInventory().addItem(itemStack).values())
            player.getWorld().dropItemNaturally(player.getLocation(), i);
    }

    public static int getMinMaxRandom(String minMaxStr) {
        int min = 0;
        int max = 0;
        String strMin = minMaxStr;
        String strMax = minMaxStr;

        if (minMaxStr.contains("-")) {
            strMin = minMaxStr.substring(0, minMaxStr.indexOf("-"));
            strMax = minMaxStr.substring(minMaxStr.indexOf("-") + 1);
        }
        if (strMin.matches("[1-9]+[0-9]*")) min = Integer.parseInt(strMin);
        max = min;
        if (strMax.matches("[1-9]+[0-9]*")) max = Integer.parseInt(strMax);
        if (max > min) return min + random.nextInt(1 + max - min);
        else return min;
    }

}
