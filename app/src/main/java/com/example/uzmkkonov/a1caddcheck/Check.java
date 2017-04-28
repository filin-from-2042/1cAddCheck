package com.example.uzmkkonov.a1caddcheck;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


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

    public String save()
    {
        User currUser = (User) DataHolder.getData("LoggedUser");

        if(this.connectionClass == null) this.connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        if(this.checkNumber ==null ) return CheckAddStatus.AddStatus.get(5);

        int createCheckStatus = -1;
        String newCheckID = "";
        String query = "DECLARE @return_value int " +
                        "DECLARE @NEWDOC char(9) " +
                        "EXEC @return_value = [elbase].[dbo].[sp_CreateNewCheck] " +
                        "@docno = '"+this.checkNumber+"', " +
                        "@stockID = '"+((currUser.stockID!=null)?currUser.stockID:"") +"', " +
                        "@cashBox = '"+((currUser.cashBoxID!=null)?currUser.cashBoxID:"")+"', " +
                        "@costSum = "+this.getItemsCosts().toString()+", " +
                        "@addedDocNum=@NEWDOC OUTPUT " +
                        "SELECT @return_value,@NEWDOC";
        try {
            if(con!=null)
            {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    createCheckStatus = rs.getInt(1);
                    if(createCheckStatus != 0 ) return CheckAddStatus.AddStatus.get(createCheckStatus);
                    else
                    {
                        newCheckID = rs.getString(2);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            return "Проблемы с соединением";
        }

        if(this.newItems!=null && this.newItems.size()>0) {
            Integer lineno = 1;
            for (Map.Entry<String, Product> entry : this.newItems.entrySet()) {
                Product product = entry.getValue();
                query = "DECLARE @return_value int " +
                        "EXEC @return_value = [dbo].[sp_addCheckDT] " +
                        "@newFullDocID = '"+newCheckID+"', " +
                        "@lineno = "+lineno.toString()+", " +
                        "@nomID = '"+product.id+"', " +
                        "@cnt = "+String.valueOf(product.count)+", " +
                        "@price = "+product.price.toString()+" " +
                        "SELECT @return_value";

                try {
                    if (con != null) {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            createCheckStatus = rs.getInt(1);
                            if (createCheckStatus != 0)
                                return CheckAddStatus.AddStatus.get(createCheckStatus);
                        }
                    }
                } catch (Exception ex) {
                    return "Проблемы с соединением";
                }
                lineno++;
            }
        }
        return CheckAddStatus.AddStatus.get(createCheckStatus);
    }

    public Double getItemsCosts()
    {
        Double costs = 0d;
        if(this.newItems!=null && this.newItems.size()>0)
        {
            for (Map.Entry<String, Product> entry : this.newItems.entrySet()) {
                Product product = entry.getValue();
                costs += product.price*product.count;
            }
        }
        return costs;
    }

    public boolean updateItemCount(String productID, Double newCount)
    {
            if (this.newItems != null && this.newItems.size()>0) {
                HashMap<String, Product> Items = (HashMap<String, Product>) this.newItems;
                Product editingProduct = Items.get(productID);
                editingProduct.count = newCount;
                Items.put(productID, editingProduct);
                this.newItems = Items;
                return true;
            }else return false;
    }

}
