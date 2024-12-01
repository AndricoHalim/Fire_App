package com.andricohalim.fireapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andricohalim.fireapp.data.ViewModelFactory
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.databinding.FragmentHomeBinding
import com.andricohalim.fireapp.ui.adapter.ListAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: ListAdapter
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
        adapter = ListAdapter(itemList)
        binding.apply {
            rvList.adapter = adapter
            rvList.layoutManager = LinearLayoutManager(requireContext())
            rvList.setHasFixedSize(true)
        }
//        showLoading(true)
    }

    override fun onResume() {
        super.onResume()
        getDataHistory()
    }

    private fun getDataHistory(){
        viewModel.getDataHistory()
        viewModel.dataHistory.observe(viewLifecycleOwner){result ->
//            showLoading(false)
            if (result.isEmpty()){
//                binding.tvEmptyHistory.visibility = View.VISIBLE
            } else {
                adapter.updateData(result)
            }
        }
    }

//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }

}