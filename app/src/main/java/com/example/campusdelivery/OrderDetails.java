package com.example.campusdelivery;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusdelivery.classes.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class OrderDetails extends AppCompatActivity {
    Order Info;
    TextView ttitle,tstate, ttime, tprice,tresp, tquantity, tcustomer, tprovider, taddress, ttotal, tdate,or_d_date,or_d_time;
    Button accept, reject, cancel, deliver_btn, save_accept, save_reject, map, bdate, btime;
    Spinner guys;
    EditText res;
    String username, type, lat, lon,d_date,d_time,deliver_guy,response;
    String URL5 = Server.ip + "send_cancel.php";
    String URL4 = Server.ip + "send_deliver.php";
    String URL3 = Server.ip + "send_accept.php";
    String URL2 = Server.ip + "send_reject.php";
    String URL = Server.ip + "getdelivery.php";
    LinearLayout accept_lin, reject_lin,del_lin;
    ArrayList<Info> infos = new ArrayList<>();
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        Info = (Order) getIntent().getSerializableExtra("item");

        ttitle = findViewById(R.id.prod_title);
        tstate = findViewById(R.id.or_status);
        ttime = findViewById(R.id.or_deliver_time);
        tprice = findViewById(R.id.or_deliver_date);
        tquantity = findViewById(R.id.or_quant);
        tcustomer = findViewById(R.id.or_customer);
        tprovider = findViewById(R.id.or_prov);
        taddress = findViewById(R.id.or_address);
        ttotal = findViewById(R.id.or_total);
        tresp = findViewById(R.id.or_resp);
        accept = findViewById(R.id.or_accept);
        reject = findViewById(R.id.or_reject);
        cancel = findViewById(R.id.or_cancel);
        deliver_btn = findViewById(R.id.or_deliverded);
        save_accept = findViewById(R.id.or_save_accept);
        save_reject = findViewById(R.id.or_save_reject);
        map = findViewById(R.id.or_map);
        bdate = findViewById(R.id.or_deliver_date);
        btime = findViewById(R.id.or_deliver_time);
        guys = findViewById(R.id.or_deliver_guy);
        res = findViewById(R.id.response);
        accept_lin = findViewById(R.id.accept_lin);
        reject_lin = findViewById(R.id.reject_lin);
        del_lin = findViewById(R.id.del_time);
        tdate = findViewById(R.id.or_date);
        or_d_date = findViewById(R.id.or_d_date);
        or_d_time = findViewById(R.id.or_d_time);

        ttitle.setText(Info.getTitle());
        tstate.setText(Info.getState());
        tdate.setText(Info.getAdd_date());
        tquantity.setText(Info.getQuantity());
        tprice.setText(Info.getPrice());
        tcustomer.setText(Info.getCustomer_name());
        tprovider.setText(Info.getProvider_name());
        taddress.setText(Info.getAddress());
        ttotal.setText(Info.getTotal());
        lat = Info.getLat();
        lon = Info.getLon();

        if(type.equals("provider") && Info.getState().equalsIgnoreCase("Waiting...")){
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
        }
        if(type.equals("delivery guy")){
            deliver_btn.setVisibility(View.VISIBLE);
        }
        if(type.equals("customer") && Info.getState().equalsIgnoreCase("Waiting...")){
            cancel.setVisibility(View.VISIBLE);

        }
        if(Info.getState().equalsIgnoreCase("accept") || Info.getState().equalsIgnoreCase("delivered")){
            del_lin.setVisibility(View.VISIBLE);
            or_d_date.setText(Info.getDeliver_date());
            or_d_time.setText(Info.getDeliver_time());
        }
        if(Info.getState().equalsIgnoreCase("reject")){
            tresp.setVisibility(View.VISIBLE);
            tresp.setText(Info.getResponse());
        }
        bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(bdate);
            }
        });
        btime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OrderDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // do something with the selected time
                        btime.setText(selectedHour+":" +selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMap(Info.getLat(),Info.getLon());
            }
        });
        save_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response=res.getText().toString().trim();
                if(response.length()<5){
                    Toast.makeText(getApplicationContext(), "Enter the rejection reason", Toast.LENGTH_LONG).show();
                }else{
                    SendReject();
                }
            }
        });
        save_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_date=bdate.getText().toString().trim();
                d_time=btime.getText().toString().trim();
                deliver_guy=infos.get(guys.getSelectedItemPosition()).getUsername();
                if(d_date.equalsIgnoreCase("date") || d_time.equalsIgnoreCase("time")){
                    Toast.makeText(getApplicationContext(), "Select Date and Time", Toast.LENGTH_LONG).show();
                }else{
                    SendAccept();
                }

            }
        });
        deliver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(OrderDetails.this);
                alert.setTitle("Delivering Order");
                alert.setMessage("Did you deliver the order?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SendDeliver();

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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(OrderDetails.this);
                alert.setTitle("Canceling the Order");
                alert.setMessage("Are You sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SendCancel();
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
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reject_lin.setVisibility(View.VISIBLE);
                accept_lin.setVisibility(View.GONE);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reject_lin.setVisibility(View.GONE);
                accept_lin.setVisibility(View.VISIBLE);
            }
        });

        getInfos();
    }

    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(OrderDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", "username");

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                infos.clear();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                } else {
                    try {
                        infos = new ArrayList<>();
                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {

                            JSONObject row = allReq.getJSONObject(i);
                            Info temp = new Info();
                            String username = row.getString("username");
                            String fullname = row.getString("fullname");

                            temp.setUsername(username);
                            temp.setFullname(fullname);


                            infos.add(temp);
                        }
                        String[] array = new String[infos.size()];
                        for (int i = 0; i < infos.size(); i++) {
                            array[i] = infos.get(i).getFullname();
                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(OrderDetails.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, array); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        guys.setAdapter(spinnerArrayAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Async la = new Async();
        la.execute();
    }

    public void setDate(Button vdate) {
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                vdate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(OrderDetails.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void OpenMap(String lat,String lon){

        double latitude = Double.parseDouble(lat); // The latitude value
        double longitude = Double.parseDouble(lon); // The longitude value
        String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
   /* // Create a Uri object with the coordinates and marker
    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Customer Location)");

    // Create an Intent with the action and Uri
    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

    // Set the package to the Google Maps app
    mapIntent.setPackage("com.google.android.apps.maps");

    // Verify if there is an app available to handle the Intent
    if (mapIntent.resolveActivity(getPackageManager()) != null) {
        // Start the activity
        startActivity(mapIntent);
    }*/
    }

    private void SendAccept() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(OrderDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", Info.getOrder_id());
                data.put("state", "accept");
                data.put("date", d_date);
                data.put("time", d_time);
                data.put("agent", deliver_guy);
                String result = con.sendPostRequest(URL3, data);
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
        la.execute();
    }
    private void SendReject() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(OrderDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", Info.getOrder_id());
                data.put("state", "reject");
                data.put("response", response);
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
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }
    private void SendDeliver() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(OrderDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", Info.getOrder_id());
                data.put("state", "delivered");
                String result = con.sendPostRequest(URL4, data);
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
        la.execute();
    }
    private void SendCancel() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(OrderDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", Info.getOrder_id());
                data.put("state", "cancel");
                String result = con.sendPostRequest(URL5, data);
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
        la.execute();
    }

}