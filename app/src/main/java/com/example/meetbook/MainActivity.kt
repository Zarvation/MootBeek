package com.example.meetbook

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoginButton.setOnClickListener { // Ketika Login diklik, terjadi pemindahan screen dari MainActivity ke HomeActivity dengan membawa data object Client yang berisi username dan password terisi
            var IntentToHome = Intent(this, HomeActivity::class.java) // Membuat Intent
            var client = Client(LUsername.text.toString(), LPassword.text.toString())
            IntentToHome.putExtra(EXTRA_CLIENT_DATA,client) // Mengirim Extra dengan key yang tersimpan di Keys.kt
            startActivity(IntentToHome)
        }
        RegisText.setOnClickListener { // Ketika Register diklik, terjadi pemindahan screen dari MainActivity ke RegisterActivity
            var IntentToRegis = Intent(this,RegisterActivity::class.java)
            startActivity(IntentToRegis)
        }
    }

    fun openWeb(view: View) {
        //Intent implisit browsing
        var txt = "supportus"
        var googleSearch = Uri.parse("https://www.google.com/search?q=$txt")
        var googleIntent = Intent(Intent.ACTION_VIEW, googleSearch)

        //memeriksa apakah intent ini bisa dijalankan atau tidak
        if (googleIntent.resolveActivity((packageManager)) != null)
        {
            startActivity(googleIntent)
        }
        else
            Log.e("intent", "gagal dijalankan")
    }
}