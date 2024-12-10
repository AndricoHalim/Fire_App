package com.andricohalim.fireapp.data.model
import com.google.firebase.Timestamp


data class DataFire(
    val flameDetected: String?,
    val hum: Double?,
    val mqValue: String?,
    val temp: Double?,
    val timestamp: Timestamp?,
    val deviceId: String,
)
