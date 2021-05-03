package com.example.meetbook

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookedRoom (var title: String, var capacity: Int, var image: String, var beginHour: Int, var endHour: Int, var beginMin: Int, var endMin: Int) :
    Parcelable {
}