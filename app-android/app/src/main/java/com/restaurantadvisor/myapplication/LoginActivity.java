package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private Button buttonLogin;
    private EditText editPassword;
    private EditText editEmail;
    private TextView textError;
    private static RestaurantAPI restaurantAPI;
    private ProgressBar progressBar;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.buttonLogin = (Button) findViewById(R.id.buttonLogin);
        this.editPassword = (EditText) findViewById(R.id.editPassword);
        this.editEmail = (EditText) findViewById(R.id.editEmail);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.textError = (TextView) findViewById(R.id.textError);
        configureRetrofit();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                Users user = new Users();
                user.setEmail(email);
                user.setPassword(password);
                getLogin(user);


            }
        });

    }

    private void getLogin(Users user) {
        restaurantAPI.postLogin(user).enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: " + response.code());
                    SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("userLogged", true);
                    editor.commit();

                    Intent intentLogged = new Intent (getApplicationContext(), MainActivity.class);
                    startActivity(intentLogged);
                    finish();
                } else {
                    Log.d(TAG, "onResponse: " + response.code() + "\n" + response.body());
                    textError.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage().toString() + "\n");
                t.printStackTrace();
            }
        });
    }

    private void configureRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restaurantAPI = retrofit.create(RestaurantAPI.class);
    }
}