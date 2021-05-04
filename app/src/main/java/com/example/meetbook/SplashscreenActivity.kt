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

private var sp: SoundPool? = null
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

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            createNewSoundPool()
        }
        else{
            createAllSoundPool()
        }
        sp?.setOnLoadCompleteListener { soundPool, id, status ->
            if(status != 0){
                Toast.makeText(this,"Gagal Load", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Load Berhasil", Toast.LENGTH_SHORT).show()
                sp?.play(soundID,.99f,.99f,1,0,.99f)
            }
        }
        soundID=sp?.load(this,R.raw.startup, 1) ?: 0
    }

    private fun createAllSoundPool() {
        sp= SoundPool(15, AudioManager.STREAM_MUSIC,0)
    }

    private fun createNewSoundPool() {
        sp=SoundPool.Builder()
                .setMaxStreams(15)
                .build()
    }

    override fun onStop() {
        super.onStop()
        sp?.release()
        sp=null
    }
}

