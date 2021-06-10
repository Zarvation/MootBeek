package com.example.meetbook

import android.content.Context

val PREF_NAME = "widgetPref"
val WIDGET_IDS_KEY = "WIDGETIDS123"
class WidgetIdsPrefHelper (context: Context) {
    var widgetIdsPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

    fun setIds(ids : MutableSet<String>){
        var editor = widgetIdsPreferences.edit()
        editor.putStringSet(WIDGET_IDS_KEY,ids)
        editor.apply()
    }
    fun getIds() = widgetIdsPreferences.getStringSet(WIDGET_IDS_KEY, hashSetOf())
}