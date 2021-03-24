package com.example.meetbook

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class MeetingProgressService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        if(intent!=null){
            var meetingProgress = 0
            //Ambil durasi dari extra yang telah dikirimkan
            var duration: Long = intent.getLongExtra(EXTRA_MEETING_DURATION, 0L)

            //Mulai jalankan meeting progress
            for (i in 1..duration){
                Thread.sleep(1000)
                //Progress diupdate setiap detik
                meetingProgress += (100.toLong()/duration).toInt() * 2

                //Bentuk intent untuk proses data
                var intentMeetingProgress = Intent(ACTION_MEETING) //Action_meeting Untuk menentukan service dan broadcast

                //kirim persentase progress
                intentMeetingProgress.putExtra(EXTRA_MEETING_PERCENTAGE, meetingProgress)
                //kirim status belum selesai
                intentMeetingProgress.putExtra(EXTRA_MEETING_FINISH, false)
                //jika sudah mencapai durasi, update status menjadi selesai
                if (i >= duration){
                    intentMeetingProgress.putExtra(EXTRA_MEETING_FINISH, true)
                }
                //Data-data tersebut akan dikirim melalui broadcase
                sendBroadcast(intentMeetingProgress)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Meeting Finished", Toast.LENGTH_SHORT).show()
    }

    //Bentuk companion object untuk mengatur antrian
    companion object{
        //Membuat fungsi enqueueWork
        fun enqueueWork(context: Context, intent: Intent){
            //Masukkan context, intent yang akan dijalankan, pengenal service dan intent
            enqueueWork(context, MeetingProgressService::class.java, JOB_ID_MEETING_PROGRESS, intent)
        }
    }
}