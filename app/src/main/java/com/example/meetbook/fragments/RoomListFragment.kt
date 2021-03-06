package com.example.meetbook.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.meetbook.*
import com.example.meetbook.HomeActivity.Companion.current_password
import com.example.meetbook.HomeActivity.Companion.current_username
import kotlinx.android.synthetic.main.fragment_room_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RoomListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoomListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: Int? = null
    private var RoomItems : MutableList<Rooms> = mutableListOf(

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getInt(ARG_CLIENT_ID)
        }

        var db= Room.databaseBuilder(
                context!!,
                RoomUserDBHelper::class.java,
                "meetbookDb.db"
        ).build()

        doAsync {
            for (allData in db.userDao().getUser(param3!!)){
                param1 = allData.username
                param2 = allData.password
            }
        }

        //Decode Gambar menjadi bitmap dan set image pada imageview
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.meetroom1)
        //meetimage.setImageBitmap(bitmap as Bitmap)

        //Convert Image into Base64 String --> Start
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val imageString: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        // --> End

        //RoomItems.add(Rooms(1, "Room 1A", 8, imageString))
        //RoomItems.add(Rooms(2, "Room 2A", 10, imageString))

        // Inisialisasi roomTransaction
        val tmp = roomTransaction(context!!)
        // Jalankan fungsi untuk membaca data Room dan masukkan ke List untuk ditampilkan pada recyclerView
        RoomItems = tmp.viewAllRoom()
    }

    //Tambah interfaceData
    private lateinit var interfaceData: InterfaceData
    private lateinit var RoomListdapter : RoomListRecyclerViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //hapus "return" ganti dengan val View agar dapat memanggil property dari layout fragment
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)

        //Deklarasi komponen
        //val meettitle = view.findViewById<TextView>(R.id.MeetRoomTitle)
        //val meetcap = view.findViewById<TextView>(R.id.MeetRoomCap)
        //val meetimage = view.findViewById<ImageView>(R.id.MeetRoomImg)
        //val roomItem = view.findViewById<LinearLayout>(R.id.RoomItem)
        val clientStatus = view.findViewById<TextView>(R.id.ClientStatus)
        val recyclerviewget = view.findViewById<RecyclerView>(R.id.recyclerViewRoomList)

        //Menerima isi argument
        /*var usernameClient = ""
        var passwordClient = ""
        var idClient = arguments?.getInt(ARG_CLIENT_ID)*/

        clientStatus.text = "Login as $current_username"

        //tambahkan interfacedata dengan aktivitas dari interfaceData
        interfaceData = activity as InterfaceData

        //Decode Gambar menjadi bitmap dan set image pada imageview
        //val bitmap = BitmapFactory.decodeResource(resources, R.drawable.meetroom1)
        //meetimage.setImageBitmap(bitmap as Bitmap)

        //Convert Image into Base64 String --> Start
        //val byteArrayOutputStream = ByteArrayOutputStream()
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        //val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        //val imageString: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        // --> End

        //Btn diclick -> jalankan fungsi interfaceData (kembali ke HomeActivity untuk override)
        //roomItem.setOnClickListener {
        //    interfaceData.sendRoomData(meettitle.text.toString(),meetcap.text.substring(0,1).toInt(),imageString)
        //}

        //RoomItems.add(Rooms("Room 1A", 8, imageString))
        //RoomItems.add(Rooms("Room 2A", 10, imageString))

        //Roomlist
        RoomListdapter = RoomListRecyclerViewAdapter(RoomItems, interfaceData)
        recyclerviewget.adapter = RoomListdapter
        recyclerviewget.layoutManager = LinearLayoutManager(activity)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoomListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: Int) =
                RoomListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_CLIENT_NAME, param1) //mengirim bundle yang berisi param1 ke argument
                        putString(ARG_CLIENT_PASSWORD, param2)
                        putInt(ARG_CLIENT_ID, param3)
                    }
                }
    }
}