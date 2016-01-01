package net.bubuxi.mc.friends;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;


/**
 * Created by zekunshen on 12/27/15.
 */
public class EventManager implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onInvClick(InventoryClickEvent event) {
        if(event.getInventory().getName().equals("好友菜单")) {
            testReturn(event);
            if(event.getCurrentItem()!=null&&
                    event.getCurrentItem().equals(GUIManager.friendList)) {
                if(event.getWhoClicked() instanceof Player) {
                    Player whoClicked = (Player) event.getWhoClicked();
                    Friends.getInstance().gm.openFriendList(whoClicked, 1);
                }
            }
            else if(event.getCurrentItem()!=null&&
                    event.getCurrentItem().equals(GUIManager.pendingRequest)) {
                if(event.getWhoClicked() instanceof Player) {
                    Player whoClicked = (Player) event.getWhoClicked();
                    Friends.getInstance().gm.openPending(whoClicked);
                }
            }
            else if(event.getCurrentItem()!=null&&
                    event.getCurrentItem().equals(GUIManager.friendsInfo)) {
                if (event.getWhoClicked() instanceof Player) {
                    Player whoClicked = (Player) event.getWhoClicked();
                    Friends.getInstance().gm.openInfo(whoClicked);
                }
            }
            else if(event.getCurrentItem()!=null&&
                    (event.getCurrentItem().equals(GUIManager.fm_canAttack)||
                    event.getCurrentItem().equals(GUIManager.fm_cannotAttack))) {
                if (event.getWhoClicked() instanceof Player) {
                    Player whoClicked = (Player) event.getWhoClicked();
                    Friends.getInstance().am.toggleAttackMode(whoClicked);
                    Friends.getInstance().gm.openGui(whoClicked);
                }

            }
            event.setCancelled(true);
        }
        if(event.getInventory().getName().equals("待处理请求")) {
            testReturn(event);
            if(event.getWhoClicked() instanceof Player) {
                Player whoClicked = (Player) event.getWhoClicked();
                if(event.getCurrentItem()!=null&&
                        event.getCurrentItem().hasItemMeta()&&event.getCurrentItem().getItemMeta().hasLore()&&
                          event.getCurrentItem().getItemMeta().getLore().size()>0&&
                        event.getCurrentItem().getItemMeta().getLore().get(0)!=null&&
                        event.getCurrentItem().getItemMeta().getLore().get(0)
                                .equals(ChatColor.translateAlternateColorCodes('&',"&6点击确认好友"))) {
                    if(Friends.getInstance().fm.addFriendship(whoClicked.getName(),
                            event.getCurrentItem().getItemMeta().getDisplayName())) {
                        Logger.sendMessage(whoClicked.getName(), "&e好友添加成功");
                    }
                    else {
                        Logger.sendMessage(whoClicked.getName(), "&4好友添加失败, 有人好友数超出上限");
                    }
                    Friends.getInstance().gm.openPending(whoClicked);
                }
            }
            event.setCancelled(true);
        }
        if(event.getInventory().getName().equals("好友列表")) {
            testReturn(event);
            if(event.getCurrentItem()!=null&&
                    event.getCurrentItem().hasItemMeta()&&event.getCurrentItem().getItemMeta().hasDisplayName()&&
                    (event.getCurrentItem().getItemMeta().getDisplayName()
                            .equals(ChatColor.translateAlternateColorCodes('&',"&e上一页"))||
                    event.getCurrentItem().getItemMeta().getDisplayName()
                            .equals(ChatColor.translateAlternateColorCodes('&',"&e下一页")))) {
                if(event.getWhoClicked() instanceof Player) {
                    Player whoClicked = (Player) event.getWhoClicked();
                    String s = event.getCurrentItem().getItemMeta().getLore().get(0);
                    s = s.substring(3, s.length() - 1);
                    int page = Integer.parseInt(s);
                    Logger.debug(""+page, 0);
                    Friends.getInstance().gm.openFriendList(whoClicked, page);
                }
            }
            event.setCancelled(true);
        }
        if(event.getInventory().getName().equals("好友信息")) {
            testReturn(event);
            if(event.getCurrentItem()!=null&& event.getCurrentItem().hasItemMeta()
                    &&event.getCurrentItem().getItemMeta().hasDisplayName()
                    &&event.getCurrentItem().getItemMeta().getDisplayName().equals("购买上限")) {
                testReturn(event);
                if(event.getWhoClicked() instanceof Player) {
                    Player whoClicked = (Player) event.getWhoClicked();
                    if(Friends.econ.getBalance(whoClicked)>Friends.getInstance().price) {
                        if(Friends.getInstance().fm.canUpgradeLimit(whoClicked.getName())) {
                            Friends.getInstance().fm.upgradeLimit(whoClicked.getName());
                            Friends.getInstance().gm.openInfo(whoClicked);
                            Friends.econ.withdrawPlayer(whoClicked, Friends.getInstance().price);
                            Logger.sendMessage(whoClicked.getName(), "&6购买上限成功");
                        }
                        else {
                            Logger.sendMessage(whoClicked.getName(), "&4你的好友数量已经达到上限");
                        }
                    }
                    else {
                        Logger.sendMessage(whoClicked.getName(),"&4你没有足够的金钱");
                    }
                }
            }
            event.setCancelled(true);
        }
    }
    public void testReturn(InventoryClickEvent e) {
        if(e.getCurrentItem()!=null&&e.getCurrentItem().equals(GUIManager.returnBot)) {
            e.setCancelled(true);
            if (e.getWhoClicked() instanceof Player) {
                Player whoClicked = (Player) e.getWhoClicked();
                Friends.getInstance().gm.openGui(whoClicked);
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {

    }

    @EventHandler
    public void onInvAct(InventoryAction event) {

    }

    @EventHandler
    public void onInvMovItem(InventoryMoveItemEvent event) {

    }
}
