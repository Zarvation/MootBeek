package com.example.meetbook

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MeetingDuration : JobService() {
    var ArrayItems : ByteArray? = null
    private val PrefFileName = "ROOMFILE001"
    override fun onStartJob(params: JobParameters?): Boolean {
        getQuotes(params)

        return true
    }
    private fun doNotif(quotes : String, author : String){
        Thread(Runnable {
            var roomSharedPrefHelper = SharedPrefHelper(this, PrefFileName)
            roomSharedPrefHelper.clearValues()

            // Panggil widgetmanager
            var appWidgetManager = AppWidgetManager.getInstance(this)
            // Ambil ID dari class widget BookedRoomWidget
            var ids = appWidgetManager.getAppWidgetIds(ComponentName(this,BookedRoomWidget::class.java))
            // Bentuk Intent dengan mengirimkan action UPDATE untuk menjalankan OnUpdate pada class widget
            var roomBookedIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            // Masukkan kedalam extra untuk menentukan apa yang akan diupdate dengan mengirimkan id
            roomBookedIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids)
            // send broadcast
            sendBroadcast(roomBookedIntent)

            val NotifyID = EXTRA_NOTIFICATION_MEETING
            val ChannelID = ID_CHANNEL_FINISH
            val name = "Meeting End"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val nNotifyChannel = NotificationChannel(ChannelID, name, importance)
            nNotifyChannel.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
            nNotifyChannel.setLightColor(Color.BLUE);
            nNotifyChannel.enableLights(true)
            nNotifyChannel.enableVibration(true)
            nNotifyChannel.setShowBadge(true)

            var mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //var duration: Long = intent.getLongExtra(EXTRA_MEETING_DURATION, 0L)

            //Mengakses layout untuk custom notification
            val contentView = RemoteViews(this.packageName, R.layout.meeting_notification_finish_layout)
            //Set title dan content yang ada di layout custom notification
            contentView.setTextViewText(R.id.tv_title, "Meeting Finished")
            contentView.setTextViewText(R.id.tv_content, "You have reached time limit !!!")
            contentView.setTextViewText(R.id.tv_quote, "Quotes Of The Day :\n\"$quotes\" \n- $author")

            var mBuilder = NotificationCompat.Builder(this, ChannelID)
                    .setContentTitle("Meeting Finished")
                    .setContentText("You have reached time limit !!!")
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Quotes Of The Day :\n\"$quotes\"\n- $author"))
                    .setSmallIcon(R.drawable.alarmclock)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.alarmclock))
                    .setColor(Color.GREEN)

            for (s in mNotificationManager.notificationChannels){
                mNotificationManager.deleteNotificationChannel(s.id)
            }
            mNotificationManager.createNotificationChannel(nNotifyChannel)
            mNotificationManager.notify(NotifyID, mBuilder.build())
            stopSelf()
        }).start()
    }

    private fun getQuotes(params: JobParameters?) {
        var client = AsyncHttpClient()
        val url = "https://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en"
        val charset = Charsets.UTF_8
        var handler = object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                var result = responseBody?.toString(charset) ?: "Kosong"
                Log.e("Hasil", result)
                var obj = JSONObject(result)
                var quotes = obj.getString("quoteText")
                var author = obj.getString("quoteAuthor")
                doNotif(quotes, author)
                jobFinished(params,false)
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                jobFinished(params,true)
            }
        }
        client.get(url,handler)

    }

    override fun onStopJob(params: JobParameters?): Boolean {
        var appWidgetManager = AppWidgetManager.getInstance(this)
        var ids = appWidgetManager.getAppWidgetIds(ComponentName(this,BookedRoomWidget::class.java))
        var roomBookedIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        roomBookedIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids)
        sendBroadcast(roomBookedIntent)
        return true
    }
    companion object {
        var quote = "quote"
    }
}