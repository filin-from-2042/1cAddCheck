package com.example.uzmkkonov.a1caddcheck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullCheckActivity extends AppCompatActivity {

    ArrayList<StoreRemainUnit> currentRemains = new ArrayList<StoreRemainUnit>();
    ArrayAdapter<StoreRemainUnit> remainsAdapter;

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
        // инициализация списка с остатками
        ListView remainsLst = (ListView)findViewById(R.id.remainsList);
        this.currentRemains = new ArrayList<StoreRemainUnit>();
        this.remainsAdapter = new ArrayAdapter<StoreRemainUnit>(this,
                R.layout.remains_list_text, this.currentRemains);
        remainsLst.setAdapter(this.remainsAdapter);
    }

    // заполнение таблицы с товарами в чеке
    protected void fillTableItems()
    {
        TableLayout table = (TableLayout) findViewById(R.id.itemList);
        if(DataHolder.getData("DataItemsTmp") != null)
        {
            HashMap<String,Product> ItemsData = (HashMap<String,Product>) DataHolder.getData("DataItemsTmp");
            for (Map.Entry<String, Product> entry: ItemsData.entrySet()) {

                Product product = entry.getValue();

                TableRow row=new TableRow(FullCheckActivity.this);

                TextView code = new  TextView(FullCheckActivity.this);
                code.setTextSize(18);
                code.setText(product.code);
                row.addView(code);

                TextView name = new  TextView(FullCheckActivity.this);
                name.setTextSize(18);
                name.setText(product.name);
                row.addView(name);
                // скрытое поле с id товара в чеке
                TextView nomid = new  TextView(FullCheckActivity.this);
                nomid.setText(product.id);
                nomid.setVisibility(View.INVISIBLE);
                row.addView(nomid);

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onTableRowClick(view);
                    }
                });

                table.addView(row);
            }
        }

    }
    // получение остатков по номенклатуре и заполнение соответсвующего списка
    public boolean onTableRowClick(View view)
    {
        TextView nomIDV = (TextView)((TableRow)view).getChildAt(2);
        String nomID = nomIDV.getText().toString();
        if(DataHolder.getData("DataItemsTmp") != null)
        {
            HashMap<String,Product> dataFromHolder = (HashMap<String,Product>)DataHolder.getData("DataItemsTmp");
            Product productData = dataFromHolder.get(nomID);
            this.currentRemains.clear();
            if(productData.remains!=null && productData.remains.size() > 0) {
                for (StoreRemainUnit tprd : productData.remains) {
                    this.currentRemains.add(tprd);
                }
            }
            else
            {
                ///TODO: механизм для вывода отсутсвующих остатков
                StoreRemainUnit emptyRemain = new StoreRemainUnit("0","Нет в наличии","0");
                this.currentRemains.add(emptyRemain);
            }
            this.remainsAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

}
