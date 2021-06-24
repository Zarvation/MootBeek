package com.example.meetbook.fragments

import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.meetbook.*
import com.example.meetbook.HomeActivity.Companion.current_password
import com.example.meetbook.HomeActivity.Companion.current_username
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
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
    // Inisialisasi Filename untuk SharedPreferences Remove Ads
    private val AdsPrefFileName = "ADSREMOVEFILE001"
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
            param1 = it.getString(ARG_CLIENT_NAME)
            param2 = it.getString(ARG_CLIENT_PASSWORD)
            param3 = it.getInt(ARG_CLIENT_ID)
        }

        /*var db= Room.databaseBuilder(
                context!!,
                RoomUserDBHelper::class.java,
                "meetbookDb.db"
        ).build()

        doAsync {
            for (allData in db.userDao().getUser(param3!!)){
                param1 = allData.username
                param2 = allData.password
            }
        }*/

        //LoaderManager.getInstance(this).initLoader(1,null,this)
    }



    private lateinit var ContactListdapter : ContactListRecyclerViewAdapter
    private lateinit var interfaceData: InterfaceData
    // Inisialisasi Rewarded Ad yang akan digunakan
    private var mRewardAd : RewardedAd? = null
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
        val removeadsbtn = view.findViewById<ImageButton>(R.id.removeAdsBtn)

        // Inisialisasi pembentukan Mobile Ads
        MobileAds.initialize(activity){}
        // Hilangkan tampilan button, bila ads belum diload
        removeadsbtn.visibility = View.GONE

        // Build Ad Request dan Load Rewarded Ad dengan menggunakan key untuk test ad
        RewardedAd.load(context, "ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback(){
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Toast.makeText(context,"No Internet", Toast.LENGTH_SHORT).show()
                    super.onAdFailedToLoad(p0)
                    // Jika Ads gagal diload, kosongkan rewarded ad
                    mRewardAd = null
                }
                // Jika berhasil di load, munculkan Ads ke rewarded ad dan munculkan button yang disembunyikan tadi
                override fun onAdLoaded(p0: RewardedAd) {
                    super.onAdLoaded(p0)
                    mRewardAd = p0
                    removeadsbtn.visibility = View.VISIBLE
                }
            })

        //Menerima isi argument
        clientName.text = "Login as ${HomeActivity.current_username}"

        //tambahkan interfacedata dengan aktivitas dari interfaceData
        interfaceData = activity as InterfaceData

        // Inisialisasi SharedPreferences yang telah dibuat dengan filename
        var roomSharedPrefHelper = SharedPrefHelper(context!!, PrefFileName)

        // Get / Ambil data dari file SharedPreferences
        var getId = roomSharedPrefHelper.Id
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
            interfaceData.sendBookedRoomData(getId.toString().toInt(),getTitle.toString(),getCap.toString().toInt(),
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
            interfaceData.openEditUser(param3!!,current_username, current_password)
        }

        history.clear()
        // panggil fungsi untuk membaca file
        readHistoryFile()
        // Buat Array adapter untuk listView dengan layout yang sudah disediakan android studio
        // dan menggunakan object list history
        historyAdapter = ArrayAdapter(context!!,android.R.layout.simple_list_item_1,history)
        historylistview.adapter = historyAdapter

        // Jika button remove ads diklik, munculkan dialog yang mengindikasikan jumlah ads yang perlu ditonton
        // untuk menghilangkan ads
        removeadsbtn.setOnClickListener {
            // Panggil shared pref untuk remove ads
            var removeAdsPrefHelper = AdsPrefHelper(context!!, AdsPrefFileName)
            // ambil jumlah ads yang perlu ditonton saat ini
            var watch_time = removeAdsPrefHelper.watchTimes
            var BuilderDialog = AlertDialog.Builder(context!!)
            // Gunakan layout dialog_remove_ads.xml untuk dialog
            var inflaterDialog = layoutInflater.inflate(R.layout.dialog_remove_ads, null)
            BuilderDialog.setView(inflaterDialog)
            var getText = inflaterDialog.findViewById<TextView>(R.id.removeadsTxt)
            var getProgress = inflaterDialog.findViewById<ProgressBar>(R.id.removeadsProg)

            // Set text jumlah video yang perlu ditonton
            getText.setText("$watch_time more videos to Remove Ads")
            // Set progress bar untuk progress tontonan saat ini
            getProgress.max = 5
            getProgress.progress += 5-watch_time

            // Jika button Watch Ad diklik
            BuilderDialog.setPositiveButton("Watch Ads"){ dialogInterface: DialogInterface, i: Int ->
                // Bila watch ad masih lebih besar dari 0, jalankan fungsi rewarded ad
                if (watch_time > 0) {
                    showRewardVideo()
                }
                // hilangkan kembali removeads button
                removeadsbtn.visibility = View.GONE
            }
            BuilderDialog.create().show()
        }

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

    // Buat fungsi menampilkan rewarded ads video
    private fun showRewardVideo() {
        // panggil sharedpref remove ads
        var removeAdsPrefHelper = AdsPrefHelper(context!!, AdsPrefFileName)
        // ambil jumlah video yang perlu ditonton saat ini
        var current = removeAdsPrefHelper.watchTimes
        // Jika video ada
        if(mRewardAd!=null){
            // tampilkan video, dan setelah video ditonton, set jumlah video yang perlu ditonton menjadi dikurangi 1 dari sebelumnya
            mRewardAd?.show(this.activity, OnUserEarnedRewardListener() {
                removeAdsPrefHelper.watchTimes = current - 1
                Toast.makeText(this.activity,"Thanks for Watching Ads!", Toast.LENGTH_SHORT).show()
            })
        }
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