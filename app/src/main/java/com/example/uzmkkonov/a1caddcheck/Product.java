package com.example.uzmkkonov.a1caddcheck;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Product {
    ///TODO: получение id должно быть через  get
    public String id;
    public String code;
    public String articul;
    public String name;
    public double count = 1.0;
    public Double price;
    public ArrayList<StoreRemainUnit> remains;

    ConnectionClass connectionClass;

    public Product(){
        super();
    }

    public Product(String id, String code, String name, String articul) {
        super();
        this.id = id;
        this.code = code;
        this.articul = articul;
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
                    this.price =  rs.getDouble(5);
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
                    if(this.remains == null) this.remains = new ArrayList<StoreRemainUnit>();
                    StoreRemainUnit remain = new StoreRemainUnit(rs.getString(1),rs.getString(2), rs.getDouble(3));
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
        return this.code + " | " + this.name ;
    }
}