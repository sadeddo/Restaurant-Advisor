package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MenuEditActivity extends AppCompatActivity {
    private FloatingActionButton floatingDelete;
    private FloatingActionButton floatingSave;
    private ProgressBar progressBarMenu;
    private TextView textError;
    private static RestaurantAPI restaurantAPI;
    private static final String TAG = "MenuEditActivity";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);

        configureRetrofit();
        EditText editMenu = (EditText) findViewById(R.id.editMenu);
        EditText editDescription = (EditText) findViewById(R.id.editDescription);
        EditText editPrice = (EditText) findViewById(R.id.editPrice);
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        this.floatingSave = (FloatingActionButton) findViewById(R.id.floatingSave);
        this.floatingDelete = (FloatingActionButton) findViewById(R.id.floatingDelete);
        this.progressBarMenu = (ProgressBar) findViewById(R.id.progressBarMenu);
        this.textError = (TextView) findViewById(R.id.textError);

        String id = String.valueOf(getIntent().getIntExtra("id", 0));
        String title = getIntent().getStringExtra("title");
        String restId = getIntent().getStringExtra("restId");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String price = String.valueOf(getIntent().getFloatExtra("price",0));
        Log.e(TAG, "id mtn: " + restId);
        textView5.setText(title);
        editMenu.setText(name);
        editDescription.setText(description);
        editPrice.setText(price);

        // ***********
        // Change Menu
        // ***********
        floatingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MenuPageActivity.class);
                progressBarMenu.setVisibility(View.VISIBLE);

                String menu = editMenu.getText().toString().trim();
                String description = editDescription.getText().toString().trim();
                Float price = Float.parseFloat(editPrice.getText().toString().trim());

                Menus menuSet = new Menus();
                menuSet.setNameMenu(menu);
                menuSet.setDescriptionMenu(description);
                menuSet.setPriceMenu(price);
                intent.putExtra("id", Integer.valueOf(id));
                intent.putExtra("restaurant",title);
                changeMenu(menuSet,id,intent);
            }
        });

        //*******************************
        // Delete Menu
        // ******************************
        floatingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarMenu.setVisibility(View.VISIBLE);
                deleteMenu(id, restId);
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

    private void changeMenu(Menus menus, String id, Intent intent) {
        restaurantAPI.putMenus(menus, id).enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: " + response.message());
                    startActivity(intent);
                    finish();

                } else {
                    Log.d(TAG, "onResponse: " + response.body());
                    progressBarMenu.setVisibility(View.INVISIBLE);
                    textError.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }

    private void deleteMenu(String id, String restId) {
        restaurantAPI.deleteMenu(id).enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse delete: " + response.message());
                    Intent intentMain = new Intent (getApplicationContext(), RestaurantPageActivity.class);
                    intentMain.putExtra("id", Integer.valueOf(restId));
                    Log.e(TAG, "id: " + restId);
                    startActivity(intentMain);
                    finish();

                } else {
                    Log.d(TAG, "onResponse: error: ");
                    progressBarMenu.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }
}