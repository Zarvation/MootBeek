package com.example.meetbook

import android.app.*
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.util.*

class MeetingAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val NotifyID = EXTRA_NOTIFICATION_MEETING
        val ChannelID = ID_CHANNEL_START
        val name = "Meeting Start"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val nNotifyChannel = NotificationChannel(ChannelID, name, importance)
        nNotifyChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
        nNotifyChannel.setLightColor(Color.BLUE);
        nNotifyChannel.enableLights(true)
        nNotifyChannel.enableVibration(true)
        nNotifyChannel.setShowBadge(true)

        var mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //var duration: Long = intent.getLongExtra(EXTRA_MEETING_DURATION, 0L)

        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(111,PendingIntent.FLAG_UPDATE_CURRENT)
        //val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        var mBuilder = Notification.Builder(context, ChannelID)
                .setContentTitle("Meeting Started")
                .setContentText("Attend the meeting !!!")
                .addAction(0,"ATTEND",pendingIntent)
                .setSmallIcon(R.drawable.alarmclock)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.alarmclock))
                .setColor(Color.GREEN)

        for (s in mNotificationManager.notificationChannels){
            mNotificationManager.deleteNotificationChannel(s.id)
        }
        mNotificationManager.createNotificationChannel(nNotifyChannel)
        mNotificationManager.notify(NotifyID, mBuilder.build())

        /*var serviceComponent = ComponentName(context,MeetingDuration::class.java)
        var mJobInfo = JobInfo.Builder(123, serviceComponent)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setRequiresDeviceIdle(false)
            .setRequiresCharging(false)
            .setPeriodic(duration * 1000)
        var JobMeeting = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        JobMeeting.schedule(mJobInfo.build())
        //Log.w("Job", "Mulai JOB")
        Toast.makeText(context,"Job Service Berjalan", Toast.LENGTH_SHORT).show()*/
    }

}