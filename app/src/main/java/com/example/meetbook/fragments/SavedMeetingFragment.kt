package com.example.meetbook.fragments

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
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
        readSavedRoomFile()
        savedAdapter = ArrayAdapter(context!!,android.R.layout.simple_list_item_1,saved)
        roomlist.adapter = savedAdapter

        return view
    }

    private fun readSavedRoomFile() {
        var dec = DecimalFormat("00")
        var myDir = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.toURI())
        try{
            File(myDir,"SavedMeeting.txt").forEachLine(Charsets.UTF_8) {
                var item = it.split(' ')
                saved.add("Title : ${item[0]}\nRoom ${item[2]} from ${dec.format(item[4].toInt())}:${dec.format(item[6].toInt())} to ${dec.format(item[5].toInt())}:${dec.format(item[7].toInt())}")
        }
        }catch (e : FileNotFoundException){
            Toast.makeText(context,"No Saved Rooms", Toast.LENGTH_SHORT).show()
        }catch (e : IOException){
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
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