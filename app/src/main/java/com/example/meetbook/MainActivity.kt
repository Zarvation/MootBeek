package com.example.meetbook

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {
    private val PrefFileName = "ROOMFILE001"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var db= Room.databaseBuilder(
            this,
            RoomUserDBHelper::class.java,
            "meetbookDb.db"
        ).build()

        LoginButton.setOnClickListener { // Ketika Login diklik, terjadi pemindahan screen dari MainActivity ke HomeActivity dengan membawa data object Client yang berisi username dan password terisi
            var IntentToHome = Intent(this, HomeActivity::class.java) // Membuat Intent
            if (LUsername.text.toString().isEmpty()){
                Toast.makeText(this,"Username Belum Diisi",Toast.LENGTH_SHORT).show()
            }
            else if (LPassword.text.toString().isEmpty()){
                Toast.makeText(this,"Password Belum Diisi",Toast.LENGTH_SHORT).show()
            }
            else{
                var roomSharedPrefHelper = SharedPrefHelper(this, PrefFileName)
                roomSharedPrefHelper.clearValues()

                var state = false
                var datuser = LUsername.text.toString()
                var datpass = LPassword.text.toString()
                doAsync {
                    var userList = db.userDao().getAllData()
                    for(allData in userList){
                        if (datuser == allData.username){
                            if (datpass == allData.password){
                                var client = Client(allData._id, LUsername.text.toString(), LPassword.text.toString())
                                IntentToHome.putExtra(
                                    EXTRA_CLIENT_DATA,
                                    client
                                ) // Mengirim Extra dengan key yang tersimpan di Keys.kt
                                startActivity(IntentToHome)
                                state = true
                                break
                            }
                        }
                    }
                    uiThread {
                        if (state)
                            Toast.makeText(this@MainActivity,"Login Sukses",Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this@MainActivity,"Username dan Password Salah",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        RegisText.setOnClickListener { // Ketika Register diklik, terjadi pemindahan screen dari MainActivity ke RegisterActivity
            var IntentToRegis = Intent(this,RegisterActivity::class.java)
            startActivity(IntentToRegis)
        }
        var receiver= AirplaneModeReceiver()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also{
            registerReceiver(receiver,it)
        }
    }

    fun openWeb(view: View) {
        //Intent implisit browsing
        //var txt = "supportus"
        var googleSearch = Uri.parse("https://www.google.com/search?q=supportus")
        var googleIntent = Intent(Intent.ACTION_VIEW, googleSearch)
        //googleIntent.data = googleSearch

        //memeriksa apakah intent ini bisa dijalankan atau tidak
        if (googleIntent.resolveActivity((packageManager)) != null)
        {
            startActivity(googleIntent)
        }
        else
            Log.e("intent", "gagal dijalankan")
    }
}