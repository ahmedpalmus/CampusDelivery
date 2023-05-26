package com.example.campusdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class AddSupport extends AppCompatActivity {

    String username,Title,Detail,support_type;
    EditText title,detail;
    String URL = Server.ip + "sendcomp.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_support);
        username = getIntent().getStringExtra("id");
        support_type = getIntent().getStringExtra("support_type");


        title=findViewById(R.id.sub_title);
        detail=findViewById(R.id.sub_details);
    }

    public void NewCompClick(View view) {
        if (view.getId() == R.id.sub_save) {
            Title=title.getText().toString().trim();
            Detail=detail.getText().toString().trim();
            if(Title.length()<3){
                Toast.makeText(AddSupport.this,"Enter valid title",Toast.LENGTH_LONG).show();
            }else if(Detail.length()<5){
                Toast.makeText(AddSupport.this,"Enter valid details",Toast.LENGTH_LONG).show();
            }else{
                sendComp();
            }

        } if (view.getId() == R.id.sub_cancel) {
            finish();
        }

    }
    private void sendComp() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(AddSupport.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", params[0]);
                data.put("title", params[1]);
                data.put("detail", params[2]);
                data.put("support_type", support_type);
                String result = con.sendPostRequest(URL, data);
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
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute(username,Title,Detail);
    }

}
