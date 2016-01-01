package net.bubuxi.mc.friends;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

/**
 * Created by zekunshen on 12/27/15.
 */
public class Util {
    protected static ItemStack getItemStack(Material m, int n, String name, String lore) {
        ItemStack is = new ItemStack(m, n);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if(m==Material.SKULL_ITEM) {
            is.setData(new MaterialData(Material.SKULL_ITEM, (byte)3));
        }
        if(lore!=null) {
            List<String> lores = new ArrayList<>();
            for (String s: lore.split(";")) {
                lores.add(ChatColor.translateAlternateColorCodes('&',s));
            }
            im.setLore(lores);
        }
        is.setItemMeta(im);

        return is;
    }

    protected static ItemStack getItemStack(Material m, int n, String name) {
        return getItemStack(m, n, name, null);
    }
    /*
    return how long a player has not been online
     */
    protected static String getLastPlayedSince(String name) {
        if(Bukkit.getOfflinePlayer(name)!=null) {
            long l = System.currentTimeMillis() - Bukkit.getOfflinePlayer(name).getLastPlayed();

            int days = (int) (l / 86400000L);
            l %= 86400000L;
            int hours = (int) (l / 3600000L);
            l %= 3600000L;
            int mins = (int) (l / 60000L);
            return days + "天" + hours + "时" + mins + "分";
        }
        else return " 未登录";
    }

    protected static void printMap(HashMap<String, List<String>> listHashMap) {
        for (String s: listHashMap.keySet()) {
            System.out.print(s+": [");
            for(String n: listHashMap.get(s)) {
                System.out.print(n+" ");
            }
            System.out.print("]\n");
        }
    }

    protected static void print(HashMap<String, Integer> m) {
        System.out.println("HashMap:");
        for (Map.Entry<String, Integer> e:m.entrySet()
             ) {
            System.out.println("  "+e.getKey()+":"+e.getValue());
        }
    }
    protected static void print(Set<String> s) {
        System.out.println("Set:");
        for(String str: s) {
            System.out.println("  "+str);
        }
    }

}
