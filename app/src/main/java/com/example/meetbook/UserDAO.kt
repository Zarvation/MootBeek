package com.example.meetbook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Query("Select * From User")
    fun getAllData() : List<User>

    @Insert
    fun insertAll(vararg user: User)

    @Query("UPDATE User SET COLUMN_USERNAME = :value1, COLUMN_PASSWORD = :value2 WHERE _id = :id")
    fun updateDB(id: Int, value1: String, value2: String)
}