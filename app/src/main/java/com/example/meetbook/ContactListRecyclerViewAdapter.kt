package com.example.meetbook

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_list.view.*

class ContactListRecyclerViewAdapter(data : MutableList<Contact>) : RecyclerView.Adapter<ContactListRecyclerViewAdapter.Holder>() {
    private var ContactData = data

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val contactName = itemView.ContactName
        val contactNumber = itemView.PhoneNumber
        val buttonShare = itemView.buttonShare
        var context = itemView.getContext()

        fun bindContact(tmp: Contact){
            contactName.text = "${contactName.text} : ${tmp.nama}"
            contactNumber.text = "${contactNumber.text} : ${tmp.nomorhp}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_list, parent, false)

        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindContact(ContactData[position])

        holder.buttonShare.setOnClickListener {
            val model = ContactData.get(position)
            var uriPhone = Uri.parse("smsto:${model.nomorhp}")
            var intentSMS = Intent(Intent.ACTION_SENDTO,uriPhone) //action kirim ke nomor
            intentSMS.putExtra("sms_body","Download Meetbook now on Google Play!")
            holder.context.startActivity(intentSMS)
        }
    }

    override fun getItemCount(): Int {
        return ContactData.size
    }
}