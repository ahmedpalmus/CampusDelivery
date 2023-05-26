package com.example.campusdelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.campusdelivery.classes.CompAdapter;
import com.example.campusdelivery.classes.Support;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SupportList extends AppCompatActivity {
    ArrayList<Support> comps;
    private final String URL = Server.ip + "getcomps.php";
    String username, type,support_type;
    private RecyclerView dRecycle;
    private CompAdapter dAdapter;
    Button newcomp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_list);
        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        support_type = getIntent().getStringExtra("support_type");

        newcomp=findViewById(R.id.new_support);

        comps = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        dRecycle = findViewById(R.id.comp_list);
        dRecycle.setHasFixedSize(true);
        dRecycle.setLayoutManager(linearLayoutManager);

        dRecycle.smoothScrollToPosition(comps.size());
        dAdapter = new CompAdapter(comps);
        dAdapter.setOnItemClickListener(new CompAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Support temp = comps.get(position);
                Intent intent = new Intent(SupportList.this, SupportDetails.class);
                intent.putExtra("support", temp);
                intent.putExtra("id", username);
                intent.putExtra("type", type);
                intent.putExtra("support_type", support_type);
                startActivity(intent);
            }
        });

        newcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportList.this, AddSupport.class);
                intent.putExtra("id", username);
                intent.putExtra("support_type", support_type);
                startActivity(intent);
                finish();
            }
        });
        dRecycle.setAdapter(dAdapter);
        if(type.equals("admin")){
            newcomp.setVisibility(View.GONE);
        }else if(type.equals("provider") && support_type.equalsIgnoreCase("Student Affairs")){
            newcomp.setVisibility(View.GONE);
        }
        getInfos();
    }

    @Override
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
                loadingDialog = ProgressDialog.show(SupportList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("type", type);
                data.put("support_type", support_type);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                comps.clear();
                dAdapter.notifyDataSetChanged();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No results", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        comps = new ArrayList<>();
                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {

                            JSONObject row = allReq.getJSONObject(i);
                            Support temp = new Support();
                            temp.setSupp_id(row.getString("supp_id"));
                            temp.setSupp_title(row.getString("supp_title"));
                            temp.setSupp_detail(row.getString("supp_detail"));
                            temp.setSupp_date(row.getString("supp_date"));
                            temp.setSupp_response(row.getString("supp_response"));
                            temp.setUsername(row.getString("username"));
                            comps.add(temp);
                        }
                        dAdapter.setmList(comps);
                        dAdapter.notifyDataSetChanged();

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
