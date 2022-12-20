<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\User;

class CreateUserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        User::create([
            'username' => 'Fullmetal',
            'firstname' => 'Edward',
            'name' => 'Elric',
            'age' => '19',
            'email' => 'edward@gmail.com',
            'password' => 'toto',
        ]);

        User::create([
            'username' => 'Nah',
            'firstname' => 'Nahida',
            'name' => 'HAMHAMI',
            'age' => '20',
            'email' => 'nah@gmail.com',
            'password' => 'titi',
        ]);
        User::create([
            'username' => 'Oum',
            'firstname' => 'Oumayma',
            'name' => 'SADDEDINE',
            'age' => '20',
            'email' => 'oum@gmail.com',
            'password' => 'tata',
        ]);
    }
}
