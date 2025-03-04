//package com.andricohalim.fireapp.ui.maps
//
//import DeviceInfoDialog
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.andricohalim.fireapp.R
//import com.andricohalim.fireapp.data.model.DataFire
//import com.andricohalim.fireapp.data.repository.FireRepository
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//
//class MapsFragment : Fragment(), OnMapReadyCallback {
//
//    private lateinit var googleMap: GoogleMap
//    private lateinit var repository: FireRepository
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        repository = FireRepository(requireContext())
//
//        val view = inflater.inflate(R.layout.fragment_maps, container, false)
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        return view
//    }
//
//    override fun onMapReady(map: GoogleMap) {
//        googleMap = map
//        googleMap.uiSettings.isZoomControlsEnabled = true
//        googleMap.uiSettings.isZoomGesturesEnabled = true
//        loadDeviceLocations()
//
//        googleMap.setOnMarkerClickListener { marker ->
//            val data = marker.tag as? DataFire
//            data?.let {
//                val deviceInfoDialog = DeviceInfoDialog.newInstance(it)
//                deviceInfoDialog.show(childFragmentManager, "DeviceInfoDialog")
//            }
//            true
//        }
//    }
//
//    private fun loadDeviceLocations() {
//        repository.listenToAllDevicesRealtime(
//            onDataChanged = { dataList ->
//                googleMap.clear()
//                for (data in dataList) {
//                    repository.getLocationForDevice(data.deviceId) { location, error ->
//                        if (error != null) {
//                            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//                        } else if (location != null) {
//                            val coordinates = location.split(",")
//                            if (coordinates.size == 2) {
//                                val lat = coordinates[0].trim().toDoubleOrNull()
//                                val lng = coordinates[1].trim().toDoubleOrNull()
//                                if (lat != null && lng != null) {
//                                    val position = LatLng(lat, lng)
//
//                                    val markerColor = if (data.flameDetected == "Api Terdeteksi") {
//                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
//                                    } else {
//                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
//                                    }
//
//                                    val marker = googleMap.addMarker(
//                                        MarkerOptions()
//                                            .position(position)
//                                            .title("Device ID: ${data.deviceId}")
//                                            .icon(markerColor)
//                                    )
//
//                                    marker?.tag = data
//
//                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10f))
//                                }
//                            }
//                        }
//                    }
//                }
//            },
//            onError = { error ->
//                Toast.makeText(context, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
//            }
//        )
//    }
//
//
//
//}
