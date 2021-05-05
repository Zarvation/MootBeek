package com.example.meetbook.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.meetbook.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

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
            mediaPlayerIntentService = Intent(activity,MeetingSongService::class.java)
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

        roomName.text = paramTitle
        roomCap.text = "${paramCap.toString()} Seats"
        doAsync{
            val imageresult=decodeStrImg(paramImage)
            uiThread {
                roomImage.setImageBitmap(imageresult)
            }
        }
        begintime.text = "${paramSH.toString()}:${paramSM.toString()}"
        endtime.text = "${paramEH.toString()}:${paramEM.toString()}"

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
    fun decodeStrImg (param3:String?): Bitmap? {
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
        fun newInstance(param1: String, param2: Int, param3: String, param4: Int, param5: Int, param6: Int, param7: Int) =
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