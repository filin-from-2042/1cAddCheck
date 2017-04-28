package com.example.uzmkkonov.a1caddcheck;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class User extends  Object{

    String userID;
    String userName;
    String stockID;
    String stockName;
    String companyID;
    String companyName;
    String cashBoxID;
    String cashBoxName;

    public User(String login, String pwd)
    {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();
        String query = "SELECT TOP 1 US.ID AS  USERID, " +
                            "US.DESCR AS USERNAME, " +
                            "ST.ID AS STOCKID, " +
                            "ST.DESCR AS STOCKNAME, " +
                            "CMP.ID AS COMPANYID, " +
                            "CMP.DESCR AS COMPANYNAME, " +
                            "CB.ID AS CASHBOXID," +
                            "CB.DESCR AS CASHBOXNAME " +
                        "FROM [elbase].[dbo].[SC201] US " +
                        "LEFT JOIN [elbase].[dbo].[SC288] ST ON ST.ID=US.SP191 AND ST.ISMARK=0" +
                        "LEFT JOIN [elbase].[dbo].[SC321] CMP ON CMP.ID=US.SP181 AND CMP.ISMARK=0" +
                        "LEFT JOIN [elbase].[dbo].[SC106] CB ON CB.ID=US.SP183 AND CB.ISMARK=0 " +
                        "WHERE LTRIM(RTRIM(US.CODE)) = '"+login+"' AND US.ISMARK = 0 AND LTRIM(RTRIM(US.SP194))='"+pwd+"' " +
                        "ORDER BY US.ROW_ID DESC";

        try {
            if(con!=null)
            {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    userID = rs.getString(1);
                    userName = rs.getString(2);
                    stockID = rs.getString(3);
                    stockName = rs.getString(4);
                    companyID = rs.getString(5);
                    companyName = rs.getString(6);
                    cashBoxID = rs.getString(7);
                    cashBoxName = rs.getString(8);
                }
            }
        }
        catch (Exception ex)
        {
        }
    }

}
