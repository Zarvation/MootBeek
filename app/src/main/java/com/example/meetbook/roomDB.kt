package com.example.meetbook

import android.net.Uri
import android.provider.BaseColumns

// Bentuk object sesuai dengan yang ada pada aplikasi induk untuk table Room
object roomDB {
    class roomTable : BaseColumns {
        companion object{
            val TABLE_ROOM = "tbl_room"
            val COLUMN_ID = "Room_ID"
            val COLUMN_TITLE = "Room_Title"
            val COLUMN_CAP = "Room_Cap"
            val COLUMN_IMAGE = "Room_Image"
        }
    }
}
// Buat class untuk menyimpan content uri
class roomContentProviderURI{
    companion object{
        val AUTHORITY = "com.example.meetbookadmin.provider.provider.meetbookContentProvider"
        val ROOM_TABLE = roomDB.roomTable.TABLE_ROOM
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$ROOM_TABLE")
    }
}