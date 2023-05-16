package com.example.campusdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Provider extends AppCompatActivity {
    String id;
    Button content,orders,support,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        content=findViewById(R.id.content);
        orders=findViewById(R.id.orders);
        support=findViewById(R.id.the_support);
        profile=findViewById(R.id.the_profile);

        id=getIntent().getStringExtra("id");

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Provider.this,ContentList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","provider");
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