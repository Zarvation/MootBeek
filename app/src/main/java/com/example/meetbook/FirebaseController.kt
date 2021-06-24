package com.example.meetbook

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseController (context: Context) {
    // Inisialisasi database firebase
    private var database = Firebase.database
    // Buat reference untuk membuat atau mengambil table Users
    private val ref = database.getReference("USERS")
    // Panggil context
    private val mContext = context
    // Buat mutableMap untuk user yang berisi key dan username
    private var userData = mutableMapOf<String,String>()
    // Buat sebuah mutablelist untuk mengisi data user
    private var userList : MutableList<Users> = mutableListOf()

    // Buat fungsi insertUser untuk menambah user ke database
    fun insertUser(users: Users){
        // buat Id untuk user dengan key yang digenerate dari firebase
        var usersID = ref.push().key.toString()

        // Masukkan user dengan key ke dalam database
        ref.child(usersID).setValue(users).apply{
            // Muncul Toast bila user berhasil ditambahkan atau galga ditambahkan
            addOnCompleteListener {
                Toast.makeText(mContext, "User Terdaftar", Toast.LENGTH_SHORT)
            }
            addOnCanceledListener {  }
            addOnFailureListener {
                Toast.makeText(mContext, "User Gagal Didaftar", Toast.LENGTH_SHORT)
            }
            addOnSuccessListener {  }
        }
    }

    // Buat fungsi untuk membaca key dan username
    private fun readUser() : MutableMap<String,String>{
        // gunakan addValueEventListener untuk membaca value didalam database
        ref.addValueEventListener(object : ValueEventListener{
            // Bila terjadi perubahan data
            override fun onDataChange(snapshot: DataSnapshot) {
                // dan bila ada Data yang tersimpan
                if(snapshot!!.exists()){
                    userData.clear()
                    // Tangkap setiap data didalam snapshot
                    for(data in snapshot.children){
                        // Ambil value dari Json diubah kembali menjadi objek user
                        val user = data.getValue(Users::class.java)
                        user.let{
                            // Masukkan key dan username ke dalam userData
                            userData.put(data.key.toString(),it!!.username)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        //Kembalikan userData
        return userData
    }

    // Buat fungsi untuk mengupdate data User dengan mengirimkan parameter berupa username lama, username baru dan pass baru
    fun updateUser(oldUsername: String, newUsername: String, newPassword: String){
        // Baca data dengan menggunakan readUser() dan filter data tersebut untuk username yang memiliki
        // value parameter oldUsername, dan ambil key nya
        val key = readUser().filterValues { it == oldUsername }.keys
        // Ambil key user tersebut
        val userID = key.first()
        val updatedData = Users(newUsername,newPassword)
        // Set value user tersebut dengan data dari parameter yang dikirimkan
        ref.child(userID).setValue(updatedData).apply{
            addOnCompleteListener{
                Toast.makeText(mContext, "User Updated", Toast.LENGTH_SHORT)
            }
            addOnFailureListener {
                Toast.makeText(mContext, "User Update Failed", Toast.LENGTH_SHORT)
            }
        }
    }

    // Buat fungsi getUserList() untuk mengambil list data user
    fun getUserList() : MutableList<Users>{
        // Gunakan kembali addValueEventListener
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){
                    userList.clear()
                    for(data in snapshot.children){
                        val user = data.getValue(Users::class.java)
                        user.let{
                            // Masukkan data user ke dalam list
                            userList.add(Users(it!!.username,it!!.password))
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        // Kembalikan userlist
        return userList
    }

    // Buat fungsi untuk autentikasi user dengan mengembalikan true/false
    fun authUser(srcUsername : String, srcPassword : String) : Boolean{
        // Ambil list user dengan fungsi getUserList()
        val getList = getUserList()
        var state = false
        // Untuk setiap data didalam list
        for (data in getList){
            // Cocokkan apakah username dan password ada dan sama
            if (data.username == srcUsername){
                if (data.password == srcPassword){
                    state = true // Kembalikan state true dan break
                    break
                }
            }
        }
        // True menandakan username terdapat didalam database, dan false sebaliknya
        return state
    }

    // Buat fungsi untuk validasi user apakah user bisa dibuat
    fun validityUser(srcUsername : String) : Boolean{
        // Ambil list user dengan fungsi getUserList()
        val getList = getUserList()
        var state = true
        // Untuk setiap data dalam list
        for (data in getList){
            // Cocokkan apakah ada username yang sama dengan username yang akan dibuat
            if (data.username == srcUsername){
                state = false // kembalikan false bila ada dan break
                break
            }
        }
        // False menandakan bahwa username telah dibuat sebelumnya
        return state
    }
}