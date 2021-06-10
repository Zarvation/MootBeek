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
    var WidgetPref : WidgetIdsPrefHelper? = null
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        if (WidgetPref == null){
            WidgetPref = WidgetIdsPrefHelper(context)
        }
        WidgetPref?.getIds()?.clear()
        var ids = WidgetPref?.getIds()

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            ids?.add(appWidgetId.toString())
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        if (ids != null){
            WidgetPref?.setIds(ids)
        }
    }

    override fun onEnabled(context: Context) {
       var alarmIntent = Intent(context, AvailableRoomWidget::class.java).let{
           it.action = ACTION_AUTO_UPDATE
           PendingIntent.getBroadcast(context, 123, it, PendingIntent.FLAG_UPDATE_CURRENT)
       }
        var cal = Calendar.getInstance()
        cal.add(Calendar.SECOND,30)

        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC,cal.timeInMillis,30000L,alarmIntent)
    }

    override fun onDisabled(context: Context) {
        var alarmIntent = Intent(context, AvailableRoomWidget::class.java).let{
            it.action = ACTION_AUTO_UPDATE
            PendingIntent.getBroadcast(context, 123, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(ACTION_AUTO_UPDATE)){
            if(WidgetPref == null){
                WidgetPref = WidgetIdsPrefHelper(context!!)
            }
            for (appWidgetId in WidgetPref?.getIds()!!) {
                updateAppWidget(context!!, AppWidgetManager.getInstance(context), appWidgetId.toInt())
            }
        }
        super.onReceive(context, intent)
    }

    companion object{
        private val PrefFileName = "ROOMFILE001"
        private var availRoom : MutableList<Rooms> = mutableListOf()
        private var availRoomString : MutableList<String> = mutableListOf()
        var ACTION_AUTO_UPDATE = "UPDATEAVAILROOM123"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.available_room_widget)
            var roomPref = SharedPrefHelper(context, PrefFileName)

            // Inisialisasi roomTransaction
            val tmp = roomTransaction(context)
            // Jalankan fungsi untuk membaca data Room dan masukkan ke List
            availRoom = tmp.viewAllRoom()
            for (data in availRoom){
                availRoomString.add("${data.title} with Capacity ${data.capacity} seats")
            }

            var dataSize = availRoomString.size
            var rndmNum = (0..dataSize).random()

            views.setTextViewText(R.id.availRoomText, availRoomString[rndmNum])
            views.setOnClickPendingIntent(R.id.actionsText,
                getPendingIntentActivity(context)
            )

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