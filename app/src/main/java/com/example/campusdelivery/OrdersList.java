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
import android.widget.ListView;
import android.widget.Toast;

import com.example.campusdelivery.classes.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrdersList extends AppCompatActivity {
    String username,type;

    private final String URL = Server.ip + "getorders.php";
    ArrayList<Order> records;
    ListView simpleList;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        username=getIntent().getStringExtra("id");
        type=getIntent().getStringExtra("type");

        simpleList = findViewById(R.id.memo_list);
        records = new ArrayList<>();

        getInfos();
    }

    protected void onResume() {
        super.onResume();
        getInfos();
    }
    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(OrdersList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("type", type);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                records.clear();
                String res1[] = new String[0];
                adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.menu_view, R.id.menu_n, res1);
                simpleList.setAdapter(adapter);
                adapter.clear();
                adapter.notifyDataSetChanged();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        records = new ArrayList<>();
                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {
                            JSONObject row = allReq.getJSONObject(i);

                            Order temp=new Order();
                            temp.setOrder_id(row.getString("order_id"));
                            temp.setTitle(row.getString("title"));
                            temp.setProvider_name(row.getString("provider_name"));
                            temp.setCustomer_name(row.getString("customer_name"));
                            temp.setQuantity(row.getString("quantity"));
                            temp.setAdd_date(row.getString("add_date"));
                            temp.setLat(row.getString("lat"));
                            temp.setLon(row.getString("lon"));
                            temp.setTotal(row.getString("total"));
                            temp.setAddress(row.getString("address"));
                            temp.setState(row.getString("state"));
                            temp.setDelivery_agent(row.getString("delivery_guy"));
                            temp.setDeliver_date(row.getString("deliver_date")+"");
                            temp.setDeliver_time(row.getString("deliver_time")+"");
                            temp.setResponse(row.getString("response"));

                            records.add(temp);

                        }

                        String res[] = new String[records.size()];
                        for (int j = 0; j < records.size(); j++) {
                            res[j] =records.get(j).getCustomer_name()+"\nTotal: "+records.get(j).getTotal()+"\nTime: "+records.get(j).getAdd_date();
                        }
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.menu_view, R.id.menu_n,res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(OrdersList.this, OrderDetails.class);
                                intent.putExtra("item", records.get(position));
                                intent.putExtra("type", type);
                                intent.putExtra("id", username);
                                startActivity(intent);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Async la = new Async();
        la.execute();
    }
}