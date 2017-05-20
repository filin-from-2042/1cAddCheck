package com.example.uzmkkonov.a1caddcheck;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionClass {
    String ip = "192.168.1.100:2301";
//    String ip = "192.168.0.105:2301";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    public static String db = "elektrika";
//    public static String db = "elbase";
    public static String scheme = "dbo";
   public static String un = "el";
//    String un = "sa";
    String password = "Qwe12345";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);
          /*  ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";progName=1cAddCheck;";
*/
            ConnURL = "jdbc:jtds:sqlserver://" + ip ;

            Properties props = new Properties();
            props.setProperty("databaseName",db);
            props.setProperty("user",un);
            props.setProperty("password",password);
            props.setProperty("progName","1cAddCheck");
            //props.setProperty("processId","1234");

            conn = DriverManager.getConnection(ConnURL,props);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}
