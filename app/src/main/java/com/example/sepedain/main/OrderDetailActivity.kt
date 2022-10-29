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

        val item = intent.getParcelableExtra<Place>("LOCATION")
        val image = intent.getIntExtra("LOCATION_IMAGE", -1)
        binding.apply {
            tvDistance.text = item?.properties?.distance.toString()
            tvLocationname.text = item?.properties?.name.toString()
            tvLocationformatted.text = item?.properties?.name.toString()
//            ivLocationimage.setImageResource(image)
            btnBooknow.setOnClickListener {
                Toast.makeText(this@OrderDetailActivity, "Processing your reqquest", Toast.LENGTH_SHORT).show()
            }
        }
        image.let { binding.ivLocationimage.setImageResource(it) }
    }
}