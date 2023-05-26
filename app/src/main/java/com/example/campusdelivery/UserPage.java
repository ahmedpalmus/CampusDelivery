package com.example.campusdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserPage extends AppCompatActivity {
    String id;
    Button food,books,meds,lectures,orders,support,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        food=findViewById(R.id.food);
        books=findViewById(R.id.books);
        meds=findViewById(R.id.meds);
        lectures=findViewById(R.id.lectures);
        orders=findViewById(R.id.the_orders);
        support=findViewById(R.id.the_support);
        profile=findViewById(R.id.the_profile);

        id=getIntent().getStringExtra("id");

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserPage.this,ContentList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","user");
                intent.putExtra("field","Restaurant");
                startActivity(intent);
            }
        });
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserPage.this,ContentList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","user");
                intent.putExtra("field","Library");
                startActivity(intent);
            }
        });
        meds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserPage.this,ContentList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","user");
                intent.putExtra("field","Clinic");
                startActivity(intent);
            }
        });
        lectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserPage.this,SupportList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","user");
                intent.putExtra("support_type","student affairs");
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserPage.this,Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserPage.this,SupportList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","user");
                intent.putExtra("support_type","comp");
                startActivity(intent);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserPage.this,OrdersList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","customer");
                startActivity(intent);
            }
        });
    }
}