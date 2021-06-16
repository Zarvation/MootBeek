package com.example.meetbook

import android.content.Context
import android.content.SharedPreferences

// Buat Shared Pref untuk menyimpan jumlah ads yang perlu ditonton
class AdsPrefHelper (context: Context, filename: String) {
    val WATCH_ID = "WATCHID"
    private var roomPreferences : SharedPreferences
    init {
        roomPreferences = context.getSharedPreferences(filename,Context.MODE_PRIVATE)
    }

    // Buat fungsi get() dan set() untuk jumlah ads yang perlu ditonton
    var watchTimes : Int
        // mengambil data int watchTimes dengan key WATCH_ID dan default value 5
        // def value 5 menyatakan bahwa jumlah awal ads yang harus ditonton adalah 5 video ads
        get() = roomPreferences.getInt(WATCH_ID,5)
        // Set data int watchTimes dengan value yang dimasukkan
        set(value){
            // edit() untuk memanipulasi data int watch_times dengan key WATCH_ID dan .apply() untuk menyimpan data
            roomPreferences.edit().putInt(WATCH_ID,value).apply()
        }
    // Buat fungsi clearValues untuk menghapus isi file
    fun clearValues(){
        // Gunakan .edit() untuk memanipulasi file, .clear() untuk menghapus dan .apply() untuk menyimpan
        roomPreferences.edit().clear().apply()
    }
}