package com.example.campusdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class LoginPage extends AppCompatActivity {
    EditText user, password;
    String username, pass;
    Spinner types;
    String URL = Server.ip + "login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        user = findViewById(R.id.log_user);
        password = findViewById(R.id.log_pass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user.setText("");
        password.setText("");
    }

    public void logClick(View v) {
        if (v.getId() == R.id.log_log) {
            username = user.getText().toString().trim();
            pass = password.getText().toString().trim();
            if (username.length() > 2) {
                login();
            } else {
                Toast.makeText(this, getResources().getString(R.string.loginError), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void login() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginPage.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", params[0]);
                data.put("password", params[1]);
                String result = con.sendPostRequest(URL, data);
                return result.trim();
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.userExist), Toast.LENGTH_LONG).show();
                }else if (result.equals("block")) {
                    Toast.makeText(getApplicationContext(), "This Account is blocked", Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("customer")) {
                    Intent intent = new Intent(LoginPage.this, UserPage.class);
                    intent.putExtra("id", username);
                    startActivity(intent);
                } else if (result.equalsIgnoreCase("delivery guy")) {
                    Intent intent = new Intent(LoginPage.this, DeliveryPage.class);
                    intent.putExtra("id", username);
                    startActivity(intent);
                }else if (result.equalsIgnoreCase("admin")) {
                    Intent intent = new Intent(LoginPage.this, AdminPage.class);
                    intent.putExtra("id", username);
                    startActivity(intent);
                }else if (result.equalsIgnoreCase("provider")) {
                    Intent intent = new Intent(LoginPage.this, Provider.class);
                    intent.putExtra("id", username);
                    startActivity(intent);
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute(username, pass);
    }

}
