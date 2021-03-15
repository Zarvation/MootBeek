package com.example.meetbook.fragments

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val roomtitle = view.findViewById<TextView>(R.id.BookMeetRoomTitle)
        val roomcap = view.findViewById<TextView>(R.id.BookMeetRoomCap)
        val roomimage = view.findViewById<ImageView>(R.id.BookMeetRoomImg)
        return view
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
                        putString(ARG_GET_ROOM_TITLE, param1)
                        putInt(ARG_GET_ROOM_CAP, param2)
                        putString(ARG_GET_ROOM_IMAGE, param3)
                    }
                }
    }
}