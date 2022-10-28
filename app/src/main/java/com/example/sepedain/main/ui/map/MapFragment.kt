package com.example.sepedain.main.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sepedain.R
import com.example.sepedain.databinding.FragmentMapBinding
import com.example.sepedain.main.ui.home.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback{
    private lateinit var mMap: GoogleMap
    private lateinit var locationClient: FusedLocationProviderClient
    private val PERMISSIONS_REQUEST_LOCATION = 1
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment = binding.frMapFragmentMap
        mapFragment.getMapAsync(this)

        return binding.root

//        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
//

//        val dashboardViewModel =
//            ViewModelProvider(this).get(MapViewModel::class.java)
//
//        _binding = FragmentMapBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.frMap_fragment_map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapView = view.findViewById(R.id.frMap_fragment_map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun generateLocations(): List<Place> {
        return listOf(
            Place("Fakultas Ilmu Komputer", -7.953983029270556, 112.61428770395894)
        )
    }

    private fun lastLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
            )
        }

        var loc: Location? = null
        locationClient.lastLocation.addOnSuccessListener {
            loc = it
//            Toast.makeText(this, (loc?.latitude?.toBigDecimal()?.toPlainString()) + " " + (loc?.longitude?.toBigDecimal()?.toPlainString()), Toast.LENGTH_SHORT).show()
        }
        return loc
    }

    override fun onMapReady(googleMap: GoogleMap) {
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mMap = googleMap
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireActivity()))
        val places: List<Place> = generateLocations()

        for (place in places) {
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(place.latitude, place.longitude))
                    .title(place.location)
                    .snippet("150m away")
                    .icon(bitmapDescriptorFromVector(requireActivity(), R.drawable.marker_sepeda)))
        }
        val startLocation: Location? = lastLocation()
        if (startLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(startLocation.latitude, startLocation.longitude), 16f))
//            Toast.makeText(this, (startLocation.latitude.toBigDecimal().toPlainString()) + (startLocation.longitude.toBigDecimal().toPlainString()), Toast.LENGTH_SHORT).show()
        }
        else {
//            Toast.makeText(this, "Couldn't find location", Toast.LENGTH_SHORT).show()
        }
    }
}