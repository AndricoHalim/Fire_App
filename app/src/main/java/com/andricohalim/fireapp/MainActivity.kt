package com.andricohalim.fireapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andricohalim.fireapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationHelper = NotificationHelper(this)

        notificationHelper.createNotificationChannel()

        notificationHelper.checkNotificationPermission()

        notificationHelper.startListening()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView = binding.navView
        navView.setupWithNavController(navController)
    }
}
