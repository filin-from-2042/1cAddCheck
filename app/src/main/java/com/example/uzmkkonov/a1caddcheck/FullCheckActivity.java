package com.example.uzmkkonov.a1caddcheck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class FullCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_check);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        fillTableItems();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button addItemBtn = (Button) findViewById(R.id.addItemBtn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchRequested();
            }
        });
    }

    protected void fillTableItems()
    {
        TableLayout table = (TableLayout) findViewById(R.id.itemList);
        if(DataHolder.getData("DataItemsTmp") != null)
        {
            ArrayList<Product> ItemsData = (ArrayList<Product>) DataHolder.getData("DataItemsTmp");
            for (Product product: ItemsData) {

                TableRow row=new TableRow(FullCheckActivity.this);

                TextView code = new  TextView(FullCheckActivity.this);
                code.setTextSize(10);
                code.setText(product.code);
                row.addView(code);

                TextView name = new  TextView(FullCheckActivity.this);
                name.setTextSize(10);
                name.setText(product.name);
                row.addView(name);

                table.addView(row);
            }
        }

    }

}
