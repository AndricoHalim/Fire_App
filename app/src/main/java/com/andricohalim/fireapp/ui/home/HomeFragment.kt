package com.andricohalim.fireapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.ViewModelFactory
import com.andricohalim.fireapp.databinding.FragmentHomeBinding
import com.andricohalim.fireapp.ui.adapter.FireAdapter
import com.andricohalim.fireapp.ui.register.RegisterActivity
import com.andricohalim.fireapp.data.response.Result
import com.andricohalim.fireapp.data.response.SensorDataItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: FireAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_name)
        autoRefreshData()

//        setupActionBar()
        setupRecyclerView()
        observeViewModel()

    }

    private fun setupRecyclerView() {
        adapter = FireAdapter(ArrayList())
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.listData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    updateList(result.data.sensorData)
                }
                is Result.Error -> {
                    showLoading(false)
                    binding.tvNoData.text = result.error
                    binding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListData()
    }

//    @SuppressLint("InflateParams")
//    private fun setupActionBar() {
//        (activity as? AppCompatActivity)?.supportActionBar?.apply {
//            setDisplayShowHomeEnabled(false)
//            setDisplayShowCustomEnabled(true)
//            title = ""
//            val customView = layoutInflater.inflate(R.layout.custom_actionbarlogo, null)
//            customView.findViewById<ImageView>(R.id.logoImageView).setImageResource(R.drawable.logo)
//            customView = customView
//        }
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateList(data: List<SensorDataItem>?) {
        binding.apply {
            if (!data.isNullOrEmpty()) {
                adapter = FireAdapter(data)
                rvList.adapter = adapter
            } else {
                rvList.adapter = null
                tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun autoRefreshData() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                viewModel.getListData()
                delay(2000) // Refresh setiap 5 detik
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}