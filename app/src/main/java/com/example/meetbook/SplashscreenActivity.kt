package com.example.meetbook

import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
//buat variabel SoundPool
private var sp: SoundPool? = null
//variabel untuk menangkap ID SoundPool
private var soundID = 0

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },4000)
    }
//load SoundPool pada awal siklus hidup activity
    override fun onStart() {
        super.onStart()
        //create SoundPool untuk SDK baru diatas versi Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            createNewSoundPool()
        }
        //create SoundPool untuk SDK dibawah versi Lollipop
        else{
            createAllSoundPool()
        }
        //fungsi untuk load Soundpool
        sp?.setOnLoadCompleteListener { soundPool, id, status ->
            if(status != 0){//apabila SoundPool gagal load
                Toast.makeText(this,"Gagal Load", Toast.LENGTH_SHORT).show()
            }
            else{//apabila SoundPool berhasil load
                Toast.makeText(this,"Load Berhasil", Toast.LENGTH_SHORT).show()
                //play SoundPool yang telah di load kedalam soundID ketika SoundPool berhasil di load
                sp?.play(soundID,.99f,.99f,1,0,.99f)
            }
        }
    //SoundPool yang ter-load akan di load ke variabel soundID
        soundID=sp?.load(this,R.raw.startup, 1) ?: 0
    }
    //fungsi create SoundPool versi lama dibawah Lollipop
    private fun createAllSoundPool() {
        sp= SoundPool(15, AudioManager.STREAM_MUSIC,0)
    }
    //fungsi create SoundPool varsi baru diatas Lollipop
    private fun createNewSoundPool() {
        sp=SoundPool.Builder()
                .setMaxStreams(15)
                .build()
    }
    //pada akhir siklus hidup SoundPool dilepas agar tidak memakan memori
    override fun onStop() {
        super.onStop()
        sp?.release()
        sp=null
    }
}

