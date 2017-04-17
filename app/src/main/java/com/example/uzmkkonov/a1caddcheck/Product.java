package com.example.uzmkkonov.a1caddcheck;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Set on 12.04.2017.
 */

public class Product {
    private String id;
    public String code;
    public String name;
    public String count = "1";
    public String price;
    public ArrayList<Map<String,String>> remains;

    ConnectionClass connectionClass;

    public Product(){
        super();
    }

    public Product(String id, String code, String name) {
        super();
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public void fillPrice()
    {

        if(this.connectionClass == null) this.connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "SELECT NOMID, CODE, DESCR, WEIGHT, NOMPRICE FROM [elbase].[dbo].[GetNomenclatureData]('"+this.id+"')";
        try {
            if(con!=null)
            {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    this.price =  rs.getString(5);
                }
            }
        }
        catch (Exception ex)
        {
        }

    }

    public void fillRemains()
    {
        if(this.connectionClass == null) this.connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();
        String query = "SELECT STOREID, STORENAME, UNITCOUNT FROM [elbase].[dbo].[GetUnitRemains]('"+this.id+"')";

        try {
            if(con!=null)
            {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    if(this.remains == null) this.remains = new ArrayList<Map<String,String>>();
                    Map<String,String> remain = new HashMap<String, String>();
                    remain.put("storeID", rs.getString(1));
                    remain.put("storeDESCR", rs.getString(2));
                    remain.put("unitCount", rs.getString(3));

                    this.remains.add(remain);
                }
            }
        }
        catch (Exception ex)
        {
        }
    }

    @Override
    public String toString() {
        return this.code + ". " + this.name ;
    }
}
