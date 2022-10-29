package com.example.sepedain.main.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sepedain.R
import com.example.sepedain.network.Place

class BikesNearYouAdapter(private val placeList: List<Place>) : RecyclerView.Adapter<BikesNearYouAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindData(place: Place) {
            val name = itemView.findViewById<TextView>(R.id.tv_placename)
            name.text = place.properties.name
            val distance = itemView.findViewById<TextView>(R.id.tv_distance)
            distance.text = StringBuilder(place.properties.distance.toString()).append(" m away")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card_bikenearyou, parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(placeList[position])
    }

    override fun getItemCount(): Int {
        return placeList.size
    }
}