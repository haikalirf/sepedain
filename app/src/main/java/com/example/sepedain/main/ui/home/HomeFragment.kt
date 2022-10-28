package com.example.sepedain.main.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sepedain.R
import com.example.sepedain.databinding.FragmentHomeBinding
import com.example.sepedain.network.ApiClient
import com.example.sepedain.network.PlaceResponse
import retrofit2.Call
import retrofit2.Response

//import com.example.sepedain.home.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val client = ApiClient.apiService.fetchPlace(
            "commercial",
            "geometry:0ad2ca57a82b4a5ab2919dc0a5e93711",
            "proximity:112.6659335,-7.8931151",
            "20",
            "ec51bcde20554127ac97cc4c52eff067"
        )

        client.enqueue(object : retrofit2.Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                if (response.isSuccessful) {
                    Log.d("place", ""+response.body())

                    val result = response.body()?.result
                    result?.let {
                        val adapter = BikesNearYouAdapter(result)
                        val recyclerView = binding.rvBikesnearyou
                        recyclerView.layoutManager = LinearLayoutManager(HomeFragment().context, LinearLayoutManager.HORIZONTAL, false)
                        recyclerView.adapter = adapter
                        recyclerView.setHasFixedSize(true)
                    }
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                Log.e("failed", ""+t.message)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}