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