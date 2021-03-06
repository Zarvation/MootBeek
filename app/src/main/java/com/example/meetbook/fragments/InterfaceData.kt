package com.example.meetbook.fragments

import androidx.fragment.app.Fragment

interface InterfaceData {
    fun sendRoomData(id: Int, title: String, capacity: Int, image: String)
    // Membuat fungsi untuk mengirim data room yang telah di book
    fun sendBookedRoomData(id: Int, title: String, capacity: Int, image: String, beginHour: Int, endHour: Int, beginMin: Int, endMin: Int)
    fun openSavedRoomData()
    fun openEditUser(id: Int, username: String, password: String)
}