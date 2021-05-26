package com.example.meetbook.fragments

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.meetbook.*
import kotlinx.android.synthetic.main.fragment_account_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountDetailFragment : Fragment()/*, LoaderManager.LoaderCallbacks<Cursor>*/ {
    // Inisialisasi Filename untuk SharedPreferences
    private val PrefFileName = "ROOMFILE001"
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: Int? = null

    //var DisplayName = ContactsContract.Contacts.DISPLAY_NAME
    //var Number = ContactsContract.CommonDataKinds.Phone.NUMBER
    //var Image = ContactsContract.CommonDataKinds.Photo.PHOTO
    //var ContactList : MutableList<Contact> = ArrayList()

    // Inisialisasi adapter dan list untuk listView
    lateinit var historyAdapter : ArrayAdapter<String>
    var history  : MutableList<String> = mutableListOf("")

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

        //LoaderManager.getInstance(this).initLoader(1,null,this)
    }



    private lateinit var ContactListdapter : ContactListRecyclerViewAdapter
    private lateinit var interfaceData: InterfaceData
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //hapus "return" ganti dengan val View agar dapat memanggil property dari layout fragment
        val view = inflater.inflate(R.layout.fragment_account_detail, container, false)
        val clientName = view.findViewById<TextView>(R.id.ClientName)
        val current = view.findViewById<TextView>(R.id.CurrentRoomLabel)
        val roomName = view.findViewById<TextView>(R.id.MeetRoomTitle)
        val roomCap = view.findViewById<TextView>(R.id.MeetRoomCap)
        val roomImage = view.findViewById<ImageView>(R.id.MeetRoomImg)
        val bookedRoom = view.findViewById<LinearLayout>(R.id.bookedRoomView)
        // Inisialisasi komponen dari fragment
        val historylistview = view.findViewById<ListView>(R.id.historyList)
        val historyclear = view.findViewById<Button>(R.id.clearHistory)
        val savedroom = view.findViewById<Button>(R.id.savedRoom)
        //val recyclerviewget = view.findViewById<RecyclerView>(R.id.recyclerViewContactList)
        val edituser = view.findViewById<Button>(R.id.editAccBtn)

        //Menerima isi argument
        clientName.text = "Login as ${HomeActivity.current_username}"
        //tambahkan interfacedata dengan aktivitas dari interfaceData
        interfaceData = activity as InterfaceData

        // Inisialisasi SharedPreferences yang telah dibuat dengan filename
        var roomSharedPrefHelper = SharedPrefHelper(context!!, PrefFileName)

        // Get / Ambil data dari file SharedPreferences
        // Mengambil nama room
        var getTitle = roomSharedPrefHelper.name
        // Mengambil kapasitas room
        var getCap = roomSharedPrefHelper.cap
        // Mengambil string image room
        var getImg = roomSharedPrefHelper.image
        // Mengambil waktu awal dan waktu akhir meeting room
        var getBegHour = roomSharedPrefHelper.beginHour
        var getEndHour = roomSharedPrefHelper.endHour
        var getBegMin = roomSharedPrefHelper.beginMin
        var getEndMin = roomSharedPrefHelper.endMin

        // data yang telah diambil akan ditampilkan ke view
        roomName.setText(getTitle)
        roomCap.text = "${getCap} Seats"
        doAsync{
            val imageresult=decodeStrImg(getImg)
            uiThread {
                roomImage.setImageBitmap(imageresult)
            }
        }

        if (getTitle == ""){
            bookedRoom.visibility = View.GONE
            current.text = "0 Room Currently Booked"
        }
        else{
            bookedRoom.visibility = View.VISIBLE
            current.text = "1 Room Currently Booked"
        }

        // Ketika View di klik, data akan dikirimkan ke fungsi interfaceData untuk ditampilkan semua
        bookedRoom.setOnClickListener {
            interfaceData.sendBookedRoomData(getTitle.toString(),getCap.toString().toInt(),
                getImg.toString(),getBegHour.toString().toInt(),getEndHour.toString().toInt(),
                getBegMin.toString().toInt(),getEndMin.toString().toInt())
        }

        // Jika button Clear History diklik
        historyclear.setOnClickListener {
            // Jalankan fungsi delFile
            delFile()
        }

        // Ketika button save diklik, buka fragment untuk room yang telah disave
        savedroom.setOnClickListener {
            interfaceData.openSavedRoomData()
        }

        edituser.setOnClickListener {
            interfaceData.openEditUser(param3!!,param1!!,param2!!)
        }

        history.clear()
        // panggil fungsi untuk membaca file
        readHistoryFile()
        // Buat Array adapter untuk listView dengan layout yang sudah disediakan android studio
        // dan menggunakan object list history
        historyAdapter = ArrayAdapter(context!!,android.R.layout.simple_list_item_1,history)
        historylistview.adapter = historyAdapter

        return view
    }

    // Fungsi untuk read file history
    private fun readHistoryFile() {
        var dec = DecimalFormat("00")
        // Gunakan try catch untuk mengantisipasi file tidak ditemukan atau error
        try{
            // Gunakan openFileInput untuk membaca file History.txt
            var input = requireContext().openFileInput("History.txt").apply {
                // baca setiap line dengan menggunakan bufferedReader dan for loop
                bufferedReader().useLines { // Mengubah menjadi Lines
                    for(text in it.toList()){ // Jadikan list dan dibaca dengan perulangan for loop
                        // Pisah baris dengan ' '
                        var item = text.split(' ')
                        // Masukkan informasi setiap baris ke dalam list history yang sudah dibuat
                        history.add("Room ${item[1]} with duration : ${item[3]} seconds from ${dec.format(item[4].toInt())}:" +
                                "${dec.format(item[6].toInt())} to ${dec.format(item[5].toInt())}:${dec.format(item[7].toInt())}")
                    }
                }
            }
        }catch (e : FileNotFoundException){
            Toast.makeText(context,"No History", Toast.LENGTH_SHORT).show()
        }catch (e : IOException){
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
        }
    }

    // Buat fungsi delFile untuk menghapus file history
    private fun delFile() {
        // Cek apakah file tersedia
        if(requireContext().fileList().size!=0) {
            // Gunakan perulangan for untuk setiap file yang ada dan gunakan .deleteFile(file)
            // untuk menghapus file. *Tambahkan requireContext() untuk penggunaan didalam fragment
            for (i in requireContext().fileList())
                requireContext().deleteFile(i)

            // Bersihkan list history
            history.clear()
            Toast.makeText(context,"History Cleared", Toast.LENGTH_SHORT).show()
            // notif bahwa terjadi perubahan pada adapter
            historyAdapter.notifyDataSetChanged()
        }
        else
            Toast.makeText(context,"No History", Toast.LENGTH_SHORT).show()
    }

    fun decodeStrImg (param3:String?): Bitmap? {
        val imageBytes = Base64.decode(param3, Base64.DEFAULT)
        val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return decodeImage
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: Int) =
                AccountDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_CLIENT_NAME, param1)
                        putString(ARG_CLIENT_PASSWORD, param2)
                        putInt(ARG_CLIENT_ID, param3)
                    }
                }
    }

    /*override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var ContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        var Projection = arrayOf(DisplayName,Number)
        return CursorLoader(activity!!,ContactUri,Projection,null,null,"$DisplayName ASC")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        ContactList.clear()
        if(data != null){
            data.moveToFirst()
            while(!data.isAfterLast){
                ContactList.add(Contact(
                    data.getString(data.getColumnIndex(DisplayName)),
                    data.getString(data.getColumnIndex(Number))
                        //,data.getInt(data.getColumnIndex(Image))
                )
                )
                data.moveToNext()
            }
            ContactListdapter = ContactListRecyclerViewAdapter(ContactList)
            recyclerViewContactList.adapter = ContactListdapter
            recyclerViewContactList.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        recyclerViewContactList?.adapter?.notifyDataSetChanged()
    }*/
}