package com.example.meetbook

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class), version = 1)
abstract class RoomUserDBHelper : RoomDatabase(){
    abstract fun userDao() : UserDAO
}