package com.example.meetbook

import android.content.Context
import android.content.SharedPreferences

class AdsPrefHelper (context: Context, filename: String) {
    val WATCH_ID = "WATCHID"
    private var roomPreferences : SharedPreferences
    init {
        roomPreferences = context.getSharedPreferences(filename,Context.MODE_PRIVATE)
    }

    // Buat fungsi get() dan set() untuk setiap infomasi yang akan dimasukkan
    var watchTimes : Int
        // mengambil data string name dengan key WATCH_ID dan default value kosong
        get() = roomPreferences.getInt(WATCH_ID,5)
        // Set data string name dengan value yang dimasukkan
        set(value){
            // edit() untuk memanipulasi data string watch_times dengan key WATCH_ID dan .apply() untuk menyimpan data
            roomPreferences.edit().putInt(WATCH_ID,value).apply()
        }
    // Buat fungsi clearValues untuk menghapus isi file
    fun clearValues(){
        // Gunakan .edit() untuk memanipulasi file, .clear() untuk menghapus dan .apply() untuk menyimpan
        roomPreferences.edit().clear().apply()
    }
}