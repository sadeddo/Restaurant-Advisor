package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class RestaurantPageActivity extends AppCompatActivity {
    private List<Menus> menu;
    private ListView ListView;
    private ListViewMenuAdapter ListViewMenuAdapter;
    private static RestaurantAPI restaurantAPI;
    private Retrofit retrofit;
    private SharedPreferences preferences;
    private FloatingActionButton floatingEdit;
    private FloatingActionButton floatingAdd;
    private static final String TAG = "RestaurantPageActivity";
    private String id;
    private  Restaurant restSet;
    private TextView textRestaurant;
    private TextView textDescription;
    private TextView textGrade;
    private TextView textAddress;
    private TextView textPhone;
    private TextView textWebsite;
    private TextView textHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        // Get data from Main Activity

        textRestaurant = (TextView) findViewById(R.id.textRestaurant);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textGrade = (TextView) findViewById(R.id.textGrade);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textPhone = (TextView) findViewById(R.id.textPhone);
        textWebsite = (TextView) findViewById(R.id.textWebsite);
        textHours = (TextView) findViewById(R.id.textHours);

        id = String.valueOf(getIntent().getIntExtra("id", 0));

        //***********************************************************
        // ListView Menu
        this.ListView = (ListView) findViewById(R.id.ListView);
        menu = new ArrayList<>();
        this.floatingEdit = (FloatingActionButton) findViewById(R.id.floatingEdit);
        this.floatingAdd = (FloatingActionButton) findViewById(R.id.floatingAdd);
        this.ListViewMenuAdapter = new ListViewMenuAdapter(getApplicationContext(), menu);
        this.ListView.setAdapter(ListViewMenuAdapter);
        this.configureRetrofit();

        getMenuID(id);
        getRestaurantId(id);
        String restId = id;

        //*******************
        // Go to MenuPage
        //*******************

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menus menus = menu.get(position);
                Intent intentMenuList = new Intent(getApplicationContext(), MenuPageActivity.class);
                intentMenuList.putExtra("id", menus.getIdMenu());
                intentMenuList.putExtra("restId", restId);
                intentMenuList.putExtra("restaurant", textRestaurant.getText().toString().trim());
                startActivity(intentMenuList);

            }
        });
        // *************************
        // Restaurant edition if connected
        // *************************
        this.preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (preferences.contains("userLogged")) {
            floatingEdit.setVisibility(View.VISIBLE);
            floatingAdd.setVisibility(View.VISIBLE);
        } else {
            floatingEdit.setVisibility(View.INVISIBLE);
            floatingAdd.setVisibility(View.INVISIBLE);
        }

        // *******************
        // * Edit Restaurant *
        // *******************
        floatingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentEdit = new Intent (getApplicationContext(), RestaurantEditActivity.class);
                intentEdit.putExtra("id", id);
                intentEdit.putExtra("title",textRestaurant.getText().toString().trim());
                intentEdit.putExtra("description",textDescription.getText().toString().trim());
                intentEdit.putExtra("grade",textGrade.getText().toString().trim());
                intentEdit.putExtra("address",textAddress.getText().toString().trim());
                intentEdit.putExtra("phone",textPhone.getText().toString().trim());
                intentEdit.putExtra("website",textWebsite.getText().toString().trim());
                intentEdit.putExtra("hours",textHours.getText().toString().trim());
                startActivity(intentEdit);
            }
        });

        // ************
        // * Add Menu *
        // ************
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent (getApplicationContext(), MenuAddActivity.class);
                intentAdd.putExtra("id", id);
                intentAdd.putExtra("title",textRestaurant.getText().toString().trim());
                startActivity(intentAdd);
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

    private void getMenuID(String id) {
        restaurantAPI.getMenuId(id).enqueue(new Callback<List<Menus>>() {
            @Override
            public void onResponse(Call<List<Menus>> call, Response<List<Menus>> response) {

                List<Menus> menuList = response.body();
                if (menuList != null) {
                    Log.d(TAG,"onResponse:" + response.body().toString());
                    for(Menus menuApi: menuList){
                        menu.add(menuApi);
                    }
                    ListViewMenuAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse menu is empty: " + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<List<Menus>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() + "\n" + id);
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
                    textGrade.setText(restSet.getGrade() + "/10");
                    textAddress.setText("Address : " + restSet.getLocalization());
                    textPhone.setText("Phone Number : " + restSet.getPhone_Number());
                    textWebsite.setText("Website : " + restSet.getWebsite());
                    textHours.setText("Open hours : " + restSet.getHours());
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