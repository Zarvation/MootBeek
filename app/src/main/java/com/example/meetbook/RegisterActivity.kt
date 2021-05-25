package com.example.meetbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var db= Room.databaseBuilder(
            this,
            RoomUserDBHelper::class.java,
            "meetbookDb.db"
        ).build()

        RegisButton.setOnClickListener {
            if (RUsername.text.toString().length>0 && RPassword.text.toString().length>0){
                var state = true
                var datuser = RUsername.text.toString()
                var datpass = RPassword.text.toString()
                var hasil = ""
                doAsync {
                    var userList = db.userDao().getAllData()
                    for(allData in userList){
                        if (datuser == allData.username){
                            hasil += "${allData._id} ${allData.username} ${allData.password}\n"
                            state = false
                            break
                        }
                    }
                    if (state) {
                        db.userDao().insertAll(
                            User(
                                userList.size+1,datuser,datpass
                            )
                        )
                    }
                    uiThread {
                        Log.w("Hasil",hasil)
                        if (state) {
                            RegisStatus.text="User ${RUsername.text} has been created"
                        }
                        else{
                            RegisStatus.text="User ${RUsername.text} has been created before !!!"
                        }
                    }
                }

            }
            else {RegisStatus.text="Username and Password cannot be empty"}
            RUsername.setText("")
            RPassword.setText("")
        }

        LoginText.setOnClickListener { // Ketika Login diklik, terjadi pemindahan screen dari RegisterActivity ke MainActivity
            var IntentToLogin = Intent(this, MainActivity::class.java)
            startActivity(IntentToLogin)
        }
    }
/* Menyimpan data yang ingin disimpan dalam key EXTRA agar tidak hilang ketika terjadi perubahan orientasi/bahasa*/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_UNAME,RegisStatus.text.toString())
    }
/* Menampilkan kembali data yang telah disimpan kedalam key EXTRA */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        RegisStatus.text=savedInstanceState?.getString(EXTRA_UNAME) ?: "Username and Password cannot be empty"
    }
}