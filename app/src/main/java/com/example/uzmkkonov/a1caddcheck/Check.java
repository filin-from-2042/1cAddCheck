package com.example.uzmkkonov.a1caddcheck;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class Check {

    ConnectionClass connectionClass;
    String checkNumber;
    Map<String,Product> newItems;

    public Check()
    {
        super();
        this.initNewCheckNumber();
    }

    // Блокировка нового номера чека в БД
    public void initNewCheckNumber()
    {
        if(this.connectionClass == null) this.connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "EXEC ["+ConnectionClass.db+"].["+ConnectionClass.scheme+"].[sp_initNewCheck]";
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
    // Удаление блокировки на номер чека в БД
    public void checkNumberUnlock()
    {
        if(this.connectionClass == null) this.connectionClass = new ConnectionClass();
        Connection con = connectionClass.CONN();

        String query = "EXEC ["+ConnectionClass.db+"].["+ConnectionClass.scheme+"].[sp_BlockDocNumber] " +
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
    // сохранения нового чека со всеми данными в БД
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
                        "EXEC @return_value = ["+ConnectionClass.db+"].["+ConnectionClass.scheme+"].[sp_CreateNewCheck] " +
                        "@docno = '"+this.checkNumber+"', " +
                        "@stockID = '"+((currUser.stockID!=null)?currUser.stockID:"") +"', " +
                        "@cashBox = '"+((currUser.cashBoxID!=null)?currUser.cashBoxID:"")+"', " +
                        "@costSum = "+this.getItemsCosts().toString()+", " +
                        "@authorID = '"+((currUser.userID!=null)?currUser.userID:"")+"', " +
                        "@authorCompanyID = '"+((currUser.checkCompanyID!=null)?currUser.checkCompanyID:"")+"', " +
                        "@authorCurrProject = '"+((currUser.currProject!=null)?currUser.currProject:"")+"', " +
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
                        "EXEC @return_value = ["+ConnectionClass.db+"].["+ConnectionClass.scheme+"].[sp_addCheckDT] " +
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
    // Возвращает полную стоимость чека исходя из добавленного кол-ва товаров и их стоимости
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

    // Обновляет кол-во товара в чеке по переданному идентификатору
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
    // удаляет товара из чека по переданнму идентификатору
    public void removeItemById(String itemID)
    {
        if (this.newItems != null && this.newItems.size()>0) {
            for (Map.Entry<String, Product> entry : this.newItems.entrySet()) {
                Product product = entry.getValue();
                if(product.id.equals(itemID)) this.newItems.remove(product.id);
            }
        }
    }

}
