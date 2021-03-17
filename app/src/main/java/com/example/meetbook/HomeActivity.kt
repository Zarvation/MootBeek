package com.example.meetbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.meetbook.fragments.InterfaceData
import com.example.meetbook.fragments.RoomBookFragment
import com.example.meetbook.fragments.RoomListFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), InterfaceData {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Menerima Extra dari MainACtivity
        var client = intent.getParcelableExtra<Client>(EXTRA_CLIENT_DATA)

        //Mengirim client ke Fragment RoomList lewat newInstance
        val roomlistfragment = RoomListFragment.newInstance(client?.Username.toString())
        //Begin transaction fragment
        supportFragmentManager
            .beginTransaction().replace(R.id.fragmentContainer, roomlistfragment).commit()
    }

    //Meng-override fungsi sendRoomData untuk memulai fragment RoomBook dengan mengirim data Room tersebut
    override fun sendRoomData(title: String, capacity: Int, image: String) {
        val fragmentBook = RoomBookFragment.newInstance(title.toString(), capacity, image)

        val transaksi = this.supportFragmentManager.beginTransaction()
        transaksi.replace(R.id.fragmentContainer, fragmentBook)
        transaksi.addToBackStack(null)
        transaksi.commit()
    }
}