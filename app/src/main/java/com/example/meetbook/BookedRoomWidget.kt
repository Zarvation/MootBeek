package com.example.meetbook

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
// Buat file Widget untuk menampilkan room yang dibook
class BookedRoomWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    companion object{
        // Inisialisasi file sharedpreferences yang menampung room yang dibook
        private val PrefFileName = "ROOMFILE001"
        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.booked_room_widget)

            // Akses sharedpreferences
            var roomPref = SharedPrefHelper(context, PrefFileName)

            // Ambil title room yang dibook
            var roomTitle = roomPref.name
            // Buat text untuk meng-info meeting sedang dijalani
            var setInfo = "You have a meeting at ${roomTitle}"

            // Jika title kosong atau tidak ada room yang di book
            if (roomTitle == ""){
                // set text info bahwa tidak ada meeting saat ini
                views.setTextViewText(R.id.infoText,"You have no meeting currently")
            }
            else{
                // Jika ada, set text info menjadi text yang telah dibuat tadi
                views.setTextViewText(R.id.infoText,setInfo)
            }

            // Ketika layout widget di klik, jalankan fungsi untuk memulai intent mainactivity
            views.setOnClickPendingIntent(R.id.meetingInfoWidget, getPendingIntentActivity(context))
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        // Buat fungsi untuk memulai pneding intent class mainactivity
        private fun getPendingIntentActivity(context: Context): PendingIntent {
            var warpIntent = Intent(context,MainActivity::class.java)
            return PendingIntent.getActivity(context,0,warpIntent,0)
        }
    }
}