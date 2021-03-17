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

        RegisButton.setOnClickListener {
            if (RUsername.text.toString().length>0 && RPassword.text.toString().length>0){
                RegisStatus.text="User ${RUsername.text} has been created"
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