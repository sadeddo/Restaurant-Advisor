package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuPageActivity extends AppCompatActivity {
    private TextView textMenu;
    private TextView textRestaurant;
    private TextView textDescription;
    private TextView textPrice;
    private FloatingActionButton floatingActionButton;
    private static final String TAG = "MenuPageActivity";
    private static RestaurantAPI restaurantAPI;
    private Retrofit retrofit;
    private SharedPreferences preferences;
    private static Menus menuId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        this.floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        this.textDescription = (TextView) findViewById(R.id.textDescription);
        this.textMenu = (TextView) findViewById(R.id.textMenu);
        this.textPrice = (TextView) findViewById(R.id.textPrice);
        this.textRestaurant = (TextView) findViewById(R.id.textRestaurant);

        // Get Intent and set Restaurant
        String id = String.valueOf(getIntent().getIntExtra("id", 0));
        String restId = getIntent().getStringExtra("restId");
        String title = getIntent().getStringExtra("restaurant");
        textRestaurant.setText(title);
        configureRetrofit();
        getMenu(id);

        //
        // *************************
        // Menu edition if connected
        // *************************

        this.preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (preferences.contains("userLogged")) {
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton.setVisibility(View.GONE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent (getApplicationContext(), MenuEditActivity.class);
                intentEdit.putExtra("id",menuId.getIdMenu());
                intentEdit.putExtra("restId", restId);
                Log.e(TAG, "id menuPage : " + restId);
                intentEdit.putExtra("title",title);
                intentEdit.putExtra("description",menuId.getDescriptionMenu());
                intentEdit.putExtra("name",menuId.getNameMenu());
                intentEdit.putExtra("price",menuId.getPriceMenu());
                startActivity(intentEdit);
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

    private void getMenu(String id) {
        restaurantAPI.getMenuById(id).enqueue(new Callback<Menus>() {
            @Override
            public void onResponse(Call<Menus> call, Response<Menus> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse getMenu : " + response.body().toString());
                    menuId = response.body();
                    // Set Text MenuPage
                    textMenu.setText(menuId.getNameMenu());
                    textDescription.setText(menuId.getDescriptionMenu());
                    textPrice.setText(String.valueOf(menuId.getPriceMenu()));
                } else {
                    Log.d(TAG, "onResponse menu is empty: " + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Menus> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() + "\n" + id);
            }
        });
    }
}