//package com.andricohalim.fireapp.ui.profile
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.andricohalim.fireapp.data.ViewModelFactory
//import com.andricohalim.fireapp.databinding.FragmentProfileBinding
//import com.andricohalim.fireapp.ui.login.LoginActivity
//
//class ProfileFragment : Fragment() {
//
//    private var _binding: FragmentProfileBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var profileViewModel: ProfileViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//
//        val factory = ViewModelFactory.getInstance(requireContext())
//        profileViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
//
//        profileViewModel.loadUserData()
//
//        profileViewModel.user.observe(viewLifecycleOwner) { user ->
//            val username = user["username"] as? String ?: "N/A"
//            val email = user["email"] as? String ?: "N/A"
//            binding.tvUsername.text = username
//            binding.tvEmail.text = email
//        }
//
//        binding.btnLogout.setOnClickListener {
//            logoutUser()
//        }
//
//        return binding.root
//    }
//
//
//    private fun logoutUser() {
//        profileViewModel.logoutUser()
//        Toast.makeText(requireContext(), "You have been logged out.", Toast.LENGTH_SHORT).show()
//        navigateToLogin()
//    }
//
//    private fun navigateToLogin() {
//        val intent = Intent(requireContext(), LoginActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
