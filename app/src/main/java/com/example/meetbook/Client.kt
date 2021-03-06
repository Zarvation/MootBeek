package com.example.meetbook

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Client(var Id : Int, var Username : String, var Password : String) : Parcelable {
}

// Object parcelable