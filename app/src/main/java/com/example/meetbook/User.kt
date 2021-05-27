package com.example.meetbook

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Buat class entity User untuk membuat table User
@Entity
data class User (
    @PrimaryKey val _id : Int, // Deklarasi primary key untuk table User
    @ColumnInfo(name = "COLUMN_USERNAME") var username : String, // Dan field-field nya
    @ColumnInfo(name = "COLUMN_PASSWORD") var password : String)