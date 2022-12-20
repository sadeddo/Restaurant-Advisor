package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private List<Users> users;
    private Button buttonRegister;
    private EditText editUsername;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editFirstname;
    private EditText editName;
    private TextView textError;
    private EditText editAge;
    private ProgressBar progressBar;
    private static RestaurantAPI restaurantAPI;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.buttonRegister = (Button) findViewById(R.id.buttonRegister);
        this.editUsername = (EditText) findViewById(R.id.editUsername);
        this.editEmail = (EditText) findViewById(R.id.editEmail);
        this.editPassword = (EditText) findViewById(R.id.editPassword);
        this.editFirstname = (EditText) findViewById(R.id.editFirstname);
        this.editName = (EditText) findViewById(R.id.editName);
        this.editAge = (EditText) findViewById(R.id.editAge);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.textError = (TextView) findViewById(R.id.textError);

        configureRetrofit();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String username = editUsername.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String firstname = editFirstname.getText().toString().trim();
                String name = editName.getText().toString().trim();
                Integer age = Integer.parseInt(editAge.getText().toString().trim());

                Users user = new Users();
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(password);
                user.setNameUser(name);
                user.setFirstname(firstname);
                user.setAge(age);
                postUser(user);

            }
        });
    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(s)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .show();

    }

    private void postUser(Users user) {
        restaurantAPI.postUser(user).enqueue(new Callback<Users>(){
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.body() != null) {

                    Log.d(TAG, "onResponse: " + response.body().toString());
                    SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("userLogged", true);
                    editor.commit();

                    Intent intentLogged = new Intent (getApplicationContext(), MainActivity.class);
                    startActivity(intentLogged);
                    finish();

                } else {
                    textError.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    Log.d(TAG, "onResponse: user is empty: " + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
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