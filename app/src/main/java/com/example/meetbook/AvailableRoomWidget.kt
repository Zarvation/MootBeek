package com.example.meetbook

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.RemoteViews
import androidx.room.Room
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class AvailableRoomWidget : AppWidgetProvider() {
    // Inisialisasi helper untuk id widget
    var WidgetPref : WidgetIdsPrefHelper? = null
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // jika sharedpref masih null, panggil sharedpref
        if (WidgetPref == null){
            WidgetPref = WidgetIdsPrefHelper(context)
        }
        // reset sharedpref
        WidgetPref?.getIds()?.clear()
        // panggil getIds
        var ids = WidgetPref?.getIds()

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            // masukkan id-id widget ke dalam var ids
            ids?.add(appWidgetId.toString())
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        // Kirim kan id-id yang telah ditangkap ke dalam sharedpref
        if (ids != null){
            WidgetPref?.setIds(ids)
        }
    }

    // Override fungsi onEnabled, ketika widget ditambahkan ke home screen, mulai alarm manager
    // untuk mengupdate isi widget setiap 30 detik
    override fun onEnabled(context: Context) {
        // Buat alarm intent untuk menjalankan class widget availableRoomWidget dengan mengirimkan action
        // ACTION_AUTO_UPDATE
        var alarmIntent = Intent(context, AvailableRoomWidget::class.java).let{
            it.action = ACTION_AUTO_UPDATE
            // bentuk sebagai pending intent untuk menjalankan broadcast
            PendingIntent.getBroadcast(context, 123, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        // tentukan waktu kapan widget akan dijalankan dengan menggunakan kalender
        var cal = Calendar.getInstance()
        cal.add(Calendar.SECOND,30)

        // Jalankan alarm manager
        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // set alarm untuk diulang setiap 30 detik sekali
        alarmManager.setRepeating(AlarmManager.RTC,cal.timeInMillis,30000L,alarmIntent)
    }

    // Override fungsi onDisabled, ketika widget dihapus, Cancel atau matikan alarm manager yang berjalan
    override fun onDisabled(context: Context) {
        var alarmIntent = Intent(context, AvailableRoomWidget::class.java).let{
            it.action = ACTION_AUTO_UPDATE
            PendingIntent.getBroadcast(context, 123, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent)
    }

    // Tangkap data yang diupdate dengan override fungsi onReceive
    override fun onReceive(context: Context?, intent: Intent?) {
        // Jika intent yang dikirim memiliki action AUTO_UPDATE
        // lakukan update pada widget
        if (intent?.action.equals(ACTION_AUTO_UPDATE)){
            if(WidgetPref == null){
                WidgetPref = WidgetIdsPrefHelper(context!!)
            }
            // jika sharedpref tidak null, panggil getIds dan jalankan updateAppWidget, update setiap id
            for (appWidgetId in WidgetPref?.getIds()!!) {
                updateAppWidget(context!!, AppWidgetManager.getInstance(context), appWidgetId.toInt())
            }
        }
        super.onReceive(context, intent)
    }

    companion object{
        // Buat List untuk menampung room dan title room
        private var availRoom : MutableList<Rooms> = mutableListOf()
        private var availRoomString : MutableList<String> = mutableListOf()

        // buat variable ACTION_AUTO_UPDATE yang akan digunakan untuk update widget
        var ACTION_AUTO_UPDATE = "UPDATEAVAILROOM123"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.available_room_widget)

            // Inisialisasi roomTransaction
            val tmp = roomTransaction(context)
            // Jalankan fungsi untuk membaca data Room dan masukkan ke List
            availRoom = tmp.viewAllRoom()

            // Untuk setiap data Room dalam list, masukkan title ke dalam list room title
            for (data in availRoom){
                availRoomString.add("${data.title} with Capacity ${data.capacity} seats")
            }

            // Lakukan proses untuk generate room secara random
            // Ambil jumlah room
            var dataSize = availRoomString.size
            // kemudian random salah satu room tersebut
            var rndmNum = (0..dataSize).random()
            // dan tampilkan ke widget
            views.setTextViewText(R.id.availRoomText, availRoomString[rndmNum])

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun updateText() : String {
            var dataSize = availRoomString.size
            var rndmNum = (0..dataSize).random()

            return availRoomString[rndmNum]
        }

        private fun getPendingIntentActivity(context: Context): PendingIntent {
            var warpIntent = Intent(context,AvailableRoomWidget::class.java)
            return PendingIntent.getActivity(context,0,warpIntent,0)
        }
    }
}