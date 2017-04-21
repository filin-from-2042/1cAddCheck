package com.example.uzmkkonov.a1caddcheck;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;


public class Check {

    ConnectionClass connectionClass;
    String checkNumber;
    HashMap<String,Product> newItems;

    public Check()
    {
        super();
        this.initNewCheckNumber();
    }

    public void initNewCheckNumber()
    {
        if(this.connectionClass == null) this.connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "EXEC [dbo].[sp_initNewCheck]";
        try {
            if(con!=null)
            {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    this.checkNumber =  rs.getString(1);
                }
            }
        }
        catch (Exception ex)
        {
        }
    }

    public void checkNumberUnlock()
    {
        if(this.connectionClass == null) this.connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "EXEC [dbo].[sp_BlockDocNumber] " +
                "@DNPREFIX = '"+DocumentTypes.CHECK+"'," +
                "@DOCNUMBER = '"+this.checkNumber+"'," +
                "@BLOCK = 0";
        try {
            if(con!=null)
            {
                Statement stmt = con.createStatement();
                boolean rs = stmt.execute(query);

                this.checkNumber = null;
            }
        }
        catch (Exception ex)
        {
        }

    }
}
