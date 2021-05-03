package com.example.meetbook.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.meetbook.ARG_GET_ROOM_CAP
import com.example.meetbook.ARG_GET_ROOM_IMAGE
import com.example.meetbook.ARG_GET_ROOM_TITLE
import com.example.meetbook.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM5 = "param5"
private const val ARG_PARAM6 = "param6"
private const val ARG_PARAM7 = "param7"

/**
 * A simple [Fragment] subclass.
 * Use the [BookedRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookedRoomFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: Int? = null
    private var param3: String? = null
    private var param4: Int? = null
    private var param5: Int? = null
    private var param6: Int? = null
    private var param7: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
            param4 = it.getInt(ARG_PARAM4)
            param5 = it.getInt(ARG_PARAM5)
            param6 = it.getInt(ARG_PARAM6)
            param7 = it.getInt(ARG_PARAM7)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_booked_room, container, false)

        val roomName = view.findViewById<TextView>(R.id.BookMeetRoomTitle)
        val roomCap = view.findViewById<TextView>(R.id.BookMeetRoomCap)
        val roomImage = view.findViewById<ImageView>(R.id.BookMeetRoomImg)
        val begintime = view.findViewById<TextView>(R.id.StartTime)
        val endtime = view.findViewById<TextView>(R.id.EndTime)

        roomName.text = param1
        roomCap.text = "${param2.toString()} Seats"
        doAsync{
            val imageresult=decodeStrImg(param3)
            uiThread {
                roomImage.setImageBitmap(imageresult)
            }
        }
        begintime.text = "${param4.toString()}:${param6.toString()}"
        endtime.text = "${param5.toString()}:${param7.toString()}"
        return view
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
         * @return A new instance of fragment BookedRoomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Int, param3: String, param4: Int, param5: Int, param6: Int, param7: Int) =
            BookedRoomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1) //mengirim bundle yang berisi param1, param2, param3 ke argument
                    putInt(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                    putInt(ARG_PARAM4, param4)
                    putInt(ARG_PARAM5, param5)
                    putInt(ARG_PARAM6, param6)
                    putInt(ARG_PARAM7, param7)
                }
            }
    }
}