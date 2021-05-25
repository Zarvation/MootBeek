package com.example.meetbook

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey val _id : Int,
    @ColumnInfo(name = "COLUMN_USERNAME") var username : String,
    @ColumnInfo(name = "COLUMN_PASSWORD") var password : String)