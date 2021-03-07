package com.example.meetbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var client = intent.getParcelableExtra<Client>(EXTRA_CLIENT_DATA)
        ClientStatus.text = "Login as ${client?.Username}"
    }
}