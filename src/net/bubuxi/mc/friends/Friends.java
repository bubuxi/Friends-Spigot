package net.bubuxi.mc.friends;

import net.bubuxi.mc.friends.database.DataBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

/**
 * Created by zekunshen on 12/27/15.
 */


public class Friends extends JavaPlugin {
    public static Connection conn;
    protected FriendsManager fm;
    protected GUIManager gm;
    private static Friends f;
    protected int defaultLimit, maximum;
    protected int price;
    public static Economy econ;
    protected AttackManager am;
    public void onEnable() {
        saveDefaultConfig();
        f = this;
        CommandExecutor mainCommand = new MainCommand(this);
        this.getCommand("friends").setExecutor(mainCommand);
        Bukkit.getPluginManager().registerEvents(new EventManager(), this);
        conn = DataBase.getConnection();
        fm = new FriendsManager();
        gm = new GUIManager(this);
        am = new AttackManager();
        Bukkit.getPluginManager().registerEvents(am, this);
        defaultLimit = this.getConfig().getInt("defaultLimit");
        maximum = this.getConfig().getInt("maximumFriends");
        price = this.getConfig().getInt("upgradeMoney");
        setupEconomy();
    }
    public void onDisable() {
        fm.upload();
        fm.closeTimer();
        DataBase.closeConection(conn);
    }
    public static Friends getInstance() {
        return f;
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
