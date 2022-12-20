<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;
use App\Http\Controllers\RestaurantController;
use App\Http\Controllers\MenuController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

//User
Route::post('register', [App\Http\Controllers\UserController::class, 'Register']);
Route::post('auth', [App\Http\Controllers\UserController::class, 'Authentification']);
Route::get('users', [App\Http\Controllers\UserController::class, 'getUsers']);


//Restaurant
Route::get('restaurants', [App\Http\Controllers\RestaurantController::class, 'getRestos']);
Route::get('restaurant/{id}', [App\Http\Controllers\RestaurantController::class, 'getResto']);
Route::post('restaurant', [App\Http\Controllers\RestaurantController::class, 'addResto']);
Route::put('restaurant/{id}', [App\Http\Controllers\RestaurantController::class, 'putResto']);
Route::delete('restaurant/{id}', [App\Http\Controllers\RestaurantController::class, 'suppResto']);


//Menu
Route::get('menus', [App\Http\Controllers\MenuController::class, 'getMenus']);
Route::get('menu/{id}', [App\Http\Controllers\MenuController::class, 'getMenu']);
Route::get('restaurant/{id}/menus', [App\Http\Controllers\MenuController::class, 'getRestoMenus']);
Route::post('restaurant/{id}/menu', [App\Http\Controllers\MenuController::class, 'addMenu']);
Route::put('restaurant/{idResto}/menu/{id}', [App\Http\Controllers\MenuController::class, 'putMenu']);
Route::delete('restaurant/{idResto}/menu/{id}', [App\Http\Controllers\MenuController::class, 'suppMenu']);