package com.example.meetbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import com.example.meetbook.ARG_CLIENT_NAME
import com.example.meetbook.ARG_CLIENT_PASSWORD
import com.example.meetbook.HomeActivity.Companion.current_password
import com.example.meetbook.HomeActivity.Companion.current_username
import com.example.meetbook.R
import com.example.meetbook.RoomUserDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_edit_user.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_GETID = "IDTOEDIT123"
private const val ARG_GETUSER = "USERTOEDIT123"
private const val ARG_GETPASS = "PASSTOEDIT123"

/**
 * A simple [Fragment] subclass.
 * Use the [EditUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var paramID: Int? = null
    private var paramUser: String? = null
    private var paramPass: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramID = it.getInt(ARG_GETID)
            paramUser = it.getString(ARG_GETUSER)
            paramPass = it.getString(ARG_GETPASS)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_user, container, false)

        val textuser = view.findViewById<TextView>(R.id.textUser)
        val textpass = view.findViewById<TextView>(R.id.textPass)
        val usernameText = view.findViewById<EditText>(R.id.editTextUsername)
        val passwordText = view.findViewById<EditText>(R.id.editTextPassword)
        val savebtn = view.findViewById<Button>(R.id.SaveBtn)

        var db= Room.databaseBuilder(
            context!!,
            RoomUserDBHelper::class.java,
            "meetbookDb.db"
        ).build()

        savebtn.setOnClickListener {
            var newUsername = usernameText.text.toString()
            var newPassword = passwordText.text.toString()
            if (usernameText.text.toString().isEmpty()){
                Toast.makeText(context,"Username Belum Diisi", Toast.LENGTH_SHORT).show()
            }
            else if (passwordText.text.toString().isEmpty()){
                Toast.makeText(context,"Password Belum Diisi", Toast.LENGTH_SHORT).show()
            }
            else {
                doAsync {
                    db.userDao().updateDB(paramID!!, newUsername, newPassword)
                    uiThread {
                        current_username = newUsername
                        current_password = newPassword
                        Toast.makeText(context,"User Updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
         * @return A new instance of fragment EditUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(paramID: Int, paramUser: String, paramPass: String) =
                EditUserFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_GETID, paramID)
                        putString(ARG_GETUSER, paramUser)
                        putString(ARG_GETPASS, paramPass)
                    }
                }
    }
}