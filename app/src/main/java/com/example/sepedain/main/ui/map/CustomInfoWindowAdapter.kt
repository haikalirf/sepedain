package com.example.sepedain.main.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.sepedain.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter (context: Context) : GoogleMap.InfoWindowAdapter {

    private lateinit var tvLocation: TextView
    private lateinit var tvDistance: TextView
    private val mWindow: View = LayoutInflater.from(context).inflate(R.layout.info_window_layout, null)

    override fun getInfoContents(marker: Marker): View? {
        tvLocation = mWindow.findViewById(R.id.tvMapLocation)
        tvDistance = mWindow.findViewById(R.id.tvDistance)
        tvLocation.text = marker.title
        tvDistance.text = marker.snippet
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        tvLocation = mWindow.findViewById(R.id.tvMapLocation)
        tvDistance = mWindow.findViewById(R.id.tvDistance)
        tvLocation.text = marker.title
        tvDistance.text = marker.snippet
        return mWindow
    }

}