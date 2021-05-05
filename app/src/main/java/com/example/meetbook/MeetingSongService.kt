package com.example.meetbook

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast

// Buat Service untuk media player lagu meeting dan inisialisasi variable untuk
// aksi-aksi media player seperti Play, Resume, Pause dan Stop serta Create
const val ACTION_PLAY = "PLAYSONG123"
const val ACTION_PAUSE = "PAUSESONG123"
const val ACTION_RESUME = "RESUMESONG123"
const val ACTION_STOP = "STOPSONG123"
const val ACTION_CREATE = "CREATESONG123"

class MeetingSongService : Service(),
MediaPlayer.OnPreparedListener, // Implementasikan listener ketika file berhasil disiapkan
MediaPlayer.OnCompletionListener, // file berhasil dibaca seluruhnya
MediaPlayer.OnErrorListener{ // terjadi error saat membaca file

    // Inisialisasi objek media player
    private var mediaPlayer : MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    // Override fungsi ketika Service dimulai
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null){
            // Mengambil aksi yang dikirimkan intent
            var actionIntent = intent.action
            when(actionIntent){
                // Jika aksi Create, maka buat
                ACTION_CREATE -> {
                    // Buat Media Player dan tentukan listener
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setOnPreparedListener(this)
                    mediaPlayer?.setOnCompletionListener(this)
                    mediaPlayer?.setOnErrorListener(this)
                }
                // Jika aksi play
                ACTION_PLAY -> {
                    // Jika media player belum bermain
                    if(!mediaPlayer!!.isPlaying){
                        // Mengubah file audio menjadi file descriptor
                        val assetFileDescriptor = this.resources.openRawResourceFd(R.raw.meetingsong)
                        // Jalankan media player
                        mediaPlayer?.run{
                            // Pastikan media player kosong
                            reset()
                            // Media player dijalankan dengan data source
                            setDataSource(
                                    // darimana filenya, dimulai dari kapan dan panjang file yang akan dijalankan
                                    assetFileDescriptor.fileDescriptor,
                                    assetFileDescriptor.startOffset,
                                    assetFileDescriptor.declaredLength
                            )
                            // Karena proses yang panjang, gunakan Async untuk memisah proses dari UI Thread
                            // Agar tidak terjadi ANR
                            prepareAsync()
                        }
                    }
                }
                // Jika aksi Pause, pause media player yang sedang berjalan dengan .pause()
                ACTION_PAUSE -> {
                    mediaPlayer?.pause()
                }
                // Jika aksi Resume, lanjutkan media player dengan .start()
                ACTION_RESUME -> {
                    mediaPlayer?.start()
                }
                // Jika aksi Stop, hentikan media player dengan .stop()
                ACTION_STOP -> {
                    mediaPlayer?.stop()
                }
            }
        }
        return flags
    }

    // Jika file telah disiapkan, Jalankan media player dengan .start()
    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer?.start()
    }

    // Jika file telah selesai dibaca, jalankan toast untuk mengindikasi file / audio telah selesai dibaca
    override fun onCompletion(mp: MediaPlayer?) {
        Toast.makeText(this,"Song Finished",Toast.LENGTH_SHORT).show()
    }

    // Jika terjadi error ketika membaca file, kirimkan toast untuk mengindikasinya
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Toast.makeText(this,"Error Audio",Toast.LENGTH_SHORT).show()
        return false
    }

    // gunakan .release() untuk menghemat memori ketika media player tidak digunakan lagi
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}