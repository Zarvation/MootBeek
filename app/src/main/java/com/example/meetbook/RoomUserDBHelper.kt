package com.example.meetbook

import androidx.room.Database
import androidx.room.RoomDatabase

// Buat Helper untuk membuat Database
// Masukkan entitas/tabel untuk database dan versi dbnya
@Database(entities = arrayOf(User::class), version = 1)
// ubah menjadi abstract class dan implementasikan RoomDatabase()
abstract class RoomUserDBHelper : RoomDatabase(){
    // Buat fungsi abstract yang mengirimkan userDao()
    abstract fun userDao() : UserDAO
}