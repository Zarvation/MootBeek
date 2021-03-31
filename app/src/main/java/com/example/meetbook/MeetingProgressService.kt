package com.example.meetbook

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.meetbook.fragments.RoomBookFragment

class MeetingProgressService : JobIntentService() {
    //inisialisasi notification manager, notification channel,
    //dan builder untuk membuat dan menjalankan notifikasi
    //lateinit var notificationManager: NotificationManager
    //lateinit var notificationChannel: NotificationChannel
    //lateinit var builder: Notification.Builder
    //buat channelID
    //private val channelId = "com.example.meetbook123"
    //private val description = "Start Notification"
    override fun onHandleWork(intent: Intent) {
        //Notifikasi akan bekerja sebagai service, sehingga dapat dijalankan
        //walaupun aplikasi dalam keadaan tertutup
        //notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(intent!=null){
            //Melakukan pengecekan versi diatas oreo, untuk membentuk notification
            //channel berdasarkan channelId
            //sehingga setiap notifikasi dapat dibedakan satu dengan yang lain
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //Buat notification Channel
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.BLUE
                notificationChannel.enableVibration(true)
                notificationChannel.setShowBadge(true)
                notificationManager.createNotificationChannel(notificationChannel)

                //val intent = Intent(this, RoomBookFragment::class.java)

                //Buat notifikasi dengan memasukkan title, content dan icon
                builder = Notification.Builder(this, channelId)
                        .setContentTitle("Meeting Started")
                        .setContentText("Attend the meeting !!!")
                        .setShowWhen(true)
                        .setSmallIcon(R.drawable.alarmclock)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.alarmclock))
            }else{ //Bila dibawah versi oreo
                builder = Notification.Builder(this)
                        .setContentTitle("Meeting Started")
                        .setContentText("Attend the meeting !!!")
                        .setShowWhen(true)
                        .setSmallIcon(R.drawable.alarmclock)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.alarmclock))
            }
            //Tampilkan notifikasi
            notificationManager?.notify(EXTRA_NOTIFICATION_MEETING,builder.build())*/

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