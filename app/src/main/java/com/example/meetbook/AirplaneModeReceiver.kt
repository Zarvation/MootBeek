package com.example.meetbook

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

class AirplaneModeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(Settings.System.getInt(context.contentResolver,
                        Settings.Global.AIRPLANE_MODE_ON,1)==1){
            Toast.makeText(context,"Airplane Mode Enabled", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context,"Airplane Mode Disabled", Toast.LENGTH_SHORT).show()
        }
    }
}