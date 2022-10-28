package com.example.sepedain.main.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.sepedain.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter (context: Context) : GoogleMap.InfoWindowAdapter {

    private lateinit var tvLocation: TextView
    private lateinit var tvDistance: TextView
    private lateinit var ivImage: ImageView
    private val mWindow: View = LayoutInflater.from(context).inflate(R.layout.info_window_layout, null)

    override fun getInfoContents(marker: Marker): View? {
        tvLocation = mWindow.findViewById(R.id.tvMapLocation)
//        tvDistance = mWindow.findViewById(R.id.tvDistance)
        ivImage = mWindow.findViewById(R.id.ivLocationImage_info_window_layout)
        tvLocation.text = marker.title
        Glide.with(mWindow).load(marker.snippet).centerCrop().placeholder(R.drawable.placeholder).into(ivImage)
//        tvDistance.text = marker.snippet
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        tvLocation = mWindow.findViewById(R.id.tvMapLocation)
//        tvDistance = mWindow.findViewById(R.id.tvDistance)
        ivImage = mWindow.findViewById(R.id.ivLocationImage_info_window_layout)
        tvLocation.text = marker.title
        Glide.with(mWindow).load(marker.snippet).into(ivImage)
//        tvDistance.text = marker.snippet
        return mWindow
    }

}