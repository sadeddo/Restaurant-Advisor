package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    private Button buttonRegister;
    private Button buttonLogin;
    private Button skipLogin;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        this.preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (preferences.contains("userLogged")) {
            Intent intentSkip = new Intent (getApplicationContext(), MainActivity.class);
            startActivity(intentSkip);
            finish();
        }
        this.buttonRegister = (Button) findViewById(R.id.buttonRegister);
        this.buttonLogin = (Button) findViewById(R.id.buttonLogin);
        this.skipLogin = (Button) findViewById(R.id.skipLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent (getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent (getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSkip = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intentSkip);
            }
        });
    }
}