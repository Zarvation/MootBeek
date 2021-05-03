package com.example.meetbook

import android.content.Context
import android.content.SharedPreferences

class SharedPrefHelper (context: Context, filename: String) {
    val ROOM_NAME = "ROOMNAME"
    val ROOM_CAP = "ROOMCAPACITY"
    val ROOM_IMG = "ROOMIMAGE"
    val BEGIN_HOUR = "BEGINHOUR"
    val END_HOUR = "ENDHOUR"
    val BEGIN_MINUTE = "BEGINMIN"
    val END_MINUTE = "ENDMIN"

    private var roomPreferences : SharedPreferences
    init {
        roomPreferences = context.getSharedPreferences(filename,Context.MODE_PRIVATE)
    }

    var name : String?
        get() = roomPreferences.getString(ROOM_NAME,"")
        set(value){
            roomPreferences.edit().putString(ROOM_NAME,value).apply()
        }
    var cap : Int
        get() = roomPreferences.getInt(ROOM_CAP,0)
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
    fun clearValues(){
        roomPreferences.edit().clear().apply()
    }
}