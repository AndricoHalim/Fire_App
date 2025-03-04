//package com.andricohalim.fireapp
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//
//class NotificationHelper(private val context: Context) {
//
//    private val CHANNEL_ID = "fire_alert_channel"
//    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
//    private val lastFlameStatus: MutableMap<String, String> = mutableMapOf()
//
//    fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Fire Alert Channel"
//            val descriptionText = "Channel for fire alert notifications"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    private fun sendFireAlertNotification(title: String, message: String, deviceId: String) {
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val soundUri = Uri.parse("android.resource://${context.packageName}/raw/alarm")
//
//        // Intent untuk membuka MainActivity saat notifikasi diklik
//        val mainIntent = Intent(context, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val mainPendingIntent = PendingIntent.getActivity(
//            context,
//            0, // Request code (arbitrary, gunakan nilai unik jika Anda ingin membedakan intent)
//            mainIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Intent untuk menghentikan notifikasi
//        val stopIntent = Intent(context, NotificationReceiver::class.java).apply {
//            action = "STOP_NOTIFICATION"
//        }
//        val stopPendingIntent = PendingIntent.getBroadcast(
//            context,
//            deviceId.hashCode(),
//            stopIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(android.R.drawable.ic_dialog_alert)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .setSound(soundUri)
//            .setVibrate(longArrayOf(0, 1000, 500, 1000))
//            .setContentIntent(mainPendingIntent)
//            .build()
//
//        notificationManager.notify(deviceId.hashCode(), notification)
//    }
//
//
//    class NotificationReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent?) {
//            if (intent?.action == "STOP_NOTIFICATION") {
//                val notificationManager =
//                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                notificationManager.cancel(1)
//            }
//        }
//    }
//
//    fun startListening() {
//        db.collection("Fire").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            if (firebaseFirestoreException != null) {
//                Log.e("Firestore", "Error: ${firebaseFirestoreException.message}")
//                return@addSnapshotListener
//            }
//
//            querySnapshot?.let {
//                for (document in it) {
//                    val deviceId = document.id
//                    val dataAlatRef = document.reference.collection("DataAlat")
//
//                    dataAlatRef
//                        .orderBy("timestamp", Query.Direction.DESCENDING)
//                        .limit(1)
//                        .addSnapshotListener { dataAlatSnapshot, dataAlatException ->
//                            if (dataAlatException != null) {
//                                Log.e("Firestore", "Error: ${dataAlatException.message}")
//                                return@addSnapshotListener
//                            }
//
//                            dataAlatSnapshot?.let { snapshot ->
//                                for (dataDocument in snapshot) {
//                                    val flameDetected = dataDocument.getString("FlameDetected")
//                                    flameDetected?.let {
//                                        val documentKey = "${deviceId}_${dataDocument.id}"
//
//                                        if (lastFlameStatus[documentKey] != it) {
//                                            lastFlameStatus[documentKey] = it
//
//                                            if (it == "Api Terdeteksi") {
//                                                sendFireAlertNotification(
//                                                    "Api Terdeteksi!",
//                                                    "Api terdeteksi pada perangkat $deviceId",
//                                                    deviceId
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                }
//            }
//        }
//    }
//
//    fun checkNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
//                (context as? MainActivity)?.requestPermissions(
//                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
//                    1001
//                )
//            } else {
//                Log.d("Permission", "Notification permission granted")
//            }
//        } else {
//            Log.d("Permission", "Notification permission not required for this API level")
//        }
//    }
//}
