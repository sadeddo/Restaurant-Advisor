package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddRestaurantActivity extends AppCompatActivity {
    private static RestaurantAPI restaurantAPI;
    private static final String TAG = "AddRestaurantActivity";
    private Button buttonAdd;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextGrade;
    private EditText editTextAddress;
    private EditText editTextPhone_Number;
    private EditText editTextWebsite;
    private EditText editTextHours;
    private ProgressBar progressBar;
    private TextView textError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        this.buttonAdd = (Button) findViewById(R.id.buttonAdd);
        this.editTextName = (EditText) findViewById(R.id.editTextName);
        this.editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        this.editTextGrade = (EditText) findViewById(R.id.editTextGrade);
        this.editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        this.editTextPhone_Number = (EditText) findViewById(R.id.editTextPhone_Number);
        this.editTextWebsite = (EditText) findViewById(R.id.editTextWebsite);
        this.editTextHours = (EditText) findViewById(R.id.editTextHours);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.textError = (TextView) findViewById(R.id.textError);

        configureRetrofit();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = editTextName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String website = editTextWebsite.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String phone = editTextPhone_Number.getText().toString().trim();
                String hours = editTextHours.getText().toString().trim();
                Float grade = Float.parseFloat(editTextGrade.getText().toString().trim());

                Restaurant restaurant = new Restaurant();
                restaurant.setName(name);
                restaurant.setDescription(description);
                restaurant.setWebsite(website);
                restaurant.setLocalization(address);
                restaurant.setPhone_Number(phone);
                restaurant.setHours(hours);
                restaurant.setGrade(grade);
                addRestaurant(restaurant);

            }
        });

    }

    private void addRestaurant(Restaurant restaurant) {
        restaurantAPI.postRestaurant(restaurant).enqueue(new Callback<Restaurant>(){
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().toString());

                    Intent intentLogged = new Intent (getApplicationContext(), MainActivity.class);
                    startActivity(intentLogged);
                    finish();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    textError.setVisibility(View.VISIBLE);

                    Log.d(TAG, "onResponse: user is empty: " + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
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