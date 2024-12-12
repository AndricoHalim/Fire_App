package com.andricohalim.fireapp.data.model
import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize


@Parcelize
data class DataFire(
    val flameDetected: String?,
    val hum: Double?,
    val mqValue: String?,
    val temp: Double?,
    val timestamp: Timestamp?,
    val deviceId: String
) : Parcelable
