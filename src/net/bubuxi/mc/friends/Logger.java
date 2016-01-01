package net.bubuxi.mc.friends;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Created by zekunshen on 12/27/15.
 */
public class Logger {
    public static int debugLevel = -1;
    public static void sendMessage(String name, String msg) {
        Bukkit.getPlayer(name).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void debug(String msg, int level) {
        if(level<=debugLevel) {
            System.out.println("Level-"+level+": "+msg);
        }
    }

    public static void warning(String msg) {
        Bukkit.getLogger().warning(msg);
    }

    public static void info(String msg) {
        Bukkit.getLogger().info(msg);
    }
}
