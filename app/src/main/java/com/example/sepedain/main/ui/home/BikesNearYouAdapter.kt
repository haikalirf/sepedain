package com.example.sepedain.main.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sepedain.R
import com.example.sepedain.network.Place

class BikesNearYouAdapter(private val placeList: List<Place>, private val locImage: List<Int>) :
    RecyclerView.Adapter<BikesNearYouAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Place, image: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(place: Place, locationImage: Int) {
            val name = itemView.findViewById<TextView>(R.id.tv_placename)
            name.text = place.properties.name
            val distance = itemView.findViewById<TextView>(R.id.tv_distance)
            distance.text = StringBuilder(place.properties.distance.toString()).append("m away")
            val image = itemView.findViewById<ImageView>(R.id.iv_placeimage)
            image.setImageResource(locationImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card_bikenearyou, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(placeList[position], locImage[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                placeList[holder.adapterPosition],
                locImage[holder.adapterPosition]
            )
        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }
}