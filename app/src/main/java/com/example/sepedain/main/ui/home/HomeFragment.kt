package com.example.sepedain.main.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sepedain.R
import com.example.sepedain.databinding.FragmentHomeBinding
import com.example.sepedain.main.MainActivity
import com.example.sepedain.main.OrderDetailActivity
import com.example.sepedain.main.ScreenState
import com.example.sepedain.network.Place
import com.example.sepedain.network.locImage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var stref: StorageReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()

        auth = FirebaseAuth.getInstance()
        val uid = auth.uid
        stref = FirebaseStorage.getInstance().getReference("profile-pictures/$uid")
        try {
            val localFile: File = File.createTempFile("Tempfile", ".jpg ")
            stref.getFile(localFile).addOnCompleteListener {
                if (it.isSuccessful) {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    binding.ivProfilepictureHomefragment.setImageBitmap(bitmap)
                } else {
//                    Toast.makeText(requireActivity(), "Failed to retrieve image", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()

        homeViewModel.fetchPlace(longitude, latitude)
        homeViewModel.placeLiveData.observe(viewLifecycleOwner) { state ->
            processPlacesResponse(state)
        }
    }

    private fun processPlacesResponse(state: ScreenState<List<Place>?>) {
        val pb = binding.progressBar
        when (state) {
            is ScreenState.Loading -> {
                pb.visibility = View.VISIBLE
            }
            is ScreenState.Success -> {
                pb.visibility = View.GONE
                if (state.data != null) {
                    val adapter = BikesNearYouAdapter(state.data, locImage)
                    val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_bikesnearyou)
                    recyclerView?.layoutManager =
                        LinearLayoutManager(
                            HomeFragment().context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    recyclerView?.adapter = adapter
                    recyclerView?.setHasFixedSize(true)

                    adapter.setOnItemClickCallback(object : BikesNearYouAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: Place, image: Int) {
                            val intent = Intent(requireActivity(), OrderDetailActivity::class.java)
                            intent.putExtra(LOCATION_DATA, data)
                            intent.putExtra(LOCATION_IMAGE, image)
                            startActivity(intent)
                        }

                    })
                }
            }
            is ScreenState.Error -> {
                pb.visibility = View.GONE
                val view = pb.rootView
                Snackbar.make(view, state.message!!, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(requireActivity(), "Null Received", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Get Success $longitude $latitude",
                            Toast.LENGTH_SHORT
                        ).show()
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
        const val LOCATION_DATA = "LOCATION"
        const val LOCATION_IMAGE = "LOCATION_IMAGE"
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(requireActivity(), "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}