package com.example.devendra.sihapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class MainPage extends AppCompatActivity {

    private ImageButton btnFarmer , btnModerator , btnScientist;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        btnFarmer = (ImageButton) findViewById(R.id.btnFarmer);
        btnModerator = (ImageButton)findViewById(R.id.btnModerator);
        btnScientist = (ImageButton) findViewById(R.id.btnScientist);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!= null)
        {
            Intent i = new Intent(MainPage.this,MainActivity.class);
            i.putExtra("type","");
            startActivity(i);
            finish();
        }

        btnFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainPage.this,MainActivity.class);
                i.putExtra("type","Farmer");
                startActivity(i);
            }
        });

        btnModerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainPage.this,MainActivity.class);
                i.putExtra("type","Moderator");
                startActivity(i);
            }
        });


        btnScientist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainPage.this,MainActivity.class);
                i.putExtra("type","Scientist");
                startActivity(i);
            }
        });
    }
}
