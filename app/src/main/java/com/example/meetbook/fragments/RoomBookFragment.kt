package com.example.meetbook.fragments

import android.app.*
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.media.AudioAttributes
import android.media.Image
import android.media.RingtoneManager
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
import com.example.meetbook.MeetingDuration.Companion.quote
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
    //deklarasi channelID
    lateinit var channelId: String

    var mAlarmManager : AlarmManager? = null
    var mPendingIntent : PendingIntent? = null


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
    /*private val meetingProgressReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
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

                val intentBack = Intent(activity, HomeActivity::class.java)
                val pendingIntentBack: PendingIntent = PendingIntent.getActivity(activity, 0, intentBack, 0)

                //Melakukan pengecekan versi diatas oreo, untuk build notification channel
                //berdasarkan channelId
                //sehingga setiap notifikasi dapat dibedakan satu dengan yang lain
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //Set channelID untuk meeting selesai
                    channelId = ID_CHANNEL_FINISH
                    //Buat notifikasi dengan memasukkan custom layout yang telah dibuat
                    builder = Notification.Builder(activity, channelId)
                            .setContent(contentView) //set notifikasi dengan custom layout
                            .setSmallIcon(R.drawable.meetroom1)
                            .setGroup(ID_GROUP_FINISH) //Set group untuk notifikasi,
                                                    //dalam hal ini, meeting finish
                            .setContentIntent(pendingIntentBack)
                }else{ //Bila dibawah versi oreo
                    builder = Notification.Builder(activity)
                            .setContent(contentView) //set notifikasi dengan custom layout
                            .setSmallIcon(R.drawable.meetroom1)
                            .setGroup(ID_GROUP_FINISH)
                }
                //Tampilkan notifikasi
                notificationManager?.notify(EXTRA_NOTIFICATION_MEETING,builder.build())
            }
        }

    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Menerima argument
        arguments?.let {
            param1 = it.getString(ARG_GET_ROOM_TITLE)
            param2 = it.getInt(ARG_GET_ROOM_CAP)
            param3 = it.getString(ARG_GET_ROOM_IMAGE)
        }

        //Notifikasi akan bekerja sebagai service, sehingga dapat dijalankan
        //walaupun aplikasi dalam keadaan tertutup
        //notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE)
                //as NotificationManager
        //Mulai fungsi createNotificationGroup dan createNotificationChannels
        //createNotificationGroup()
        //createNotificationChannels()
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
        val quotesText = view.findViewById<TextView>(R.id.Quotes)

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
        //var MeetingService = Intent(activity, MeetingProgressService::class.java)

        mAlarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //Button Book diklik, jalankan Intent
        buttonBook.setOnClickListener {
            //Menghitung durasi meeting
            var beginHour = startTimePick.text.substring(0,2).toInt()
            var beginMin = startTimePick.text.substring(3,5).toInt()
            var endHour = endTimePick.text.substring(0,2).toInt()
            var endMin = endTimePick.text.substring(3,5).toInt()

            val sdf = SimpleDateFormat("hh:mm")
            val currentTime = sdf.format(android.icu.util.Calendar.getInstance().time)
            var timeNow = currentTime.split(":")

            if(mPendingIntent != null) {
                mAlarmManager?.cancel(mPendingIntent)
                mPendingIntent?.cancel()
            }

            var duration: Long = ((endHour*3600 + endMin*60) - (timeNow[0].toInt()*3600 + timeNow[1].toInt()*60)).toLong()
            var jam = beginHour-timeNow[0].toInt()
            var menit = beginMin-timeNow[1].toInt()
            //var jamSelesai = endHour-timeNow[0].toInt()
            //var menitSelesai = endMin-timeNow[1].toInt()

            var alarmTimer = Calendar.getInstance()
            alarmTimer.add(Calendar.HOUR_OF_DAY, jam)
            alarmTimer.add(Calendar.MINUTE, menit)
            alarmTimer.add(Calendar.SECOND, 0)

            var sendIntent = Intent(activity, MeetingAlarmReceiver::class.java)
            //sendIntent.putExtra(EXTRA_MEETING_DURATION, duration)

            mPendingIntent = PendingIntent.getBroadcast(activity, 123, sendIntent, 0)

            mAlarmManager?.set(AlarmManager.RTC, alarmTimer.timeInMillis, mPendingIntent)
            //mAlarmManager?.setInexactRepeating(AlarmManager.RTC, alarmTimer.timeInMillis,AlarmManager.INTERVAL_FIFTEEN_MINUTES, mPendingIntent)
            Toast.makeText(this.activity,"alarm made $duration",Toast.LENGTH_SHORT).show()

            var serviceComponent = ComponentName(context!!,MeetingDuration::class.java)
            var mJobInfo = JobInfo.Builder(123, serviceComponent)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setMinimumLatency(duration * 1000)
                    .setOverrideDeadline(duration * 1000)
            var JobMeeting = context!!.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            JobMeeting.schedule(mJobInfo.build())
            //Log.w("Job", "Mulai JOB")
            Toast.makeText(context,"Job Service Berjalan", Toast.LENGTH_SHORT).show()

            quotesText.text = "Do The Best"

            //Kirimkan durasi ke service
            //MeetingService.putExtra(EXTRA_MEETING_DURATION,duration)
            //Toast.makeText(activity, "Meeting Started", Toast.LENGTH_SHORT).show()

            //Melakukan pengecekan versi diatas oreo, untuk build notification channel
            //berdasarkan channelId
            //sehingga setiap notifikasi dapat dibedakan satu dengan yang lain
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //Set ChannelID untuk meeting start
                channelId = ID_CHANNEL_START
                val intent = Intent(this.activity, HomeActivity::class.java)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(this.activity, 0, intent, 0)

                //Buat notifikasi dengan memasukkan title, content dan icon
                builder = Notification.Builder(activity, channelId)
                        .setContentTitle("Meeting Started")
                        .setContentText("Attend the meeting !!!")
                        .setShowWhen(true)
                        .setGroup(ID_GROUP_START) //Set group meeting start
                        .addAction(0,"ATTEND",pendingIntent)
                        .setSmallIcon(R.drawable.alarmclock)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.alarmclock))
            }else{ //Bila dibawah versi oreo
                builder = Notification.Builder(activity)
                        .setContentTitle("Meeting Started")
                        .setContentText("Attend the meeting !!!")
                        .setShowWhen(true)
                        .setGroup(ID_GROUP_START) //Set group meeting start
                        .setSmallIcon(R.drawable.alarmclock)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.alarmclock))
            }
            //Tampilkan notifikasi
            notificationManager?.notify(EXTRA_NOTIFICATION_MEETING,builder.build())

            //Jalankan intent dengan enqueueWork
            MeetingProgressService.enqueueWork(activity!!, MeetingService)*/
        }
        //Menangkap broadcast untuk diproses dengan menentukan filter (ketika broadcast melewati Action_meeting) dan receivernya
        //var filterMeetingProgress = IntentFilter(ACTION_MEETING)
        //requireActivity().registerReceiver(meetingProgressReceiver, filterMeetingProgress)

        return view
    }

    /*override fun onDestroy() {
        super.onDestroy()
        //Lakukan unregister agar proses yang dilakukan tidak memakan banyak memory
        requireActivity().unregisterReceiver(meetingProgressReceiver)
    }*/
    //Function to Convert String into Image (bitmap)
    fun decodeStrImg (param3:String?): Bitmap? {
        val imageBytes = Base64.decode(param3, Base64.DEFAULT)
        val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return decodeImage
    }

    private fun createNotificationGroup() {
        //memastikan android adalah android Oreo dan mendaftarkan
        //2 grup berbeda untuk notification
        //jika ada grup berbeda, anda dapat menambahkan list
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val list = mutableListOf<NotificationChannelGroup>()
            list.add(NotificationChannelGroup(ID_GROUP_START,
                    "Start"))
            list.add(NotificationChannelGroup(ID_GROUP_FINISH,
                    "Finish"))
            notificationManager!!.createNotificationChannelGroups(list)
        }
    }
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Set ringtone dan audio
            val notificationSound = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val att = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()

            //Membuat notification channel untuk meeting start
            val ChannelStartMeeting = NotificationChannel(
                    ID_CHANNEL_START,
                    "Start",
                    NotificationManager.IMPORTANCE_HIGH)
            ChannelStartMeeting.group = ID_GROUP_START
            ChannelStartMeeting.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
            ChannelStartMeeting.setSound(notificationSound, att);
            ChannelStartMeeting.setLightColor(Color.BLUE);
            ChannelStartMeeting.enableLights(true)
            ChannelStartMeeting.enableVibration(true)
            ChannelStartMeeting.setShowBadge(true)

            //Membuat notification channel untuk meeting finish
            val ChannelFinishMeeting = NotificationChannel(
                    ID_CHANNEL_FINISH,
                    "Finish",
                    NotificationManager.IMPORTANCE_HIGH)
            ChannelFinishMeeting.enableLights(true)
            ChannelFinishMeeting.enableVibration(true)
            ChannelFinishMeeting.lightColor = Color.RED
            ChannelFinishMeeting.group = ID_GROUP_FINISH
            ChannelFinishMeeting.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)

            notificationManager?.createNotificationChannel(ChannelStartMeeting)
            notificationManager?.createNotificationChannel(ChannelFinishMeeting)

        }
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