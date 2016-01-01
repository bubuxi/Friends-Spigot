package net.bubuxi.mc.friends;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by zekunshen on 12/29/15.
 */
public class GUIManager {

    protected Friends f;
    protected static ItemStack friendList, pendingRequest, friendsInfo, help, attackMode;
    protected static ItemStack fl_heading, returnBot;
    protected static ItemStack pr_heading;
    protected static ItemStack fm_canAttack, fm_cannotAttack;
    private static final int FRIENDLIST_ROWS=3;
    private static final int REQUESTING_ROWS=2;

    public GUIManager (Friends plugin) {
        f = plugin;
        friendList = Util.getItemStack(Material.APPLE, 1, "&e好友列表", "&f点击查看你的好友列表;&e点击进入");
        pendingRequest = Util.getItemStack(Material.PAPER, 1, "&e待处理请求", "&f查看你的好友申请;&e点击进入");
        friendsInfo = Util.getItemStack(Material.BOOKSHELF, 1, "&a你的信息", "&f查看你的现在的好友数以及好友上限;&e可以购买好友上限哦");
        help = Util.getItemStack(Material.BOOK, 1, "&d好友插件介绍","&3查看好友列表,接受好友请求;&3以及查看信息都可以在这里实现;" +
                "&6如需添加好友,输入指令;&f/friends add [玩家];&6如需删除好友,输入指令;&f/friends remove [玩家]");
        fl_heading = Util.getItemStack(Material.BOOK, 1, "好友列表", "查看你所有的好友");
        returnBot = Util.getItemStack(Material.EYE_OF_ENDER, 1, "&4返回主菜单");
        pr_heading = Util.getItemStack(Material.BOOK, 1, "&e待处理请求");

        fm_canAttack = Util.getItemStack(Material.WOOD_SWORD, 1, "&e友好模式", "&f设置是否能攻击好友;&f当前模式为:&e可以攻击好友;&6点击切换模式");
        fm_cannotAttack = Util.getItemStack(Material.WOOD_SWORD, 1, "&e友好模式", "&f设置是否能攻击好友;&f当前模式为:&e不能攻击好友;&6点击切换模式");
    }
    public void openGui(Player player) {
        Inventory inv = Bukkit.createInventory(player,9,"好友菜单");
        inv.setItem(0, help.clone());
        inv.setItem(3, friendList.clone());
        inv.setItem(4, pendingRequest.clone());
        inv.setItem(5, friendsInfo.clone());
        String s;
        if(Friends.getInstance().am.canAttackFriends(player)) inv.setItem(8, fm_canAttack.clone());
        else inv.setItem(8,fm_cannotAttack.clone());
        player.openInventory(inv);
    }
    public void openFriendList(Player player, int page) {
        Inventory inv = Bukkit.createInventory(player, 45, "好友列表");
        inv.setItem(0, fl_heading.clone());
        inv.setItem(8, returnBot.clone());
        if(page>1) {
            inv.setItem(36, Util.getItemStack(Material.CAKE,1, "&e上一页", "&2第"+(page-1)+"页"));
        }
        if(Friends.getInstance().fm.getFriendsNum(player.getName())>(page*FRIENDLIST_ROWS*9)) {
            inv.setItem(44, Util.getItemStack(Material.CAKE, 1, "&e下一页", "&2第" + (page+1) + "页"));
        }
        List<String> l= f.fm.getFriends(player.getName());
        int numFriends;
        if(l == null) numFriends=0;
        else numFriends=l.size();
        int counter = 9;
        for(int i = 0;i<FRIENDLIST_ROWS*9&&i+(page-1)*FRIENDLIST_ROWS*9<numFriends;i++) {
            Player p = Bukkit.getPlayer(l.get(i+(page-1)*FRIENDLIST_ROWS*9));
            if(p==null||(!p.isOnline())) {
                inv.setItem(counter, Util.getItemStack(Material.SKULL_ITEM, 1, l.get(i + (page - 1) * FRIENDLIST_ROWS * 9),
                        "&b上次登陆;&9" + Util.getLastPlayedSince(l.get(i + (page - 1) * FRIENDLIST_ROWS * 9))+";&6金钱:;&f"+
                Friends.econ.getBalance(l.get(i+(page-1)*FRIENDLIST_ROWS*9))));
            }
            else {
                inv.setItem(counter, Util.getItemStack(Material.SKULL_ITEM, 1, l.get(i+(page-1)*FRIENDLIST_ROWS*9),"&e在线;&6金钱:;&f"+
                        Friends.econ.getBalance(l.get(i+(page-1)*FRIENDLIST_ROWS*9))));
            }
            counter++;

        }
        player.openInventory(inv);
    }
    public void openPending(Player player) {
        Inventory inv = Bukkit.createInventory(player, 18, "待处理请求");
        inv.setItem(0, pr_heading);
        inv.setItem(8, returnBot);
        List<String> l= f.fm.getRequesting(player.getName());
        int counter = 9;
        for(int i = 0;i<REQUESTING_ROWS*9&&i<l.size();i++) {
            inv.setItem(counter, Util.getItemStack(Material.SKULL_ITEM, 1, l.get(i), "&6点击确认好友"));
        }
        player.openInventory(inv);
    }
    public void openInfo(Player player) {
        Logger.debug("tst",0);
        Inventory inv = Bukkit.createInventory(player, 9, "好友信息");
        inv.setItem(0, Util.getItemStack(Material.ANVIL, 1, "好友信息", "你拥有"+Friends.getInstance().fm.getFriendsNum(player.getName())+
        "好友;好友上限为"+Friends.getInstance().fm.getLimit(player.getName())));
        inv.setItem(3, Util.getItemStack(Material.GOLD_INGOT, 1, "购买上限", "+10好友上限;价格:"+Friends.getInstance().price));
        inv.setItem(8, returnBot);
        player.openInventory(inv);
    }
}
