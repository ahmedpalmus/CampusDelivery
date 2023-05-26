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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.campusdelivery.classes.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ContentList extends AppCompatActivity {
    String username,type,field;
    Button add;

    ArrayList<MenuItem> records;
    private final String URL = Server.ip + "getcontent.php";

    ListView simpleList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        username=getIntent().getStringExtra("id");
        type=getIntent().getStringExtra("type");
        field=getIntent().getStringExtra("field");

        add=findViewById(R.id.new_member);

        simpleList = findViewById(R.id.memo_list);
        records = new ArrayList<>();


        if(!type.equals("provider"))
            add.setVisibility(View.GONE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ContentList.this,AddItem.class);
                intent.putExtra("id",username);
                intent.putExtra("op_type","add");
                intent.putExtra("field",field);
                startActivity(intent);
            }
        });

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
                loadingDialog = ProgressDialog.show(ContentList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("type", type);
                data.put("field", field);

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

                            MenuItem temp=new MenuItem();
                            temp.setItem_id(row.getString("item_id"));
                            temp.setItem_title(row.getString("item_title"));
                            temp.setPrice(row.getString("price"));
                            temp.setDetail(row.getString("detail"));
                            temp.setImage(row.getString("image"));

                            records.add(temp);

                        }

                        String res[] = new String[records.size()];
                        for (int j = 0; j < records.size(); j++) {
                            res[j] =records.get(j).getItem_title()+"\n"+records.get(j).getPrice()+" SR";
                        }
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.menu_view, R.id.menu_n,res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(ContentList.this, ItemDetails.class);
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