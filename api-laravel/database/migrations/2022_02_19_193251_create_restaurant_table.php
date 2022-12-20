<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('restaurant', function (Blueprint $table) {
            $table->id();
            $table->string('name');
            $table->string('description');
            $table->float('grade');
            $table->string('localization');
            $table->string('phone_number');
            $table->string('website');
            $table->string('hours');
            $table->timestamps();
        });
        Schema::table('restaurant', function (Blueprint $table) {
            $table->softDeletes();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('restaurant');
        Schema::table('restaurant', function (Blueprint $table) {
            $table->dropSoftDeletes();
        });
    }
};
