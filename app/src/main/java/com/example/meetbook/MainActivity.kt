package com.example.meetbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoginButton.setOnClickListener {
            var IntentToHome = Intent(this, HomeActivity::class.java)
            var client = Client(LUsername.text.toString(), LPassword.text.toString())
            IntentToHome.putExtra(EXTRA_CLIENT_DATA,client)
            startActivity(IntentToHome)
        }
        RegisText.setOnClickListener {
            var IntentToRegis = Intent(this,RegisterActivity::class.java)
            startActivity(IntentToRegis)
        }
    }
}