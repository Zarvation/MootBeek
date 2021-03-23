package com.example.meetbook

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.meetbook.fragments.InterfaceData
import com.example.meetbook.fragments.RoomBookFragment

class RoomListRecyclerViewAdapter (data : MutableList<Rooms>, interfaceData: InterfaceData) : RecyclerView.Adapter<RoomListRecyclerViewAdapter.Holder>() {
    private var RoomData = data
    private var interfaceData: InterfaceData = interfaceData

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val roomtitle = itemView.findViewById<TextView>(R.id.MeetRoomTitle)
        val roomcap = itemView.findViewById<TextView>(R.id.MeetRoomCap)
        val roomimage = itemView.findViewById<ImageView>(R.id.MeetRoomImg)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomListRecyclerViewAdapter.Holder {
        val inflate = LayoutInflater.from(parent.context)
                .inflate(R.layout.room_list, parent, false)

        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: RoomListRecyclerViewAdapter.Holder, position: Int) {
        holder.roomtitle.setText(RoomData.get(position).title)

        val returnedCap = RoomData.get(position).capacity
        holder.roomcap.setText("$returnedCap Seats")

        val imageBytes = Base64.decode(RoomData.get(position).image, Base64.DEFAULT)
        val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.roomimage.setImageBitmap(decodeImage)

        //Cara Pertama kirim data ke fragment
        /*holder.itemView.setOnClickListener(object :View.OnClickListener{
            val model = RoomData.get(position)
            override fun onClick(v: View?) {
                val activity=v!!.context as AppCompatActivity

                val fragmentBook = RoomBookFragment.newInstance(model.title,model.capacity,model.image)

                val transaksi = activity.supportFragmentManager.beginTransaction()
                transaksi.replace(R.id.fragmentContainer, fragmentBook)
                transaksi.addToBackStack(null)
                transaksi.commit()
            }
        })*/

        //Cara kedua, diperlukan interfaceData = activity as InterfaceData (dari RoomListFragment.kt)
        holder.itemView.setOnClickListener {
            val model = RoomData.get(position)
            interfaceData.sendRoomData(model.title,model.capacity,model.image)
        }
    }

    override fun getItemCount(): Int {
        return RoomData.size
    }
}