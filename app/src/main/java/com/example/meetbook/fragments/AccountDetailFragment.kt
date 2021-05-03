package com.example.meetbook.fragments

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetbook.*
import kotlinx.android.synthetic.main.fragment_account_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private val PrefFileName = "ROOMFILE001"
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var DisplayName = ContactsContract.Contacts.DISPLAY_NAME
    var Number = ContactsContract.CommonDataKinds.Phone.NUMBER
    //var Image = ContactsContract.CommonDataKinds.Photo.PHOTO
    var ContactList : MutableList<Contact> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_CLIENT_NAME)
        }

        LoaderManager.getInstance(this).initLoader(1,null,this)
    }

    private lateinit var ContactListdapter : ContactListRecyclerViewAdapter
    private lateinit var interfaceData: InterfaceData
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //hapus "return" ganti dengan val View agar dapat memanggil property dari layout fragment
        val view = inflater.inflate(R.layout.fragment_account_detail, container, false)
        val clientName = view.findViewById<TextView>(R.id.ClientName)
        val current = view.findViewById<TextView>(R.id.CurrentRoomLabel)
        val roomName = view.findViewById<TextView>(R.id.MeetRoomTitle)
        val roomCap = view.findViewById<TextView>(R.id.MeetRoomCap)
        val roomImage = view.findViewById<ImageView>(R.id.MeetRoomImg)
        val bookedRoom = view.findViewById<LinearLayout>(R.id.bookedRoomView)
        //val recyclerviewget = view.findViewById<RecyclerView>(R.id.recyclerViewContactList)

        //Menerima isi argument
        clientName.text = "Login as $param1"
        //tambahkan interfacedata dengan aktivitas dari interfaceData
        interfaceData = activity as InterfaceData

        var roomSharedPrefHelper = SharedPrefHelper(context!!, PrefFileName)
        var getTitle = roomSharedPrefHelper.name
        var getCap = roomSharedPrefHelper.cap
        var getImg = roomSharedPrefHelper.image
        var getBegHour = roomSharedPrefHelper.beginHour
        var getEndHour = roomSharedPrefHelper.endHour
        var getBegMin = roomSharedPrefHelper.beginMin
        var getEndMin = roomSharedPrefHelper.endMin

        roomName.setText(getTitle)
        roomCap.text = "${getCap} Seats"
        doAsync{
            val imageresult=decodeStrImg(getImg)
            uiThread {
                roomImage.setImageBitmap(imageresult)
            }
        }

        if (getTitle == ""){
            bookedRoom.visibility = View.GONE
            current.text = "0 Room Currently Booked"
        }
        else{
            bookedRoom.visibility = View.VISIBLE
            current.text = "1 Room Currently Booked"
        }

        bookedRoom.setOnClickListener {
            interfaceData.sendBookedRoomData(getTitle.toString(),getCap.toString().toInt(),getImg.toString(),getBegHour.toString().toInt(),getEndHour.toString().toInt(),getBegMin.toString().toInt(),getEndMin.toString().toInt())
        }

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
         * @return A new instance of fragment AccountDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                AccountDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_CLIENT_NAME, param1)
                    }
                }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var ContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        var Projection = arrayOf(DisplayName,Number)
        return CursorLoader(activity!!,ContactUri,Projection,null,null,"$DisplayName ASC")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        ContactList.clear()
        if(data != null){
            data.moveToFirst()
            while(!data.isAfterLast){
                ContactList.add(Contact(
                    data.getString(data.getColumnIndex(DisplayName)),
                    data.getString(data.getColumnIndex(Number))
                        //,data.getInt(data.getColumnIndex(Image))
                )
                )
                data.moveToNext()
            }
            ContactListdapter = ContactListRecyclerViewAdapter(ContactList)
            recyclerViewContactList.adapter = ContactListdapter
            recyclerViewContactList.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        recyclerViewContactList?.adapter?.notifyDataSetChanged()
    }
}