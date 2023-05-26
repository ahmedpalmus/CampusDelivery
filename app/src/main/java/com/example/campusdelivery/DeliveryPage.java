package com.example.campusdelivery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;

public class DeliveryPage extends AppCompatActivity {
    String id,status;
    Button orders,support,profile;
    Switch deliv_status;
    private final String URL = Server.ip + "getstatus.php";
    private final String URL2 = Server.ip + "setstatus.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_page);

        deliv_status=findViewById(R.id.deliv_status);
        orders=findViewById(R.id.orders);
        support=findViewById(R.id.the_support);
        profile=findViewById(R.id.the_profile);

        id=getIntent().getStringExtra("id");


        deliv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DeliveryPage.this);
                alert.setTitle("Changing status");
                alert.setMessage("Are You Sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ChanegStatus();

                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.create().show();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(DeliveryPage.this, Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeliveryPage.this,SupportList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","user");
                intent.putExtra("support_type","comp");
                startActivity(intent);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeliveryPage.this,OrdersList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","delivery guy");
                startActivity(intent);
            }
        });
        getInfos();
    }

    public void ChanegStatus(){
        if(status.equalsIgnoreCase("Not Available")){
            deliv_status.setChecked(true);
            deliv_status.setText("Available");
            status="Available";
            SendInfo();
        }else{
            deliv_status.setChecked(false);
            deliv_status.setText("Not Available");
            status="Not Available";
            SendInfo();
        }
    }
    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(DeliveryPage.this, "please waite...", "Connecting....");
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
                    status=result;
                    if(status.equalsIgnoreCase("Available")){
                        deliv_status.setChecked(true);
                        deliv_status.setText("Available");
                    }else{
                        deliv_status.setChecked(false);
                        deliv_status.setText("Not Available");
                    }
                }
            }
        }
        Async la = new Async();
        la.execute();
    }
    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(DeliveryPage.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put("state",status);

                String result = con.sendPostRequest(URL2, data);
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noResult), Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }

}