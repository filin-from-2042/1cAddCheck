package com.example.uzmkkonov.a1caddcheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    EditText edtuserid,edtpass;
    Button btnlogin;
    ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionClass = new ConnectionClass();
        edtuserid = (EditText) findViewById(R.id.edtuserid);
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");

            }
        });

    }

    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;


        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                /*
                Intent i = new Intent(MainActivity.this, AddProducts.class);
                startActivity(i);
                finish();
                */
                //Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT);
            }
        }

        @Override
        protected String doInBackground(String... params) {
//            if(userid.trim().equals("")|| password.trim().equals(""))
            if(userid.trim().equals(""))
                z = "Please enter User Id and Password";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "SELECT [elbase].[dbo].[UserAuth]('" + userid + "','" + password + "')";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {
                            if(rs.getInt(1)==1) {

                                z = "Login successfull";
                                isSuccess = true;
                                return z;
                            }
                        }
                        z = "Invalid Credentials";
                        isSuccess = false;
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }

}
