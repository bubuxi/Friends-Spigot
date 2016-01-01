package net.bubuxi.mc.friends.database;

import net.bubuxi.mc.friends.Friends;
import net.bubuxi.mc.friends.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zekunshen on 12/27/15.
 */
public class SQLUtil {

    public static String freshSql = "SELECT 1";

    public static String createSql= "CREATE TABLE FRIENDS(FRIEND_ID int(16) NOT NULL AUTO_INCREMENT, PLAYER1 varchar(32) NOT NULL,PLAYER2 varchar(32) NOT NULL, VALID int(1) NOT NULL DEFAULT 1,PRIMARY KEY(FRIEND_ID)) ";
    public static String getAllSql = "SELECT PLAYER1, PLAYER2 FROM FRIENDS WHERE VALID=1";
    public static String insertFriendSql= "INSERT INTO FRIENDS(PLAYER1, PLAYER2) VALUES(?, ?)";
    public static String getFriendsSql= "SELECT PLAYER2 FROM FRIENDS WHERE PLAYER1=? AND VALID=1";
    public static String removeFriendshipSql = "UPDATE FRIENDS SET VALID=0 WHERE PLAYER1=? AND PLAYER2=?";
    public static String removeInvalidSql = "DELETE FROM FRIENDS WHERE VALID=0";

    public static String createLimitSql = "CREATE TABLE LIMITS(PLAYER varchar(32) NOT NULL, FLIMIT int(16) NOT NULL)";
    public static String getLimits = "SELECT PLAYER, FLIMIT FROM LIMITS";
    public static String setLimits = "UPDATE LIMITS SET FLIMIT =? WHERE PLAYER=?";
    public static String setNewLimits = "INSERT INTO LIMITS (PLAYER, FLIMIT) VALUES(?,?)";


    public static void freshConnection() {
        Statement st = null;
        try {
            st = Friends.conn.createStatement();
            st.executeQuery(freshSql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(st!=null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void createTable() {
        Statement st = null;
        try {
            st = Friends.conn.createStatement();
            if(st.execute(createSql)) {
                Logger.debug("表格创建成功", 0);
            }else {
                Logger.debug("表格创建失败", 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(st!=null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public static HashMap<String, List<String>> getAll() {
        Statement st =null;
        try {
            HashMap<String, List<String>> hm = new HashMap<>();
            st = Friends.conn.createStatement();
            ResultSet rs = st.executeQuery(getAllSql);
            while (rs.next()) {
                String s1 = rs.getString(1);
                String s2 = rs.getString(2);
                if (hm.containsKey(s1)) {
                    hm.get(s1).add(s2);
                } else {
                    List<String> l = new ArrayList<String>();
                    l.add(s2);
                    hm.put(s1, l);
                }
            }
            return hm;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(st!=null) st.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void addFriends(String name1, String name2) {
        PreparedStatement ps = null;
        try {
            ps = Friends.conn.prepareStatement(insertFriendSql);
            ps.setString(1, name1);
            ps.setString(2, name2);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getFriends(String name) {
        List<String> list = new ArrayList<String>();
        PreparedStatement ps = null;
        try {
            ps = Friends.conn.prepareStatement(getFriendsSql);
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                return list;
            }
        }
    }

    public static void removeFriendship(String name1, String name2) {
        PreparedStatement ps = null;
        try {
            ps = Friends.conn.prepareStatement(removeFriendshipSql);
            ps.setString(1, name1);
            ps.setString(2, name2);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeInvalidFriends() {
        PreparedStatement ps = null;
        try {
            ps = Friends.conn.prepareStatement(removeInvalidSql);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.debug("移除无效好友失败",1);
            e.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public static void createLimit() {
        Statement st = null;
        try {
            st = Friends.conn.createStatement();
            if(st.execute(createLimitSql)) {
                Logger.debug("表格创建成功", 0);
            }else {
                Logger.debug("表格创建失败", 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(st!=null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static HashMap<String, Integer> getLimits() {
        PreparedStatement ps = null;
        try {
            HashMap<String, Integer> hm = new HashMap<>();
            ps = Friends.conn.prepareStatement(getLimits);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                //Logger.info(rs.getString(1)+rs.getInt(2));
                hm.put(rs.getString(1), rs.getInt(2));
            }
            return hm;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return new HashMap<>();
    }
    public static void setLimit1(String name, int l) {
        PreparedStatement ps = null;
        try {
            ps = Friends.conn.prepareStatement(setLimits);
            ps.setString(2, name);
            ps.setInt(1, l);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setLimit2(String name, int l) {
        PreparedStatement ps = null;
        try {
            ps = Friends.conn.prepareStatement(setNewLimits);
            //Logger.info(name+l);
            ps.setString(1, name);
            ps.setInt(2, l);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
