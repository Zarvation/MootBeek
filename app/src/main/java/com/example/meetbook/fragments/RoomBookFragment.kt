package com.example.meetbook.fragments

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentActivity
import com.example.meetbook.*
import com.example.meetbook.TimePicker
import kotlinx.android.synthetic.main.fragment_room_book.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.sql.Time
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RoomBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoomBookFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: Int? = null
    private var param3: String? = null
    //inisialisasi notification manager, notification channel,
    //dan builder untuk membuat dan menjalankan notifikasi
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    //buat channelID
    private val channelId = "com.example.meetbook345"
    private val description = "Finished Notification"

    //Pengirim dengan Getter dan Setter
        /*
    var title : String = ""
        get() = field.toString()
        set(value){
            if (value === "")
                field = "Kosong"
            else
                field = value
        }
    var cap : Int = 0
        get() = field.toInt()
        set(value){
            if (value === 0)
                field = 0
            else
                field = value
        }

         */

    //Broadcast
    private val meetingProgressReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            //Notifikasi akan bekerja sebagai service, sehingga dapat dijalankan
            //walaupun aplikasi dalam keadaan tertutup
            notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //Mengambil persen durasi
            var meeting_percentage = intent?.getIntExtra(EXTRA_MEETING_PERCENTAGE, 0)
            //mengambil status selesai
            var meeting_finish = intent?.getBooleanExtra(EXTRA_MEETING_FINISH, true)

            //update progressbar
            MeetingProgress.progress = meeting_percentage ?: 0

            //Jika selesai, jalankan toast
            if (meeting_finish!!){
                Toast.makeText(activity, "Meeting Finished", Toast.LENGTH_SHORT).show()

                //Mengakses layout untuk custom notification
                val contentView = RemoteViews(context!!.packageName, R.layout.meeting_notification_finish_layout)
                //Set title dan content yang ada di layout custom notification
                contentView.setTextViewText(R.id.tv_title, "Meeting Finished")
                contentView.setTextViewText(R.id.tv_content, "You have reached time limit !!!")
                //Melakukan pengecekan versi diatas oreo, untuk membentuk notification
                //channel berdasarkan channelId
                //sehingga setiap notifikasi dapat dibedakan satu dengan yang lain
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //Buat notification Channel
                    notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.BLUE
                    notificationChannel.enableVibration(true)
                    notificationChannel.setShowBadge(true)
                    notificationManager.createNotificationChannel(notificationChannel)

                    //Buat notifikasi dengan memasukkan custom layout yang telah dibuat
                    builder = Notification.Builder(activity, channelId)
                            .setContent(contentView) //set notifikasi dengan custom layout
                            .setSmallIcon(R.drawable.meetroom1)
                }else{ //Bila dibawah versi oreo
                    builder = Notification.Builder(activity)
                            .setContent(contentView) //set notifikasi dengan custom layout
                            .setSmallIcon(R.drawable.meetroom1)
                }
                //Tampilkan notifikasi
                notificationManager?.notify(EXTRA_NOTIFICATION_MEETING,builder.build())
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Menerima argument
        arguments?.let {
            param1 = it.getString(ARG_GET_ROOM_TITLE)
            param2 = it.getInt(ARG_GET_ROOM_CAP)
            param3 = it.getString(ARG_GET_ROOM_IMAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_book, container, false)

        //Inisialisasi komponen atau property dari fragment
        val roomtitle = view.findViewById<TextView>(R.id.BookMeetRoomTitle)
        val roomcap = view.findViewById<TextView>(R.id.BookMeetRoomCap)
        val roomimage = view.findViewById<ImageView>(R.id.BookMeetRoomImg)
        val startTimePick = view.findViewById<TextView>(R.id.StartTime)
        val endTimePick = view.findViewById<TextView>(R.id.EndTime)
        val buttonBook = view.findViewById<Button>(R.id.BookBtn)

        roomtitle.text = param1
        roomcap.text = "${param2.toString()} Seats"

        //Convert Base64 String into Image
        /*val imageBytes = Base64.decode(param3, Base64.DEFAULT)
        val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        roomimage.setImageBitmap(decodeImage)*/

        //Proses AsyncTask untuk load gambar
        doAsync{
            val imageresult=decodeStrImg(param3)
            uiThread {
                roomimage.setImageBitmap(imageresult)
            }
        }
        /*val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            startTimePick.text = SimpleDateFormat("HH:mm").format(cal.time)
        }*/

        //Menentukan waktu mulai meeting
        startTimePick.setOnClickListener {
            //TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            var timePicker = TimePicker("start")
            timePicker.show(this.childFragmentManager, "time")
        }

        //Menentukan waktu selesai Meeting
        endTimePick.setOnClickListener {
            //TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            var timePicker = TimePicker("end")
            timePicker.show(this.childFragmentManager, "time")
        }

        //Menjalankan Service dengan Intent
        var MeetingService = Intent(activity, MeetingProgressService::class.java)

        //Button Book diklik, jalankan Intent
        buttonBook.setOnClickListener {
            //Menghitung durasi meeting
            var beginHour = startTimePick.text.substring(0,2).toInt()
            var beginMin = startTimePick.text.substring(3,5).toInt()
            var endHour = endTimePick.text.substring(0,2).toInt()
            var endMin = endTimePick.text.substring(3,5).toInt()
            var duration: Long = ((endHour*3600 + endMin*60) - (beginHour*3600 + beginMin*60)).toLong()

            //Kirimkan durasi ke service
            MeetingService.putExtra(EXTRA_MEETING_DURATION,duration)
            Toast.makeText(activity, "Meeting Started", Toast.LENGTH_SHORT).show()

            //Jalankan intent dengan enqueueWork
            MeetingProgressService.enqueueWork(activity!!, MeetingService)
        }
        //Menangkap broadcast untuk diproses dengan menentukan filter (ketika broadcast melewati Action_meeting) dan receivernya
        var filterMeetingProgress = IntentFilter(ACTION_MEETING)
        requireActivity().registerReceiver(meetingProgressReceiver, filterMeetingProgress)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        //Lakukan unregister agar proses yang dilakukan tidak memakan banyak memory
        requireActivity().unregisterReceiver(meetingProgressReceiver)
    }
    //Function to Convert String into Image (bitmap)
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
         * @return A new instance of fragment RoomBookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Int, param3: String) =
                RoomBookFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_GET_ROOM_TITLE, param1) //mengirim bundle yang berisi param1, param2, param3 ke argument
                        putInt(ARG_GET_ROOM_CAP, param2)
                        putString(ARG_GET_ROOM_IMAGE, param3)
                    }
                }
    }
}