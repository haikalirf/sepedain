package com.example.sepedain.main.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.directions.route.*
import com.example.sepedain.R
import com.example.sepedain.databinding.FragmentMapBinding
import com.example.sepedain.dataclasses.PlaceMap
import com.example.sepedain.network.Place
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MapFragment : Fragment(), OnMapReadyCallback, RoutingListener{
    private lateinit var mMap: GoogleMap
    private lateinit var map: GoogleMap
    private lateinit var locationClient: FusedLocationProviderClient
    private val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var dbRef: DatabaseReference
    private lateinit var markerLatLng: LatLng
    private var polylines: MutableList<Polyline>? = null
    private val mapViewModel : MapViewModel by lazy {
        ViewModelProvider(this)[MapViewModel::class.java]
    }
    private lateinit var placeData : ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment = binding.frMapFragmentMap
        mapFragment.getMapAsync(this)

        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapView = view.findViewById(R.id.frMap_fragment_map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        placeData = mapViewModel.placeLiveData.value?.data as ArrayList<Place>
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

    private fun generateLocations(): List<PlaceMap> {
        return listOf(
//            PlaceMap("Fakultas Ilmu Komputer", -7.953983029270556, 112.61428770395894, "https://firebasestorage.googleapis.com/v0/b/sepedain.appspot.com/o/places%2Ffilkom.jpg?alt=media&token=d63f133c-2533-47f1-911f-b7799bceff1d", date = null, duration = null)
            PlaceMap(placeData[0].properties.name.toString(), placeData[0].properties.lat, placeData[0].properties.lon, "https://firebasestorage.googleapis.com/v0/b/sepedain.appspot.com/o/places%2Ffilkom.jpg?alt=media&token=d63f133c-2533-47f1-911f-b7799bceff1d", date = null, duration = null)
        )
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

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                locationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(requireActivity(), "Null Received", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val currUid = auth.currentUser?.uid!!
                        dbRef = FirebaseDatabase.getInstance().reference
                        val update: HashMap<String, Any> = HashMap()
                        update["latitude"] = location.latitude
                        update["longitude"] = location.longitude
                        dbRef.child("user").child(currUid).updateChildren(update)
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

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    private fun getUserLocation(mMap: GoogleMap) {
        getCurrentLocation()
        val currUid = auth.currentUser?.uid!!
        dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("user").child(currUid).get().addOnSuccessListener {
            if (it.exists()) {
                val latitude: Double = it.child("latitude").value as Double
                val longitude: Double = it.child("longitude").value as Double
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 16f))
                val userMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(latitude, longitude))
                        .icon(bitmapDescriptorFromVector(requireActivity(), R.drawable.marker_user))
                )
                userMarker!!.tag = true
            } else {
                Toast.makeText(requireActivity(), "Something broke", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(mMap: GoogleMap) {
        map = mMap
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireActivity()))

        mMap.setOnMarkerClickListener { marker ->
            markerLatLng = marker.position
            var userLatLng: LatLng? = null
            getCurrentLocation()
            val currUid = auth.currentUser?.uid!!
            dbRef = FirebaseDatabase.getInstance().reference
            dbRef.child("user").child(currUid).get().addOnSuccessListener {
                if (it.exists()) {
                    val latitude = it.child("latitude").value as Double
                    val longitude = it.child("longitude").value as Double
                    userLatLng = LatLng(latitude, longitude)
                    findRoutes(userLatLng, markerLatLng)
                } else {
                    Toast.makeText(requireActivity(), "Something broke", Toast.LENGTH_SHORT).show()
                }
            }
//            Toast.makeText(requireActivity(), userLatLng.toString() + " " + markerLatLng.toString(), Toast.LENGTH_SHORT).show()
            marker.tag != null && marker.tag as Boolean
        }

        val placeMaps = generateLocations()
        for (place in placeMaps) {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(place.latitude, place.longitude))
                    .title(place.location)
                    .snippet(place.imageUrl)
                    .icon(bitmapDescriptorFromVector(requireActivity(), R.drawable.marker_sepeda))
            )
            marker!!.tag = false
        }
        getUserLocation(mMap)
    }

    private fun findRoutes(Start: LatLng?, End: LatLng?) {
        if (Start == null || End == null) {
            Toast.makeText(requireActivity(), "Unable to get location", Toast.LENGTH_LONG).show()
        } else {
            val routing: Routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.WALKING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(Start, End)
                .key("AIzaSyBAl3QQGvL01hLI3NNlrSQdHXyxitSwiQw")
                .build()
            routing.execute()
        }
    }

    override fun onRoutingFailure(e: RouteException?) {
        Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
//    Findroutes(start,end);
    }

    override fun onRoutingStart() {
        Toast.makeText(requireActivity(),"Finding Route...",Toast.LENGTH_LONG).show();
    }

    override fun onRoutingSuccess(route: ArrayList<Route>?, shortestRouteIndex: Int) {
//        val center = CameraUpdateFactory.newLatLng(start)
//        val zoom = CameraUpdateFactory.zoomTo(16f)
        polylines?.clear()
        val polyOptions = PolylineOptions()
        var polylineStartLatLng: LatLng? = null
        var polylineEndLatLng: LatLng? = null


        polylines = ArrayList()
        //add route(s) to the map using polyline
        //add route(s) to the map using polyline
        if (route != null) {
            for (i in 0 until route.size) {
                if (i == shortestRouteIndex) {
                    polyOptions.color(R.color.orange)
                    polyOptions.width(7f)
                    polyOptions.addAll(route.get(shortestRouteIndex).getPoints())
                    val polyline = map.addPolyline(polyOptions)
                    polyline.points[0]
                    val k = polyline.points.size
                    polyline.points[k - 1]
                    (polylines as ArrayList<Polyline>).add(polyline)
                }
            }
        }

        //Add Marker on route starting position
//        val startMarker = MarkerOptions()
//        startMarker.position(polylineStartLatLng!!)
//        startMarker.title("My Location")
//        mMap.addMarker(startMarker)

        //Add Marker on route ending position
//        val endMarker = MarkerOptions()
//        endMarker.position(polylineEndLatLng!!)
//        endMarker.title("Destination")
//        mMap.addMarker(endMarker)
    }

    override fun onRoutingCancelled() {
        TODO("Not yet implemented")
    }
}
