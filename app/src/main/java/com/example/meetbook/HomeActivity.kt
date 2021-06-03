package com.example.meetbook

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.meetbook.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), InterfaceData {
    var client : Client? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.room_list -> {
                replaceFragment(RoomListFragment.newInstance(client?.Username.toString(),client?.Password.toString(),client?.Id!!.toInt()))
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                replaceFragment(AccountDetailFragment.newInstance(client?.Username.toString(),client?.Password.toString(),client?.Id!!.toInt()))
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
        replaceFragment(RoomListFragment.newInstance(client?.Username.toString(),client?.Password.toString(),client?.Id!!.toInt()))

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
    override fun sendRoomData(id: Int, title: String, capacity: Int, image: String) {
        val fragmentBook = RoomBookFragment.newInstance(id,title,capacity,image)

        val transaksi = this.supportFragmentManager.beginTransaction()
        transaksi.replace(R.id.fragmentContainer, fragmentBook)
        transaksi.addToBackStack(null)
        transaksi.commit()
    }

    // Mengoverride fungsi sendBookedRoomData
    override fun sendBookedRoomData(id: Int,
        title: String,
        capacity: Int,
        image: String,
        beginHour: Int,
        endHour: Int,
        beginMin: Int,
        endMin: Int
    ) {
        // Kirim data booked room ke fragment BookedRoom lewat newInstance
        val fragmentBooked = BookedRoomFragment.newInstance(id,title,capacity,image,beginHour,endHour,beginMin,endMin)

        // Mulai fragment BookedRoom
        val transaksi2 = this.supportFragmentManager.beginTransaction()
        transaksi2.replace(R.id.fragmentContainer, fragmentBooked)
        transaksi2.addToBackStack(null)
        transaksi2.commit()
    }

    override fun openSavedRoomData() {
        val fragmentSaved = SavedMeetingFragment()
        val transaksi3 = this.supportFragmentManager.beginTransaction()
        transaksi3.replace(R.id.fragmentContainer, fragmentSaved)
        transaksi3.addToBackStack(null)
        transaksi3.commit()
    }

    override fun openEditUser(id: Int, username: String, password: String) {
        val fragmentEditUser = EditUserFragment.newInstance(id, username, password)
        val transaksi4 = this.supportFragmentManager.beginTransaction()
        transaksi4.replace(R.id.fragmentContainer, fragmentEditUser)
        transaksi4.addToBackStack(null)
        transaksi4.commit()
    }

    // Buat Companion object yang berisi username dan password agar dapat diakses ketika user melakukan login
    companion object{
        var current_username = ""
        var current_password = ""
    }
}