package com.example.campusdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusdelivery.classes.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Provider extends AppCompatActivity {
    String id,field;
    Button content,orders,support,profile;
    private final String URL = Server.ip + "getfield.php";
TextView l3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        content=findViewById(R.id.content);
        orders=findViewById(R.id.orders);
        support=findViewById(R.id.the_support);
        profile=findViewById(R.id.the_profile);
        l3=findViewById(R.id.l3);

        id=getIntent().getStringExtra("id");


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Provider.this,Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Provider.this,SupportList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","user");
                intent.putExtra("support_type","comp");
                startActivity(intent);
            }
        });
        getInfos();
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(field.equalsIgnoreCase("Student Affairs")){
                    Intent intent=new Intent(Provider.this,SupportList.class);
                    intent.putExtra("id",id);
                    intent.putExtra("type","provider");
                    intent.putExtra("support_type","student affairs");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Provider.this, ContentList.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "provider");
                    intent.putExtra("field", field);
                    startActivity(intent);
                }
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Provider.this,OrdersList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","provider");
                startActivity(intent);
            }
        });
    }

    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Provider.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", id);

                String result = con.sendPostRequest(URL, data);
                return result.trim();
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();

                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_LONG).show();
                } else{
                    field=result;
                    l3.setText(field +" Account");
                    if(field.equalsIgnoreCase("student affairs")){
                        orders.setVisibility(View.GONE);
                    }
                }
            }
        }
        Async la = new Async();
        la.execute();
    }

}