package com.andricohalim.fireapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andricohalim.fireapp.data.ViewModelFactory
import com.andricohalim.fireapp.databinding.ActivityMainBinding
import com.andricohalim.fireapp.ui.register.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
//    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin || user.token.isEmpty()) {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            } else {
                Log.d("TOKEN", "Token saat ini: ${user.token}")
            }
        }

//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser == null) {
//            navigateToLogin()
//            return
//        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        notificationHelper = NotificationHelper(this)
//        notificationHelper.createNotificationChannel()
//        notificationHelper.checkNotificationPermission()
//        notificationHelper.startListening()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView = binding.navView
        navView.setupWithNavController(navController)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
