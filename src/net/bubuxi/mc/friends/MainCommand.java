package net.bubuxi.mc.friends;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by zekunshen on 12/27/15.
 */
public class MainCommand implements CommandExecutor{

    Friends plugin;

    public MainCommand(Friends f) {
        plugin =f;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("friends")&&commandSender instanceof Player) {
            if(strings.length==2&&strings[0].equals("add")) {
                if (!Friends.getInstance().fm.areFriends(commandSender.getName(), strings[1])) {
                    Player p = Bukkit.getPlayer(strings[1]);
                    if(p!=null) {
                        Friends.getInstance().fm.tryToAdd(commandSender.getName(), strings[1]);
                        Logger.sendMessage(p.getName(),"玩家" + commandSender.getName() + "希望加你为好友");
                        Logger.sendMessage(commandSender.getName(),"&e好友申请发送成功");
                        return false;
                    }else {
                        Logger.sendMessage(commandSender.getName(),"&4该玩家不在线,仅能对在线玩家申请");
                    }
                }
                else {
                    commandSender.sendMessage("你们已经是好友了");
                    return false;
                }
            }
            else if(strings.length==2&&strings[0].equals("remove")) {
                if (Friends.getInstance().fm.areFriends(commandSender.getName(), strings[1])) {
                    Friends.getInstance().fm.removeFriend(commandSender.getName(), strings[1]);
                    commandSender.sendMessage("好友关系已解除");
                    return false;
                }
                else {
                    commandSender.sendMessage("你们不是好友");
                    return false;
                }
            }
            plugin.gm.openGui((Player)commandSender);
            return false;
        }
        return false;
    }



}
