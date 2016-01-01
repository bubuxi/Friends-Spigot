package net.bubuxi.mc.friends.database;

/**
 * Created by zekunshen on 12/27/15.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.bubuxi.mc.friends.Friends;
import net.bubuxi.mc.friends.Logger;


public class DataBase {

    private static String driver="com.mysql.jdbc.Driver";
    private static String port = Friends.getInstance().getConfig().getString("database.port");
    public static String table = Friends.getInstance().getConfig().getString("database.table");
    private static String url="jdbc:mysql://"+Friends.getInstance().getConfig().getString("database.url")+":"+port+"/"+table+"?characterEncoding=UTF-8";
    private static String user = Friends.getInstance().getConfig().getString("database.user");
    private static String password=Friends.getInstance().getConfig().getString("database.password");

    public static Connection getConnection(){
        Connection conn=null;
        try{
            Class.forName(driver);
            Logger.debug("user:"+user, 1);
            Logger.debug("password:"+password, 1);
            Logger.debug("database:"+table, 1);
            Logger.debug("url:"+url, 1);
            conn=DriverManager.getConnection(url,user,password);
            if(!conn.isClosed()){
                Logger.info("Mysql开启");
            }
        }catch(ClassNotFoundException e){
            Logger.warning("MYSQL驱动不存在");
        }catch(SQLException e){
            e.printStackTrace();
            Logger.warning("MYSQL出错");
        }
        return conn;
    }


    public static void closeConection(Connection conn){
        if(conn!=null){
            try{
                conn.close();
                Logger.info("Mysql关闭");
            }catch(SQLException e){
                Logger.warning("Mysql关闭失败");
            }
        }
    }

    public static boolean isClosed (Connection conn) {
        if(conn!=null) {
            try {
                return conn.isClosed();
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }
}
