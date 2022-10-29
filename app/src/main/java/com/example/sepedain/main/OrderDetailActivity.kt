package com.example.sepedain.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sepedain.databinding.ActivityOrderDetailBinding
import com.example.sepedain.network.Place

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val item = intent.getParcelableExtra<Place>("LOCATION")
        val image = intent.getIntExtra("LOCATION_IMAGE", -1)
        binding.apply {
            tvDistance.text = StringBuilder(item?.properties?.distance.toString()).append(" m away")
            tvLocationname.text = item?.properties?.name.toString()
            tvLocationformatted.text = item?.properties?.formatted.toString()
            ivLocationimage.setImageResource(image)
            btnBooknow.setOnClickListener {
                Toast.makeText(this@OrderDetailActivity, "Processing your request", Toast.LENGTH_SHORT).show()
            }
        }
    }
}