package com.example.meetbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        RegisButton.setOnClickListener{
            var IntentRegis=Intent(this,MainActivity::class.java)
            startActivity(IntentRegis)
        }

        LoginText.setOnClickListener {
            var IntentToLogin = Intent(this, MainActivity::class.java)
            startActivity(IntentToLogin)
        }
    }
}