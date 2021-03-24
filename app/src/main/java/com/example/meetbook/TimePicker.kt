package com.example.meetbook

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import org.w3c.dom.Text
import java.time.LocalTime
import java.util.*

class TimePicker(type: String) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    var hour : Int? = null
    var min : Int? = null
    var second : Int? = null
    var type = type
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR)
        var min = calendar.get(Calendar.MINUTE)
        //var second = calendar.get(Calendar.SECOND)
        return TimePickerDialog(activity, this, hour, min, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        var time = LocalTime.of(hourOfDay, minute)
        var st = activity?.findViewById<TextView>(R.id.StartTime)
        var et = activity?.findViewById<TextView>(R.id.EndTime)
        if (type == "start")
            st?.setText("$time")
        else
            et?.setText("$time")
    }
}