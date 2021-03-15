package com.example.meetbook.fragments

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.meetbook.ARG_CLIENT_NAME
import com.example.meetbook.R
import kotlinx.android.synthetic.main.fragment_room_list.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var interfaceData: InterfaceData
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)

        val meettitle = view.findViewById<TextView>(R.id.MeetRoomTitle)
        val meetcap = view.findViewById<TextView>(R.id.MeetRoomCap)
        val meetimage = view.findViewById<ImageView>(R.id.MeetRoomImg)
        val clientStatus = view.findViewById<TextView>(R.id.ClientStatus)
        var usernameClient = arguments?.getString(ARG_CLIENT_NAME)
        clientStatus.text = "Login as $usernameClient"

        interfaceData = activity as InterfaceData

        //Left Here--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        RoomItem.setOnClickListener {
            interfaceData.sendRoomData(meettitle.text.toString(),meetcap.text.substring(0,1).toInt(),meetimage.toString())
        }
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
        fun newInstance(param1: String) =
                RoomListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_CLIENT_NAME, param1)
                        //putString(ARG_PARAM2, param2)
                    }
                }
    }
}