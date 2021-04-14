package com.example.meetbook.fragments

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetbook.*
import kotlinx.android.synthetic.main.fragment_account_detail.*

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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var DisplayName = ContactsContract.Contacts.DISPLAY_NAME
    var Number = ContactsContract.CommonDataKinds.Phone.NUMBER
    var ContactList : MutableList<Contact> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_CLIENT_NAME)
        }

        LoaderManager.getInstance(this).initLoader(1,null,this)
    }

    private lateinit var ContactListdapter : ContactListRecyclerViewAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //hapus "return" ganti dengan val View agar dapat memanggil property dari layout fragment
        val view = inflater.inflate(R.layout.fragment_account_detail, container, false)

        val clientName = view.findViewById<TextView>(R.id.ClientName)
        //val recyclerviewget = view.findViewById<RecyclerView>(R.id.recyclerViewContactList)

        //Menerima isi argument
        clientName.text = "Login as $param1"

        return view
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
                    data.getString(data.getColumnIndex(Number)))
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