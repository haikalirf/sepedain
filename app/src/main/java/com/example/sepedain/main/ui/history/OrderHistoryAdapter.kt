package com.example.sepedain.main.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sepedain.R
import com.example.sepedain.network.Place

class OrderHistoryAdapter(private val placeList: List<Place>, private val imageList: List<Int>) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(data: Place, image: Int) {
            val name = itemView.findViewById<TextView>(R.id.tv_placename)
            name.text = data.properties.name.toString()
            val img = itemView.findViewById<ImageView>(R.id.iv_placeimage)
            img.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card_orderhistory, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(placeList[position], imageList[position])
    }

    override fun getItemCount(): Int {
        return placeList.size
    }
}