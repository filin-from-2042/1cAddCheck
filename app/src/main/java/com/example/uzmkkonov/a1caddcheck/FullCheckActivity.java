package com.example.uzmkkonov.a1caddcheck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullCheckActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<StoreRemainUnit> currentRemains = new ArrayList<StoreRemainUnit>();
    ArrayAdapter<StoreRemainUnit> remainsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_check);
        // обработка exception
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                unleashCheckNumber();
            }
        });

        // кнопка поиска товара
        Button addItemBtn = (Button) findViewById(R.id.addItemBtn);
        addItemBtn.setOnClickListener(this);
        // кнопка поиска товара
        Button saveCheckBtn = (Button) findViewById(R.id.saveCheck);
        saveCheckBtn.setOnClickListener(this);

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

        fillTableItems();

        TextView checkNumberText = (TextView)findViewById(R.id.checkNumberText);
        checkNumberText.setText(newCheck.checkNumber);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unleashCheckNumber();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHolder.setData("newCheck", null);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            // кнопка сохранение нового чека
            case R.id.saveCheck:
            {
                if(DataHolder.getData("newCheck") != null)
                {
                    Check newCheck = (Check) DataHolder.getData("newCheck");
                    String checkRes = newCheck.save();
                    Toast.makeText(FullCheckActivity.this, checkRes ,Toast.LENGTH_SHORT).show();
                }
            }break;
            // кнопка поиска товара
            case R.id.addItemBtn:
            {
                onSearchRequested();
            }break;
        }
    }

    // заполнение таблицы с товарами в чеке
    protected void fillTableItems()
    {
        TableLayout table = (TableLayout) findViewById(R.id.itemList);
        table.removeAllViews();
        if(DataHolder.getData("newCheck") != null)
        {
            Check newCheck = (Check) DataHolder.getData("newCheck");
            if(newCheck.newItems!= null && newCheck.newItems.size()>0) {

                final TextView itemsSummData = (TextView)findViewById(R.id.itemsSummData);
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
                    cnt.setText(product.count.toString());
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

                                if(DataHolder.getData("newCheck")!=null) {
                                    Check newCheck = (Check) DataHolder.getData("newCheck");
                                    if(newCheck.updateItemCount(product.id,countParsed)) {
                                        itemsSummData.setText(newCheck.getItemsCosts().toString());
                                        DataHolder.setData("newCheck", newCheck);
                                    }
                                }
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
                itemsSummData.setText(newCheck.getItemsCosts().toString());
            }
        }
    }

    protected void unleashCheckNumber()
    {
        if(DataHolder.getData("newCheck") != null) {
            Check newCheck = (Check) DataHolder.getData("newCheck");
            newCheck.checkNumberUnlock();
        }
    }

}
