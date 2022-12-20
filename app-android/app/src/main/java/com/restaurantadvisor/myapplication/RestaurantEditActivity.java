package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantEditActivity extends AppCompatActivity {

    private FloatingActionButton floatingDelete;
    private FloatingActionButton floatingEditSave;
    private static RestaurantAPI restaurantAPI;
    private static final String TAG = "RestaurantEditActivity";
    private Retrofit retrofit;
    private EditText textRestaurant;
    private EditText textDescription;
    private EditText textGrade;
    private EditText textAddress;
    private EditText textPhone;
    private EditText textWebsite;
    private EditText textHours;
    private ProgressBar progressBar;
    private TextView textError;
    private  Restaurant restSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit);

        this.floatingEditSave = (FloatingActionButton) findViewById(R.id.floatingEditSave);
        this.floatingDelete = (FloatingActionButton) findViewById(R.id.floatingDelete);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.textError = (TextView) findViewById(R.id.textError);
        this.configureRetrofit();

        // Get data from Main Activity

        textRestaurant = (EditText) findViewById(R.id.textRestaurant);
        textDescription = (EditText) findViewById(R.id.textDescription);
        textGrade = (EditText) findViewById(R.id.textGrade);
        textAddress = (EditText) findViewById(R.id.textAddress);
        textPhone = (EditText) findViewById(R.id.textPhone);
        textWebsite = (EditText) findViewById(R.id.textWebsite);
        textHours = (EditText) findViewById(R.id.textHours);

        String id = getIntent().getStringExtra("id");
        getRestaurantId(id);

        //***********************************************************
        //Save edit Restaurant

        floatingEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogged = new Intent (getApplicationContext(), RestaurantPageActivity.class);
                progressBar.setVisibility(View.VISIBLE);

                String name = textRestaurant.getText().toString().trim();
                String description = textDescription.getText().toString().trim();
                String website = textWebsite.getText().toString().trim();
                String address = textAddress.getText().toString().trim();
                String phone = textPhone.getText().toString().trim();
                String hours = textHours.getText().toString().trim();
                Float grade = Float.parseFloat(textGrade.getText().toString().trim());

                Restaurant restaurant = new Restaurant();
                restaurant.setName(name);
                restaurant.setDescription(description);
                restaurant.setWebsite(website);
                restaurant.setLocalization(address);
                restaurant.setPhone_Number(phone);
                restaurant.setHours(hours);
                restaurant.setGrade(grade);

                intentLogged.putExtra("id", Integer.valueOf(id));

                editRestaurant(restaurant, id, intentLogged);
            }
        });
        //***********************************************************
        //Delete Restaurant
        floatingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                deleteRestaurant(id);
            }
        });
    }

    private void deleteRestaurant(String id) {
        restaurantAPI.deleteRestaurant(id).enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Intent intentMain = new Intent (getApplicationContext(), MainActivity.class);
                    startActivity(intentMain);
                    finish();

                } else {
                    Log.d(TAG, "onResponse: error: ");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }

    private void configureRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restaurantAPI = retrofit.create(RestaurantAPI.class);
    }

    private void editRestaurant(Restaurant restaurant, String id, Intent intentLogged) {
        restaurantAPI.putRestaurant(restaurant, id).enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: " + response.body());
                    startActivity(intentLogged);
                    finish();

                } else {
                    Log.d(TAG, "onResponse: user is empty: " + response.body() + "/n" + restaurant.getName());
                    progressBar.setVisibility(View.INVISIBLE);
                    textError.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }
    private void getRestaurantId(String id) {
        restaurantAPI.getRestaurantById(id).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse getMenu : " + response.body().toString());
                    restSet = response.body();
                    // Set Text MenuPage
                    textRestaurant.setText(restSet.getName());
                    textDescription.setText(restSet.getDescription());
                    textGrade.setText(String.valueOf(restSet.getGrade()));
                    textAddress.setText(restSet.getLocalization());
                    textPhone.setText(restSet.getPhone_Number());
                    textWebsite.setText(restSet.getWebsite());
                    textHours.setText(restSet.getHours());
                } else {
                    Log.d(TAG, "onResponse menu is empty: " + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() + "\n" + id);
            }
        });
    }
}