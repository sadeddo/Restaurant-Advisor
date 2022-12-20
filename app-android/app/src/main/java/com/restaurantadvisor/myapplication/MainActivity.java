package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    private List<Restaurant> restaurants;
    private ListView ListView;
    private Retrofit retrofit;
    private static RestaurantAPI restaurantAPI;
    private ListViewAdaptater ListViewAdaptater;
    private static final String TAG = "MainActivity";
    private FloatingActionButton buttonAddRestaurant;
    private SharedPreferences preferences;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.ListView = (ListView) findViewById(R.id.ListView);
        this.buttonAddRestaurant = (FloatingActionButton) findViewById(R.id.buttonAddRestaurant);
        this.buttonLogout = (Button) findViewById(R.id.buttonLogout);

        restaurants = new ArrayList<>();

        this.ListViewAdaptater = new ListViewAdaptater(getApplicationContext(), restaurants);
        this.ListView.setAdapter(ListViewAdaptater);

        this.configureRetrofit();
        getRestaurantsViaAPI();
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = restaurants.get(position);
                Intent intentRest = new Intent(getApplicationContext(), RestaurantPageActivity.class);
                intentRest.putExtra("id",restaurant.getId());
                intentRest.putExtra("title",restaurant.getName());
                intentRest.putExtra("description",restaurant.getDescription());
                intentRest.putExtra("grade",restaurant.getGrade());
                intentRest.putExtra("address",restaurant.getLocalization());
                intentRest.putExtra("phone",restaurant.getPhone_Number());
                intentRest.putExtra("website",restaurant.getWebsite());
                intentRest.putExtra("hours",restaurant.getHours());
                startActivity(intentRest);
            }
        });
        this.preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (preferences.contains("userLogged")) {
            buttonAddRestaurant.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.VISIBLE);
        } else {
            buttonAddRestaurant.setVisibility(View.GONE);
        }
        buttonAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent (getApplicationContext(), AddRestaurantActivity.class);
                startActivity(intentAdd);
            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("userLogged");
                editor.commit();
                Intent intent = new Intent (getApplicationContext(), LaunchActivity.class);
                startActivity(intent);
                finish();
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

    private void getRestaurantsViaAPI() {
        restaurantAPI.getRestaurant().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                Log.d(TAG,"onResponse:");

                List<Restaurant> restaurantList = response.body();
                if (restaurantList != null) {
                    for(Restaurant restaurant: restaurantList){
                        restaurants.add(restaurant);
                    }
                    ListViewAdaptater.notifyDataSetChanged();
                } else {
                    Log.d(TAG, " restaurants is empty: " + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}