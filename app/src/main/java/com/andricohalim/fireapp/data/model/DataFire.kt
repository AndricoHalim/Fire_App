package com.andricohalim.fireapp.data.model

data class DataFire(
    val flameDetected: String?,
    val hum: Double?,
    val mqValue: String?,
    val temp: Double?,
    val timestamp: String?,
    val deviceId: String,
)
