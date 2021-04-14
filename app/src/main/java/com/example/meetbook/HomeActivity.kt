package com.example.meetbook

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.meetbook.fragments.AccountDetailFragment
import com.example.meetbook.fragments.InterfaceData
import com.example.meetbook.fragments.RoomBookFragment
import com.example.meetbook.fragments.RoomListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), InterfaceData {
    var client : Client? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.room_list -> {
                replaceFragment(RoomListFragment.newInstance(client?.Username.toString()))
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                replaceFragment(AccountDetailFragment.newInstance(client?.Username.toString()))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Menerima Extra dari MainACtivity
        client = intent.getParcelableExtra<Client>(EXTRA_CLIENT_DATA)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        replaceFragment(RoomListFragment.newInstance(client?.Username.toString()))

        //Mengirim client ke Fragment RoomList lewat newInstance
        //val roomlistfragment = RoomListFragment.newInstance(client?.Username.toString())
        //Begin transaction fragment
        //supportFragmentManager
        //    .beginTransaction().replace(R.id.fragmentContainer, roomlistfragment).commit()
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment).commit()
    }

    //Meng-override fungsi sendRoomData untuk memulai fragment RoomBook dengan mengirim data Room tersebut
    override fun sendRoomData(title: String, capacity: Int, image: String) {
        val fragmentBook = RoomBookFragment.newInstance(title,capacity,image)

        val transaksi = this.supportFragmentManager.beginTransaction()
        transaksi.replace(R.id.fragmentContainer, fragmentBook)
        transaksi.addToBackStack(null)
        transaksi.commit()
    }
}