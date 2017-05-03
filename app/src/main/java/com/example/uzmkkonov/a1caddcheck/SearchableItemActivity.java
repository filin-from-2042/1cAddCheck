package com.example.uzmkkonov.a1caddcheck;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchableItemActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_item);


        ListView list= (ListView)findViewById(android.R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product productItem = (Product)parent.getItemAtPosition(position);
                productItem.fillRemains();
                productItem.fillPrice();
                if(DataHolder.getData("newCheck")!=null) {
                    Check newCheck = (Check) DataHolder.getData("newCheck");
                    if (newCheck.newItems != null) {
                        // TODO: Добавить функцию добавления эелемента в класс Check
                        LinkedHashMap<String, Product> oldItems = (LinkedHashMap<String, Product>) newCheck.newItems;
                        oldItems.put(productItem.id, productItem);
                        newCheck.newItems = oldItems;
                    } else {
                        LinkedHashMap<String, Product> saveItems = new LinkedHashMap <String, Product>();
                        saveItems.put(productItem.id, productItem);
                        newCheck.newItems = saveItems;
                    }
                    DataHolder.setData("newCheck", newCheck);
                    finish();
                }
            }
        });

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String search = intent.getStringExtra(SearchManager.QUERY);

            ConnectionClass connectionClass = new ConnectionClass();
            Connection con = connectionClass.CONN();
            String query = "SELECT [ID]" +
                    "      ,[CODE]" +
                    "      ,[DESCR]" +
                    "      ,[SP131] "+
                    "  FROM [elbase].[dbo].[SC148]" +
                    "  WHERE (CODE LIKE '%"+search+"%' OR DESCR LIKE '%"+search+"%') AND ISMARK=0 AND ISFOLDER=2" +
                    "  ORDER BY ROW_ID DESC";

            ArrayList<Product> items = new ArrayList<Product>();
            try {
                if(con!=null)
                {
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        items.add(new Product(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
                    }
                }
            }
            catch (Exception ex)
            {
            }
            ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this,
                    android.R.layout.simple_list_item_1, items);

            setListAdapter(adapter);
        }
    }
}
