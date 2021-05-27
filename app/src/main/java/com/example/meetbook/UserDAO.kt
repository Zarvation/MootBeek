package com.example.meetbook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Buat DAO dengan interface
@Dao // Tambahkan @Dao untuk menuliskan query ke database
interface UserDAO {
    // Query ditulis :

    // Query untuk membaca semua data dari table User
    @Query("Select * From User")
    fun getAllData() : List<User> // Kembalikan List berisi User yang telah dibaca

    // Query untuk membaca semua data user dari User dimana idnya sesuai dengan yang ditentukan
    @Query("Select * From User WHERE _id = :id")
    // Masukkan parameter id yang akan digunakan sebagai statement untuk query diatas
    // Kembalikan list berisi User yang telah dibaca
    fun getUser(id: Int) : List<User>

    // Query untuk insert data ke User
    @Insert
    // gunakan vararg bertipe User agar bisa menampung lebih dari 1 User (dalam bentuk array)
    fun insertAll(vararg user: User)

    // Query untuk meng-Update data User (Username dan Password) dimana id sesuai yang ditentukan
    @Query("UPDATE User SET COLUMN_USERNAME = :value1, COLUMN_PASSWORD = :value2 WHERE _id = :id")
    // Masukkan id untuk statement, username dan password untuk diupdate sebagai parameter
    fun updateDB(id: Int, value1: String, value2: String)

    // Query untuk menghapus data User sesuai Username yang diberikan
    @Query("DELETE FROM User WHERE COLUMN_USERNAME = :value1")
    // Masukkan username sebagai parameter untuk statement yang akan digunakan pada query
    fun deleteUser(value1: String)

    // Query untuk membaca password sesuai username yang diberikan
    @Query("Select COLUMN_PASSWORD From User WHERE COLUMN_USERNAME = :value1")
    fun getPassByName(value1: String) : String
}