package com.example.meetbook

import android.content.Context
import android.content.SharedPreferences

// Buat Shared Preferences Helper untuk Room yang telah di book
class SharedPrefHelper (context: Context, filename: String) {
    // Buat Key untuk informasi yang akan dimasukkan
    val ROOM_ID = "ROOMID"
    val ROOM_NAME = "ROOMNAME"
    val ROOM_CAP = "ROOMCAPACITY"
    val ROOM_IMG = "ROOMIMAGE"
    val BEGIN_HOUR = "BEGINHOUR"
    val END_HOUR = "ENDHOUR"
    val BEGIN_MINUTE = "BEGINMIN"
    val END_MINUTE = "ENDMIN"

    // Inisialisasi dan buat objek SharedPreferences
    private var roomPreferences : SharedPreferences
    init {
        roomPreferences = context.getSharedPreferences(filename,Context.MODE_PRIVATE)
    }

    // Buat fungsi get() dan set() untuk setiap infomasi yang akan dimasukkan
    var Id : Int
        // mengambil data string name dengan key ROOM_NAME dan default value kosong
        get() = roomPreferences.getInt(ROOM_ID,0)
        // Set data string name dengan value yang dimasukkan
        set(value){
            // edit() untuk memanipulasi data string name dengan key ROOM_NAME dan .apply() untuk menyimpan data
            roomPreferences.edit().putInt(ROOM_ID,value).apply()
        }
    var name : String?
        // mengambil data string name dengan key ROOM_NAME dan default value kosong
        get() = roomPreferences.getString(ROOM_NAME,"")
        // Set data string name dengan value yang dimasukkan
        set(value){
            // edit() untuk memanipulasi data string name dengan key ROOM_NAME dan .apply() untuk menyimpan data
            roomPreferences.edit().putString(ROOM_NAME,value).apply()
        }
    var cap : Int
        // mengambil data integer cap dengan key ROOM_CAP dam default value 0
        get() = roomPreferences.getInt(ROOM_CAP,0)
        // edit() untuk memanipulasi data integer cap dengan key ROOM_CAP dan .apply() untuk menyimpan data
        set(value){
            roomPreferences.edit().putInt(ROOM_CAP,value).apply()
        }
    var image : String?
        get() = roomPreferences.getString(ROOM_IMG,"")
        set(value){
            roomPreferences.edit().putString(ROOM_IMG,value).apply()
        }
    var beginHour : Int
        get() = roomPreferences.getInt(BEGIN_HOUR,0)
        set(value){
            roomPreferences.edit().putInt(BEGIN_HOUR,value).apply()
        }
    var endHour : Int
        get() = roomPreferences.getInt(END_HOUR,0)
        set(value){
            roomPreferences.edit().putInt(END_HOUR,value).apply()
        }
    var beginMin : Int
        get() = roomPreferences.getInt(BEGIN_MINUTE,0)
        set(value){
            roomPreferences.edit().putInt(BEGIN_MINUTE,value).apply()
        }
    var endMin : Int
        get() = roomPreferences.getInt(END_MINUTE,0)
        set(value){
            roomPreferences.edit().putInt(END_MINUTE,value).apply()
        }

    // Buat fungsi clearValues untuk menghapus isi file
    fun clearValues(){
        // Gunakan .edit() untuk memanipulasi file, .clear() untuk menghapus dan .apply() untuk menyimpan
        roomPreferences.edit().clear().apply()
    }
}