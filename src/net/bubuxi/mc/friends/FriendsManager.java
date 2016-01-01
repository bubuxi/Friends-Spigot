package net.bubuxi.mc.friends;

import net.bubuxi.mc.friends.database.DataBase;
import net.bubuxi.mc.friends.database.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zekunshen on 12/27/15.
 */
public class FriendsManager {

    private HashMap<String, List<String>> data;
    private List<AbstractMap.SimpleEntry<String, String>> toAdd;
    private List<AbstractMap.SimpleEntry<String, String>>  toRemove;
    /*=======================================================*/

    private HashMap<AbstractMap.SimpleEntry<String, String>, Long> adding;
    /*=======================================================*/
    private HashMap<String, Integer> limits;
    private List<String> exists;
    private HashSet<String> toChange;

    Timer timer;


    public FriendsManager () {
        //create table if not exists
        Logger.debug("正在检测表格", 1);
        try {
            ResultSet rs = Friends.conn.getMetaData().getTables(null, null, "FRIENDS", null);
            if(rs.next()) {
                Logger.debug("表格已存在", 1);
            }
            else {
                SQLUtil.createTable();
                Logger.debug("表格创建成功",1);
            }
            rs = Friends.conn.getMetaData().getTables(null, null, "LIMITS", null);
            if(rs.next()) {
                Logger.debug("表格已存在", 1);
            }
            else {
                SQLUtil.createLimit();
                Logger.debug("表格创建成功",1);
            }
        } catch (SQLException e) {
            Logger.debug("表格创建失败",0);
            e.printStackTrace();
        }
        //do the first download
        this.downloadData();
        //Util.printMap(data);
        toAdd = new ArrayList<>();
        toRemove = new ArrayList<>();
        adding = new HashMap<>();
        toChange = new HashSet<>();
        //register timer task
        timer = new Timer();
        timer.schedule(new UpdateTask(), 1000L, 180000L);
        timer.schedule(new RefreshTask(), 2000L, 60000L);
    }
    protected void closeTimer() {
        timer.cancel();
    }

    protected boolean canUpgradeLimit(String name ) {
        return getLimit(name)<Friends.getInstance().maximum;
    }

    protected int getLimit(String name) {
        if(limits.containsKey(name)) {
            return limits.get(name);
        }
        else
            return Friends.getInstance().defaultLimit;
    }

    protected void upgradeLimit(String name) {
        if(limits.containsKey(name)) {
            limits.put(name,limits.get(name)+10);
        }
        else {
            limits.put(name,Friends.getInstance().defaultLimit+10);
        }

        toChange.add(name);
        //Logger.info("已经添加至toChange");
    }


    /*
    @param
    p1: the one request adding
    p2: the one is being added
     */
    protected void tryToAdd(String p1, String p2) {
        AbstractMap.SimpleEntry<String, String> e = new AbstractMap.SimpleEntry<>(p1, p2);
        //TODO: may have problem
        if(!adding.containsKey(e)) {
            adding.put(e, System.currentTimeMillis());
        }
    }

    protected List<String> getRequesting(String name) {
        List<String> l = new ArrayList<>() ;
        for(AbstractMap.SimpleEntry<String, String> entry: adding.keySet()) {
            if(System.currentTimeMillis()-adding.get(entry)<900000L) {
                if (entry.getValue().equals(name)) {
                    l.add(entry.getKey());
                }
            }

        }
        return l;
    }

    protected boolean addFriendship(String name1, String name2) {
        if(getFriendsNum(name1)<getLimit(name1)&&getFriendsNum(name2)<getLimit(name2)) {
            toAdd.add(new AbstractMap.SimpleEntry<>(name1, name2));
            toAdd.add(new AbstractMap.SimpleEntry<>(name2, name1));
            if (data.containsKey(name1)) {
                List<String> list = data.get(name1);
                list.add(name2);
                data.put(name1, list);
            } else {
                List<String> l = new ArrayList<>();
                l.add(name2);
                data.put(name1, l);
            }
            if (data.containsKey(name2)) {
                List<String> list = data.get(name2);
                list.add(name1);
                data.put(name2, list);
            } else {
                List<String> l = new ArrayList<>();
                l.add(name1);
                data.put(name2, l);
            }

            adding.remove(new AbstractMap.SimpleEntry<>(name2, name1));
            return true;
        }
        else {
            return false;
        }
    }

    protected void removeFriend(String name1, String name2) {
        if(data.containsKey(name1)&&data.get(name1)!=null&&data.get(name1).contains(name2)) {
            List<String> list = data.get(name1);
            list.remove(name2);
            data.put(name1, list);
        }
        if(data.containsKey(name2)&&data.get(name1)!=null&&data.get(name2).contains(name1)) {
            List<String> list = data.get(name2);
            list.remove(name1);
            data.put(name2, list);
        }
        toRemove.add(new AbstractMap.SimpleEntry<>(name1,name2));
        toRemove.add(new AbstractMap.SimpleEntry<>(name2, name1));
    }

    protected List<String> getFriends(String player) {
        if(data.containsKey(player))
            return data.get(player);
        else return null;
    }

    protected boolean areFriends(String n1, String n2) {
        Logger.debug(n1+" "+n2, 0);
        return data.containsKey(n1)&&data.get(n1).contains(n2);
    }

    protected int getFriendsNum(String player) {
        if(data.containsKey(player)) {
            return data.get(player).size();
        }
        else return 0;
    }

    protected void downloadData() {
        data = SQLUtil.getAll();
        limits = SQLUtil.getLimits();
        if(limits!=null) {
            exists = new ArrayList<>(limits.keySet());
        }
        else exists = new ArrayList<>();
        //Util.print(limits);
    }


    protected void upload() {
        Logger.info("开始上传数据");
        for(AbstractMap.SimpleEntry<String, String> e : toAdd) {
            SQLUtil.addFriends(e.getKey(), e.getValue());
        }
        toAdd.clear();
        for(AbstractMap.SimpleEntry<String, String> e : toRemove) {
            SQLUtil.removeFriendship(e.getKey(), e.getValue());
        }
        toRemove.clear();
        for(String e: toChange) {
            //Logger.info("上传Limit");
            if(exists!=null&&exists.contains(e)) {
                //Util.print(limits);
                //Util.print(exists);
                SQLUtil.setLimit1(e,limits.get(e));
            }
            else {
                //Logger.info("Limit2");
                SQLUtil.setLimit2(e,limits.get(e));
            }
        }
        toChange.clear();
        Logger.info("上传结束");
    }

    private class UpdateTask extends TimerTask {
        @Override
        public void run()  {
            upload();
            downloadData();
        }
    }

    private class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if(!DataBase.isClosed(Friends.conn)) SQLUtil.freshConnection();
            else Friends.conn = DataBase.getConnection();
        }
    }

}
