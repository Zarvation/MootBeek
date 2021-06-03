package com.example.meetbook

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rooms (var id: Int = 0, var title: String = "", var capacity: Int = 0, var image: String = "") : Parcelable {
}