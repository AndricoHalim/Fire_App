package com.andricohalim.fireapp.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andricohalim.fireapp.data.ViewModelFactory
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.databinding.FragmentHomeBinding
import com.andricohalim.fireapp.ui.adapter.FireAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: FireAdapter
    private val itemList = ArrayList<DataFire>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FireAdapter(itemList) { deviceId ->
            Log.d("HomeFragment", "Clicked deviceId: $deviceId")
            viewModel.getLocationForDevice(deviceId) { location, error ->
                if (error != null) {
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                } else {
                    if (location != null) {
                        Log.d("HomeFragment", "Opening Google Maps with location: $location")
                        openGoogleMaps(location)
                    } else {
                        Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.apply {
            rvList.adapter = adapter
            rvList.layoutManager = LinearLayoutManager(requireContext())
            rvList.setHasFixedSize(true)
            swipeRefreshLayout.setOnRefreshListener {
                observeRealTimeData()
            }
        }

        observeRealTimeData()
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    private fun observeRealTimeData() {
        if (isInternetAvailable(requireContext())) {
            showLoading(true)
            viewModel.observeAllDevicesRealtime()
            viewModel.dataHistory.observe(viewLifecycleOwner) { data ->
                if (data.isEmpty()) {
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    binding.tvNoData.visibility = View.GONE
                    adapter.updateData(data)
                }
                showLoading(false)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            binding.tvNoData.visibility = View.VISIBLE
            showLoading(false)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun openGoogleMaps(location: String) {
        val coordinates = location.split(",")

        if (coordinates.size == 2) {
            val latitude = coordinates[0].trim()
            val longitude = coordinates[1].trim()

            Log.d("Maps", "Opening Google Maps search with coordinates: $latitude, $longitude")

            val uri = "https://www.google.com/maps/search/$latitude,$longitude"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

            intent.setPackage("com.google.android.apps.maps")

            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("Maps", "Error opening Google Maps: ${e.message}")
                Toast.makeText(context, "Google Maps tidak terinstal", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("Maps", "Invalid location format: $location")
            Toast.makeText(context, "Invalid location format", Toast.LENGTH_SHORT).show()
        }
    }
}
