package com.example.meetbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.meetbook.fragments.InterfaceData
import com.example.meetbook.fragments.RoomBookFragment
import com.example.meetbook.fragments.RoomListFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), InterfaceData {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var client = intent.getParcelableExtra<Client>(EXTRA_CLIENT_DATA)
        val roomlistfragment = RoomListFragment.newInstance(client?.Username.toString())
        supportFragmentManager
            .beginTransaction().replace(R.id.fragmentContainer, roomlistfragment).commit()
    }

    override fun sendRoomData(title: String, capacity: Int, image: String) {
        val fragmentB = RoomBookFragment.newInstance(title.toString(), capacity, image.toString())

        val transaksi = this.supportFragmentManager.beginTransaction()
        transaksi.replace(R.id.fragmentContainer, fragmentB)
        transaksi.addToBackStack(null)
        transaksi.commit()
    }
}