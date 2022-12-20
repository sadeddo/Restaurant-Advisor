package com.restaurantadvisor.myapplication;

import android.content.Intent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestaurantAPI {
    @GET("restaurants")
    Call<List<Restaurant>> getRestaurant();

    @GET("users")
    Call<List<Users>> getUsers();

    @GET("restaurant/{id}")
    Call<Restaurant> getRestaurantById(@Path("id") String id);

    @GET("menus")
    Call<Menus> getMenu();

    @GET("menu/{id}")
    Call<Menus> getMenuById(@Path("id") String id);

    @GET("restaurant/{id}/menus")
    Call<List<Menus>> getMenuId(@Path("id") String id);

    @POST("restaurant")
    Call<Restaurant> postRestaurant (@Body Restaurant restaurant);

    @POST("register")
    Call<Users> postUser (@Body Users user);

    @POST("auth")
    Call<Void> postLogin (@Body Users user);

    @POST("restaurant/{id}/menu")
    Call<Menus> postMenu (@Body Menus menu, @Path("id") String id);

    @DELETE("restaurant/{id}")
    Call<Void> deleteRestaurant(@Path("id") String id);

    @DELETE("restaurant/{idResto}/menu/{id}")
    Call<Void> deleteMenu(@Path("id") String id);

    @PUT("restaurant/{id}")
    Call<Void> putRestaurant (@Body Restaurant restaurant, @Path("id") String id);

    @PUT("restaurant/{idResto}/menu/{id}")
    Call<Void> putMenus (@Body Menus menu, @Path("id") String id);

}
