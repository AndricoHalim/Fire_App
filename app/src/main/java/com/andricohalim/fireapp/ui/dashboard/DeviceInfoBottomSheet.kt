package com.andricohalim.fireapp.ui.dashboard

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.databinding.CustomInfoWindowBinding

class DeviceInfoBottomSheet : BottomSheetDialogFragment() {

    private var _binding: CustomInfoWindowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomInfoWindowBinding.inflate(inflater, container, false)

        val deviceInfo = arguments?.getParcelable<DataFire>("deviceInfo")

        if (deviceInfo != null) {
            binding.apply {
                locationName.text = deviceInfo.deviceId
                flameDetected.text = "Flame Detected: ${deviceInfo.flameDetected}"
                temperature.text = "Temperature: ${deviceInfo.temp} °C"
                mqValue.text = "MQ Value: ${deviceInfo.mqValue}"
            }
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(deviceInfo: DataFire): DeviceInfoBottomSheet {
            val fragment = DeviceInfoBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable("deviceInfo", deviceInfo)
            fragment.arguments = bundle
            return fragment
        }
    }
}
