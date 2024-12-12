package com.andricohalim.fireapp.ui.dashboard

import FireRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.andricohalim.fireapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DashboardFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var repository: FireRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository = FireRepository(requireContext())

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true

        loadDeviceLocations()
    }

    private fun loadDeviceLocations() {
        repository.listenToAllDevicesRealtime(
            onDataChanged = { dataList ->
                googleMap.clear()
                for (data in dataList) {
                    repository.getLocationForDevice(data.deviceId) { location, error ->
                        if (error != null) {
                            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        } else if (location != null) {
                            val coordinates = location.split(",")
                            if (coordinates.size == 2) {
                                val lat = coordinates[0].trim().toDoubleOrNull()
                                val lng = coordinates[1].trim().toDoubleOrNull()
                                if (lat != null && lng != null) {
                                    val position = LatLng(lat, lng)

                                    val isFlameDetected = data.flameDetected?.toLowerCase() == "api terdeteksi"

                                    val markerColor = if (isFlameDetected) {
                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                    } else {
                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                                    }

                                    googleMap.addMarker(
                                        MarkerOptions()
                                            .position(position)
                                            .title("Device ID: ${data.deviceId}")
                                            .snippet("Flame: ${data.flameDetected}, Temp: ${data.temp}")
                                            .icon(markerColor)
                                    )
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10f))
                                }
                            }
                        }
                    }
                }
            },
            onError = { error ->
                Toast.makeText(context, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }


}
