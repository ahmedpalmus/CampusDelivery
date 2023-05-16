package com.example.campusdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminPage extends AppCompatActivity {
    String id;
    Button manage,support,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        manage=findViewById(R.id.manage_user);
        support=findViewById(R.id.the_support);
        profile=findViewById(R.id.the_profile);

        id=getIntent().getStringExtra("id");

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminPage.this,ProvidersList.class);
                intent.putExtra("id",id);
                intent.putExtra("user_type","admin");
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(AdminPage.this, Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "admin");

                startActivity(intent);*/
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}