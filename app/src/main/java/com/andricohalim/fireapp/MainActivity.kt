package com.andricohalim.fireapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.andricohalim.fireapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val CHANNEL_ID = "fire_alert_channel"

    private lateinit var db: FirebaseFirestore

    // Map untuk menyimpan status terakhir FlameDetected
    private val lastFlameStatus: MutableMap<String, String> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkNotificationPermission()
        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Buat channel notifikasi
        createNotificationChannel()

        // Memantau perubahan pada koleksi Fire
        db.collection("Fire").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                // Handle error
                return@addSnapshotListener
            }

            querySnapshot?.let {
                for (document in it) {
                    val deviceId = document.id // ID perangkat
                    val dataAlatRef = document.reference.collection("DataAlat")

                    // Memantau perubahan pada DataAlat untuk dokumen ini
                    dataAlatRef.addSnapshotListener { dataAlatSnapshot, dataAlatException ->
                        if (dataAlatException != null) {
                            return@addSnapshotListener
                        }

                        dataAlatSnapshot?.let { snapshot ->
                            for (dataDocument in snapshot) {
                                val flameDetected = dataDocument.getString("FlameDetected")
                                flameDetected?.let {
                                    val documentKey = "${deviceId}_${dataDocument.id}"

                                    // Periksa apakah status FlameDetected sudah berubah
                                    if (lastFlameStatus[documentKey] != it) {
                                        lastFlameStatus[documentKey] = it

                                        // Kirim notifikasi jika api terdeteksi
                                        if (it == "Api Terdeteksi") {
                                            sendFireAlertNotification(
                                                "Api Terdeteksi!",
                                                "Api terdeteksi pada perangkat $deviceId",
                                                deviceId
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Fire Alert Channel"
            val descriptionText = "Channel for fire alert notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendFireAlertNotification(title: String, message: String, deviceId: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val soundUri = Uri.parse("android.resource://${packageName}/raw/alarm")

        val stopIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = "STOP_NOTIFICATION"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            deviceId.hashCode(), // Gunakan hash dari deviceId sebagai requestCode unik
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "OK", stopPendingIntent)
            .build()

        notificationManager.notify(deviceId.hashCode(), notification) // ID unik untuk tiap perangkat
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001 // Request code untuk notifikasi
                )
            } else {
                // Izin sudah diberikan, lakukan sesuatu
                Log.d("Permission", "Notification permission granted")
            }
        } else {
            // Izin tidak diperlukan untuk versi di bawah API 33
            Log.d("Permission", "Notification permission not required for this API level")
        }
    }
}