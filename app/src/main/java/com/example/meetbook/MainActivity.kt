package com.example.meetbook

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.meetbook.HomeActivity.Companion.current_password
import com.example.meetbook.HomeActivity.Companion.current_username
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class MainActivity : AppCompatActivity() {
    private val PrefFileName = "ROOMFILE001"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkBoxRemember.isChecked = false

        // Buat dan panggil database dengan databaseBuilder
        var db= Room.databaseBuilder(
            this, // Darimana di build
            RoomUserDBHelper::class.java, // Database yang akan dibangun
            "meetbookDb.db" // nama database
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
                //var roomSharedPrefHelper = SharedPrefHelper(this, PrefFileName)
                //roomSharedPrefHelper.clearValues()

                // Ketika login diklik,
                var state = false // Deklarasi status apakah user terdapat di database
                // Deklarasi user dan pass yang dimasukkan
                var datuser = LUsername.text.toString()
                var datpass = LPassword.text.toString()

                doAsync {
                    // Ambil data dari User dengan memanggil fungsi getAllData() dari db.userDao()
                    var userList = db.userDao().getAllData()
                    // Lakukan pengecekan apakah username dan password yang diinput sesuai/terdaftar
                    for(allData in userList){
                        if (datuser == allData.username){
                            if (datpass == allData.password){
                                // Jika terdaftar, kirimkan ID ke HomeActivity
                                var client = Client(allData._id, LUsername.text.toString(), LPassword.text.toString())
                                IntentToHome.putExtra(
                                    EXTRA_CLIENT_DATA,
                                    client
                                ) // Mengirim Extra dengan key yang tersimpan di Keys.kt

                                // Kirimkan username dan password ke dalam companion object milik HomeActivity
                                current_username = datuser
                                current_password = datpass
                                // Mulai activity
                                startActivity(IntentToHome)
                                state = true
                                break // Hentikan pengecekan
                            }
                        }
                    }
                    uiThread {
                        if (state) {
                            Toast.makeText(this@MainActivity, "Login Sukses", Toast.LENGTH_SHORT)
                                .show()
                        }
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

        // Ketika button remove diklik, munculkan dialog
        removeBtn.setOnClickListener {
            var BuilderDialog = AlertDialog.Builder(this)
            // Gunakan layout dialog_remove_user.xml untuk dialog
            var inflaterDialog = layoutInflater.inflate(R.layout.dialog_remove_user, null)
            BuilderDialog.setView(inflaterDialog)
            // Jika button Remove diklik
            BuilderDialog.setPositiveButton("Remove"){ dialogInterface: DialogInterface, i: Int ->
                // Ambil username dan password yang dimasukkan ke dalam layout
                var getUsername = inflaterDialog.findViewById<EditText>(R.id.usernameToRemove)
                var getPassword = inflaterDialog.findViewById<EditText>(R.id.passwordToRemove)

                var targetuser = getUsername.text.toString()
                var targetpass = getPassword.text.toString()

                doAsync {
                    // gunakan getPassByName() untuk mengambil password dari username yang dimasukkan
                    // dan cocokkan dengan password yang di isi
                    if (targetpass == db.userDao().getPassByName(targetuser)){
                        // Jika sesuai, hapus user dengan deleteUser() sesuai dengan username yang dimasukkan
                        db.userDao().deleteUser(targetuser)
                    }
                    uiThread {
                        Toast.makeText(this@MainActivity,"User Deleted",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            BuilderDialog.create().show()
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