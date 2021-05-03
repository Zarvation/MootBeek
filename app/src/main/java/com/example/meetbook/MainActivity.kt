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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val PrefFileName = "ROOMFILE001"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                var client = Client(LUsername.text.toString(), LPassword.text.toString())
                IntentToHome.putExtra(EXTRA_CLIENT_DATA,client) // Mengirim Extra dengan key yang tersimpan di Keys.kt
                startActivity(IntentToHome)
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