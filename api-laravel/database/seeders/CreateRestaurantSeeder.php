<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\Restaurant;

class CreateRestaurantSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Restaurant::create([
            'name' => 'MacDonald s',
            'description' => 'Classic, long-running fast-food chain known for its burgers, fries & shakes.',
            'grade' => '3.2',
            'localization' => 'Centre Commercial Grand Ciel, 30 Boulevard Paul Vaillant Couturier, 94200 Ivry-sur-Seine',
            'phone_number' => '01 49 60 62 60',
            'website' => 'macdonalds.fr',
            'hours' => 'Monday-Saturday 9AM–9PM, Sunday Closed',
        ]);
        Restaurant::create([
            'name' => 'Quick',
            'description' => 'Classic, long-running fast-food chain known for its burgers, fries & shakes.',
            'grade' => '4.3',
            'localization' => '122 av Champs Elysées, Paris',
            'phone_number' => '01 53 75 38 49',
            'website' => 'quick.fr',
            'hours' => 'Monday-Sunday 9AM–9PM',
        ]);
    }
}
