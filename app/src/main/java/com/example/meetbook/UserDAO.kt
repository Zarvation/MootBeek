package com.example.meetbook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Query("Select * From User")
    fun getAllData() : List<User>

    @Query("Select * From User WHERE _id = :id")
    fun getUser(id: Int) : List<User>

    @Insert
    fun insertAll(vararg user: User)

    @Query("UPDATE User SET COLUMN_USERNAME = :value1, COLUMN_PASSWORD = :value2 WHERE _id = :id")
    fun updateDB(id: Int, value1: String, value2: String)

    @Query("DELETE FROM User WHERE COLUMN_USERNAME = :value1")
    fun deleteUser(value1: String)

    @Query("Select COLUMN_PASSWORD From User WHERE COLUMN_USERNAME = :value1")
    fun getPassByName(value1: String) : String
}