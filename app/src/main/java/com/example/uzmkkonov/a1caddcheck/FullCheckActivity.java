package com.example.uzmkkonov.a1caddcheck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        // кнопка поиска товара
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

    @Override
    protected void onStart() {
        super.onStart();

        // данные по чеку
        Check newCheck;
        if(DataHolder.getData("newCheck") == null) {
            newCheck = new Check();
            DataHolder.setData("newCheck", newCheck);
        }
        else {
            newCheck = (Check) DataHolder.getData("newCheck");
            if(newCheck.checkNumber==null) newCheck.initNewCheckNumber();
        }

        TextView checkNumberText = (TextView)findViewById(R.id.checkNumberText);
        checkNumberText.setText(newCheck.checkNumber);
    }

    // заполнение таблицы с товарами в чеке
    protected void fillTableItems()
    {
        TableLayout table = (TableLayout) findViewById(R.id.itemList);
        if(DataHolder.getData("newCheck") != null)
        {
            Check newCheck = (Check) DataHolder.getData("newCheck");
            if(newCheck.newItems.size()>0) {
                for (Map.Entry<String, Product> entry : newCheck.newItems.entrySet()) {

                    final Product product = entry.getValue();

                    TableRow row = new TableRow(FullCheckActivity.this);

                    TextView code = new TextView(FullCheckActivity.this);
                    code.setTextSize(18);
                    code.setText(product.code + " | ");

                    TextView name = new TextView(FullCheckActivity.this);
                    name.setTextSize(18);
                    name.setText(product.name.substring(0,19));
                    name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String nomID = product.id;
                            if(DataHolder.getData("newCheck") != null)
                            {
                                Check newCheck = (Check) DataHolder.getData("newCheck");

                                if(newCheck.newItems.size()>0) {
                                    Product productData = newCheck.newItems.get(nomID);
                                    currentRemains.clear();
                                    if (productData.remains != null && productData.remains.size() > 0) {
                                        for (StoreRemainUnit tprd : productData.remains) {
                                            currentRemains.add(tprd);
                                        }
                                    } else {
                                        ///TODO: механизм для вывода отсутсвующих остатков
                                        StoreRemainUnit emptyRemain = new StoreRemainUnit("0", "Нет в наличии", 0.0);
                                        currentRemains.add(emptyRemain);
                                    }
                                    remainsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

                    final TextView price = new TextView(FullCheckActivity.this);
                    price.setTextSize(18);
                    price.setText(String.format("%.2f",product.price)+" Р");

                    final EditText  cnt = new EditText(FullCheckActivity.this);
                    cnt.setTextSize(18);
                    cnt.setText(product.count);
                    cnt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!hasFocus &&  cnt.getText().toString().isEmpty()) {
                                cnt.setText("1");
                            }
                        }
                    });

                    cnt.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(s.length() != 0){
                                double priceParsed = product.price;
                                double countParsed = Double.parseDouble(s.toString());
                                Double res = priceParsed*countParsed;
                                price.setText(String.format("%.2f",res)+" Р");

                            }
                        }
                    });

                    // скрытое поле с id товара в чеке
                    TextView nomid = new TextView(FullCheckActivity.this);
                    nomid.setText(product.id);
                    nomid.setVisibility(View.INVISIBLE);

                    row.addView(code);
                    row.addView(name);
                    row.addView(cnt);
                    row.addView(price);
                    row.addView(nomid);

                    table.addView(row);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(DataHolder.getData("newCheck") != null) {
            Check newCheck = (Check) DataHolder.getData("newCheck");
            newCheck.checkNumberUnlock();
        }
    }

}
