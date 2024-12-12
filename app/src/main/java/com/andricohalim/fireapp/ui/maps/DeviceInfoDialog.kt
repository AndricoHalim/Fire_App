import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.andricohalim.fireapp.databinding.CustomInfoWindowBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.model.DataFire

class DeviceInfoDialog : DialogFragment() {

    private var _binding: CustomInfoWindowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = CustomInfoWindowBinding.inflate(LayoutInflater.from(context))

        val deviceInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("deviceInfo", DataFire::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable<DataFire>("deviceInfo")
        }

        if (deviceInfo != null) {
            binding.tvId.text = deviceInfo.deviceId
            binding.tvFlameDetected.text = "Status Api: ${deviceInfo.flameDetected}"
            binding.tvTemperature.text = "Temperature: ${deviceInfo.temp} °C"
            binding.mqValue.text = "Kualitas Udara: ${deviceInfo.mqValue}"
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            .setView(binding.root)
            .create()
    }

    companion object {
        fun newInstance(deviceInfo: DataFire): DeviceInfoDialog {
            val fragment = DeviceInfoDialog()
            val bundle = Bundle()
            bundle.putParcelable("deviceInfo", deviceInfo)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
