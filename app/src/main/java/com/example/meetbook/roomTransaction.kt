package com.example.meetbook

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import com.example.meetbook.roomDB.roomTable.Companion.COLUMN_CAP
import com.example.meetbook.roomDB.roomTable.Companion.COLUMN_ID
import com.example.meetbook.roomDB.roomTable.Companion.COLUMN_IMAGE
import com.example.meetbook.roomDB.roomTable.Companion.COLUMN_TITLE

// Buat class untuk mengakses database aplikasi induk
class roomTransaction(context: Context){
    // inisialisasi content resolver yang menghubungkan dengan aplikasi induk / provider
    private val roomContentResolver = context.contentResolver

    // Buat fungsi untuk membaca data Room
    fun viewAllRoom() : MutableList<Rooms>{
        val roomList : MutableList<Rooms> = mutableListOf()
        // Buat projection untuk menentukan data yang akan diselect
        var mProjection = arrayOf(COLUMN_ID,COLUMN_TITLE,COLUMN_CAP,COLUMN_IMAGE)
        // gunakan content resolver untuk menjalankan query pada provider di aplikasi induk
        // dan baca data ke dalam cursor
        var cursor  = roomContentResolver.query(roomContentProviderURI.CONTENT_URI,mProjection,null,null,null)

        var roomID : Int = 0
        var roomTitle : String = ""
        var roomCap : Int = 0
        var roomImage : String = ""
        // Baca cursor dari awal
        if (cursor != null){
            if (cursor.moveToFirst()) {
                // Ambil data dari cursor sesuai column dalam table room
                do {
                    // Baca column ID
                    roomID = cursor.getInt(
                        cursor.getColumnIndex(COLUMN_ID)
                    )
                    // Baca column title
                    roomTitle = cursor.getString(
                        cursor.getColumnIndex(COLUMN_TITLE)
                    )
                    // Baca column capacity
                    roomCap = cursor.getInt(
                        cursor.getColumnIndex(COLUMN_CAP)
                    )
                    // Baca column image
                    roomImage = cursor.getString(
                        cursor.getColumnIndex(COLUMN_IMAGE)
                    )
                    // Tambahkan data-data yang telah dibaca ke list Room
                    roomList.add(Rooms(roomID,roomTitle,roomCap,roomImage))
                } while (cursor.moveToNext()) // Lanjut ke data selanjutnya dalam cursor
            }
        }

        // Kembalikan list Room
        return roomList
    }
}