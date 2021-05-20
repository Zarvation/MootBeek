package com.example.meetbook.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.meetbook.R
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedMeetingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedMeetingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    // Inisialisasi adapter dan list untuk room yang disave
    var saved  : MutableList<String> = mutableListOf("")
    lateinit var savedAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_meeting, container, false)

        val roomlist = view.findViewById<ListView>(R.id.savedList)

        saved.clear()
        // jalankan fungsi readSavedRoomFile()
        if (isExternalStorageReadable()){
            readSavedRoomFile()
        }
        // Buat Array adapter untuk listView dengan layout yang sudah disediakan android studio
        // dan menggunakan object list saved
        savedAdapter = ArrayAdapter(context!!,android.R.layout.simple_list_item_1,saved)
        roomlist.adapter = savedAdapter

        return view
    }

    // Buat fungsi readSavedRoomFile untuk membaca isi file room yang di save
    private fun readSavedRoomFile() {
        var dec = DecimalFormat("00")
        // Gunakan File dan akses directory file external (DIRECTORY DOCUMENTS) dengan getExternalFilesDir
        var myDir = File(requireContext().getExternalFilesDir("Folder")?.toURI())
        // Gunakan try catch untuk mengantisipasi file tidak ditemukan atau error
        try{
            // dengan menggunakan File(), akses file SavedMeeting.txt pada directory yang telah dibuat
            // dan untuk setiap baris yang dibaca,
            File(myDir,"SavedMeeting.txt").forEachLine(Charsets.UTF_8) {
                // pisahkan text setiap baris dengan ' '
                var item = it.split(' ')
                // dan tambahkan informasi ke dalam list saved yang sudah dibuat
                saved.add("Title : ${item[0]}\nRoom ${item[2]} from ${dec.format(item[4].toInt())}:" +
                        "${dec.format(item[6].toInt())} to ${dec.format(item[5].toInt())}:${dec.format(item[7].toInt())}")
        }
        }catch (e : FileNotFoundException){
            Toast.makeText(context,"No Saved Rooms", Toast.LENGTH_SHORT).show()
        }catch (e : IOException){
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
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
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    5312) // Tambahkan request code
        }

        // Setelah permission di dapatkan
        // lakukan pengecekan state/status apakah external storage telah terpasang atau tidak
        var state = Environment.getExternalStorageState()
        // jika state == MEDIA_MOUNTED atau MEDIA_MOUNTED_READ_ONLY, maka external storage sudah bisa digunakan
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SavedMeetingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SavedMeetingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}