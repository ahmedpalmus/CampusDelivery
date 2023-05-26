package com.example.campusdelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    EditText pass, pass2, perName, mail, phone, eaddress;

    String username, type, password, name, email, thePhone, Address;
    String URL = Server.ip + "Edit.php";
    String URL2 = Server.ip + "getUser.php";
    String URL3 = Server.ip + "delUser.php";

    Button del;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");


        pass = findViewById(R.id.prof_pass);
        pass2 = findViewById(R.id.prof_pass2);
        perName = findViewById(R.id.prof_name);
        mail = findViewById(R.id.prof_email);
        phone = findViewById(R.id.prof_phone);
        eaddress = findViewById(R.id.prof_address);
        del = findViewById(R.id.prof_del);

        if(!type.equals("manage")){
            del.setVisibility(View.GONE);
        }
        getInfo();
    }

    public void editOnclick(View view) {
        if (view.getId() == R.id.prof_update) {
            password = pass.getText().toString().trim();
            String password2 = pass2.getText().toString().trim();
            name = perName.getText().toString().trim();
            email = mail.getText().toString().trim();
            thePhone = phone.getText().toString().trim();
            Address = eaddress.getText().toString().trim();
            boolean error = false;
            if (password.length() > 0 || password2.length() > 0) {
                if (password.length() < 8) {
                    error = true;
                    Toast.makeText(this, "Password should have more than 7 characters", Toast.LENGTH_LONG).show();
                } else if (!password.equals(password2)) {
                    error = true;

                    Toast.makeText(this, getResources().getString(R.string.enterPass2), Toast.LENGTH_LONG).show();
                } else if (!isValidPassword(password)) {
                    error = true;

                    Toast.makeText(this, "The password must contain letters and numbers", Toast.LENGTH_LONG).show();
                }
            } else if (name.length() < 2) {
                error = true;

                Toast.makeText(this, "Enter valid name", Toast.LENGTH_LONG).show();
            } else if (Address.length() < 2) {
                error = true;

                Toast.makeText(this, "Enter valid address", Toast.LENGTH_LONG).show();
            } else if (!isValidEmail(email)) {
                error = true;

                Toast.makeText(this, getResources().getString(R.string.enterEmail), Toast.LENGTH_LONG).show();
            } else if (thePhone.length() != 10 || !thePhone.startsWith("05")) {
                error = true;

                Toast.makeText(this, getResources().getString(R.string.enterPhone), Toast.LENGTH_LONG).show();
            }
            if (!error) {
                Update();
            }

        }else if (view.getId() == R.id.prof_del) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);
            alert.setTitle("Deleting a user");
            alert.setMessage("Are You sure?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   DelUser();
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alert.create().show();
        }
    }

    private void Update() {
        class RegAsync extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("password", params[0]);
                data.put("email", params[1]);
                data.put("name", params[2]);
                data.put("phone", params[3]);
                data.put("address", Address);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        if (password.length() > 0)
            la.execute(password, email, name, thePhone);
        else
            la.execute("no", email, name, thePhone);

    }
    private void DelUser() {
        class RegAsync extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);

                String result = con.sendPostRequest(URL3, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }

    private void getInfo() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Profile.this, getResources().getString(R.string.wait), "Connecting.....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", username);
                String result = con.sendPostRequest(URL2, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No Results", Toast.LENGTH_LONG).show();
                } else{
                    try {
                        JSONArray allInfo = new JSONArray(result);
                        for (int i = 0; i < allInfo.length(); i++) {
                            JSONObject row = allInfo.getJSONObject(i);

                            name=row.getString("name");
                            email=row.getString("email");
                            thePhone=row.getString("phone");
                            Address=row.getString("address");
                            perName.setText(name);
                            mail.setText(email);
                            phone.setText(thePhone);
                            eaddress.setText(Address);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Async la = new Async();
        la.execute();
    }

    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isValidPassword(String s) {
        String n = ".*[0-9].*";
        String a = ".*[A-Z].*";
        String sa = ".*[a-z].*";
        return (s.matches(n) && s.matches(a)) || (s.matches(n) && s.matches(sa));
    }

}
