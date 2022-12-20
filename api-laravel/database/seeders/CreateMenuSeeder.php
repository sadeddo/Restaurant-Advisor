<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\Menu;

class CreateMenuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        
        Menu::create([
            'name' => 'Menu A5',
            'description' => '8 sushis, 4 makis, 4 calofornia rolls',
            'price' => '16.5',
        ]);
        Menu::create([
            'name' => 'fish',
            'description' => 'poisson, pain, fromage, sauce, frite, boisson',
            'price' => '8.70',
        ]);
    }
}
