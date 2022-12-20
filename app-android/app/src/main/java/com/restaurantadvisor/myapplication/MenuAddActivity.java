package com.restaurantadvisor.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MenuAddActivity extends AppCompatActivity {
    private static RestaurantAPI restaurantAPI;
    private static final String TAG = "MenuAddActivity";
    private Button button;
    private ProgressBar progressBarMenu;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_add);

        TextView textView5 = (TextView) findViewById(R.id.textView5);
        EditText editMenu = (EditText) findViewById(R.id.editMenu);
        EditText editDescription = (EditText) findViewById(R.id.editDescription);
        EditText editPrice = (EditText) findViewById(R.id.editPrice);
        this.button = (Button) findViewById(R.id.button);
        this.progressBarMenu = (ProgressBar) findViewById(R.id.progressBarMenu);
        this.textError = (TextView) findViewById(R.id.textError);

        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        textView5.setText(title);

        configureRetrofit();

        // ************
        // * Add Menu *
        // ************
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), RestaurantPageActivity.class);
                progressBarMenu.setVisibility(View.VISIBLE);

                String name = editMenu.getText().toString().trim();
                String description = editDescription.getText().toString().trim();
                Float price = Float.parseFloat(editPrice.getText().toString().trim());

                Menus menuSet = new Menus();
                menuSet.setNameMenu(name);
                menuSet.setDescriptionMenu(description);
                menuSet.setPriceMenu(price);
                menuSet.setIdRest(Integer.valueOf(id));

                intent.putExtra("id", Integer.valueOf(id));
                addMenu(menuSet, id, intent);
                finish();

            }
        });

    }

    private void addMenu(Menus menus, String id, Intent intent) {
        restaurantAPI.postMenu(menus,id).enqueue(new Callback<Menus>(){
            @Override
            public void onResponse(Call<Menus> call, Response<Menus> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().toString());

                    startActivity(intent);
                } else {
                    progressBarMenu.setVisibility(View.INVISIBLE);
                    textError.setVisibility(View.VISIBLE);

                    Log.d(TAG, "onResponse: user is empty: " + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Menus> call, Throwable t) {
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