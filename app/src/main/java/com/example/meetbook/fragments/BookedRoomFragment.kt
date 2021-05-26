package com.example.meetbook.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.example.meetbook.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.text.DecimalFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TITLE = "param1"
private const val ARG_CAP = "param2"
private const val ARG_IMAGE = "param3"
private const val ARG_SH = "param4"
private const val ARG_EH = "param5"
private const val ARG_SM = "param6"
private const val ARG_EM = "param7"

/**
 * A simple [Fragment] subclass.
 * Use the [BookedRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookedRoomFragment : Fragment() {
    // Inisialisasi dan sediakan variable untuk menampung data room
    private var paramTitle: String? = null
    private var paramCap: Int? = null
    private var paramImage: String? = null
    private var paramSH: Int? = null
    private var paramEH: Int? = null
    private var paramSM: Int? = null
    private var paramEM: Int? = null

    // Inisialisasi variable untuk service media player
    private var mediaPlayerIntentService : Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Simpan data ke variable yang telah di sediakan
        arguments?.let {
            paramTitle = it.getString(ARG_TITLE)
            paramCap = it.getInt(ARG_CAP)
            paramImage = it.getString(ARG_IMAGE)
            paramSH = it.getInt(ARG_SH)
            paramEH = it.getInt(ARG_EH)
            paramSM = it.getInt(ARG_SM)
            paramEM = it.getInt(ARG_EM)
        }

        // Mulai dan create media player
        if (mediaPlayerIntentService == null){
            // Buat intent untuk service media player
            mediaPlayerIntentService = Intent(activity, MeetingSongService::class.java)
            // Kirimkan aksi untuk membuat media player dan jalankan service
            mediaPlayerIntentService?.setAction(ACTION_CREATE)
            getActivity()?.startService(mediaPlayerIntentService)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_booked_room, container, false)

        // Tampilkan data
        val roomName = view.findViewById<TextView>(R.id.BookMeetRoomTitle)
        val roomCap = view.findViewById<TextView>(R.id.BookMeetRoomCap)
        val roomImage = view.findViewById<ImageView>(R.id.BookMeetRoomImg)
        val begintime = view.findViewById<TextView>(R.id.StartTime)
        val endtime = view.findViewById<TextView>(R.id.EndTime)
        val buttonStart = view.findViewById<Button>(R.id.mediaPlayerSTART)
        val buttonStop = view.findViewById<Button>(R.id.mediaPlayerSTOP)

        // Inisialisasi komponen button save
        val buttonSave = view.findViewById<Button>(R.id.saveMeeting)

        roomName.text = paramTitle
        roomCap.text = "${paramCap.toString()} Seats"
        doAsync{
            val imageresult=decodeStrImg(paramImage)
            uiThread {
                roomImage.setImageBitmap(imageresult)
            }
        }
        var dec = DecimalFormat("00")
        begintime.text = "${dec.format(paramSH)}:${dec.format(paramSM)}"
        endtime.text = "${dec.format(paramEH)}:${dec.format(paramEM)}"

        // Ketika button save diklik, buat dialog untuk save atau cek space
        buttonSave.setOnClickListener {
            var BuilderDialog = AlertDialog.Builder(context!!)
            // Gunakan layout save_meeting.xml untuk dialog
            var inflaterDialog = layoutInflater.inflate(R.layout.save_meeting, null)
            BuilderDialog.setView(inflaterDialog)
            // Jika button Save diklik
            BuilderDialog.setPositiveButton("Save"){ dialogInterface: DialogInterface, i: Int ->
                // ambil isi text yang ada di editText layout dan mulai simpan file External
                var getTitle = inflaterDialog.findViewById<EditText>(R.id.meetingTitle)
                // Sebelum menyimpan, cek permission untuk mengakses external storage
                if(isExternalStorageReadable()){
                    // Jika izin diberikan, jalankan fungsi untuk menyimpan file
                    saveFileExternal(getTitle.text.toString())
                }
            }
            BuilderDialog.setNeutralButton("Check Storage"){ dialogInterface: DialogInterface, i: Int ->
                if(isExternalStorageReadable()){
                    checkStorage()
                }
            }
            BuilderDialog.create().show()
        }

        // Ketika Button Play di klik
        buttonStart.setOnClickListener {
            // Jika button tertulis PLAY
            if (buttonStart.text.toString().toUpperCase().equals("PLAY SONG")){
                // Ubah text button menjadi Pause
                buttonStart.text = "Pause Song"
                // Kirimkan aksi untuk memulai media player dan jalankan service
                mediaPlayerIntentService?.setAction(ACTION_PLAY)
                getActivity()?.startService(mediaPlayerIntentService)
            }
            // Jika button tertulis RESUME
            else if (buttonStart.text.toString().toUpperCase().equals("PAUSE SONG")){
                // Ubah text button menjadi Resume
                buttonStart.text = "Resume Song"
                // Kirimkan aksi untuk menghentikan sebentar media player dan jalankan service
                mediaPlayerIntentService?.setAction(ACTION_PAUSE)
                getActivity()?.startService(mediaPlayerIntentService)
            }
            // Jika button tertulis RESUME
            else if (buttonStart.text.toString().toUpperCase().equals("RESUME SONG")){
                // Ubah text button menjadi Pause
                buttonStart.text = "Pause Song"
                // Kirimkan aksi untuk melanjutkan media player dan jalankan service
                mediaPlayerIntentService?.setAction(ACTION_RESUME)
                getActivity()?.startService(mediaPlayerIntentService)
            }
        }
        // Ketika button Stop diklik
        buttonStop.setOnClickListener {
            // Ubah kembali text button menjadi Play
            buttonStart.text = "Play Song"
            // Kirimkan aksi untuk menghentikan media player dan jalankan service
            mediaPlayerIntentService?.setAction(ACTION_STOP)
            getActivity()?.startService(mediaPlayerIntentService)
        }

        return view
    }

    // Buat fungsi untuk menyimpan file external
    private fun saveFileExternal(title: String) {
        // Gunakan File dan akses directory file external (DIRECTORY FOLDER) dengan getExternalFilesDir
        var myDir = File(requireContext().getExternalFilesDir("Folder")?.toURI())
        if(!myDir.exists()){ // Jika directory belum ada, gunakan mkdir untuk membuat directory Folder
            myDir.mkdir()
        }
        // dengan menggunakan File(), akses file SavedMeeting.txt pada directory yang telah dibuat
        // dan apply fungsi "appendText" untuk menambahkan text ke dalam file
        File(myDir, "SavedMeeting.txt").apply {
            appendText("${title} ${paramTitle} ${paramCap} ${paramSH} ${paramEH} ${paramSM} ${paramEM}\n")
        }
        Toast.makeText(context!!, "Meeting Saved", Toast.LENGTH_SHORT).show()
    }

    private fun externalMemoryAvailable() : Boolean {
        return android.os.Environment.getExternalStorageState().equals(
            android.os.Environment.MEDIA_MOUNTED)
    }

    // Buat fungsi untuk cek space storage
    private fun checkStorage() {
        /*if (externalMemoryAvailable()) {
            val path = Environment.getExternalStorageState()
            val stat = StatFs(path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            Toast.makeText(
                context!!,
                "External Storage's Space available : ${availableBlocks / 1048576} MB",
                Toast.LENGTH_SHORT
            ).show()
        }*/
        val NUM_BYTES_NEEDED_FOR_MY_APP = 1024 * 1024 * 10L
        val storageManager = requireContext().applicationContext.getSystemService<StorageManager>()!!
        val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(requireContext().filesDir)
        val availableBytes: Long =
                storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
        if (availableBytes < NUM_BYTES_NEEDED_FOR_MY_APP) {
            Toast.makeText(context!!, "External Storage's Space < 10MB", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                context!!,
                "External Storage's Space available : ${availableBytes / 1048576} MB",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Buat fungsi untuk mendapat permission
    fun isExternalStorageReadable(): Boolean{
        // gunakan checkSelfPermission untuk mengecek permission apa yang sudah diberikan
        if(ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED){ // Cek apakah izin external storage diberikan
                    // Jika belum, request permission untuk Read dan Write external storage (Write saja)
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                5312
            ) // Tambahkan request code
        }

        // Setelah permission di dapatkan
        // lakukan pengecekan state/status apakah external storage telah terpasang atau tidak
        var state = Environment.getExternalStorageState()
        // jika state == MEDIA_MOUNTED atau MEDIA_MOUNTED_READ_ONLY, maka external storage sudah bisa digunakan
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(
                state
            ))
        {
            return true
        }
        return false
    }

    // Buat fungsi untuk hasil request permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            5312 -> { // Jika request code sesuai dan izin didapatkan, tampilkan toast
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(context!!, "Access Given", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun decodeStrImg(param3: String?): Bitmap? {
        val imageBytes = Base64.decode(param3, Base64.DEFAULT)
        val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return decodeImage
    }

    // stop service untuk menghemat memori jika sudah tidak digunakan
    override fun onDestroy() {
        super.onDestroy()
        getActivity()?.stopService(mediaPlayerIntentService)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookedRoomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(
            param1: String,
            param2: Int,
            param3: String,
            param4: Int,
            param5: Int,
            param6: Int,
            param7: Int
        ) =
            BookedRoomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, param1) //mengirim bundle yang berisi data room yang telah di book ke argument
                    putInt(ARG_CAP, param2)
                    putString(ARG_IMAGE, param3)
                    putInt(ARG_SH, param4)
                    putInt(ARG_EH, param5)
                    putInt(ARG_SM, param6)
                    putInt(ARG_EM, param7)
                }
            }
    }
}