package com.example.meetbook

import android.content.Context

// Buat shared preferences untuk menampung ID widget
// Tentukan nama file nya dam key untuk widget id
val PREF_NAME = "widgetPref"
val WIDGET_IDS_KEY = "WIDGETIDS123"
class WidgetIdsPrefHelper (context: Context) {
    // akses file shared preferences
    var widgetIdsPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

    // Buat fungsi untuk set ID dengna memasukkan ID-ID widget berupa string set
    fun setIds(ids : MutableSet<String>){
        var editor = widgetIdsPreferences.edit()
        editor.putStringSet(WIDGET_IDS_KEY,ids)
        editor.apply()
    }
    // buat fungsi getID untuk mengambil ID-ID widget yang tersimpan
    fun getIds() = widgetIdsPreferences.getStringSet(WIDGET_IDS_KEY, hashSetOf())
}