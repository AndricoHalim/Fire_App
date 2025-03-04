package com.andricohalim.fireapp.data.response

import com.google.gson.annotations.SerializedName

data class FireResponse(

	@field:SerializedName("sensorData")
	val sensorData: List<SensorDataItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class SensorDataItem(

	@field:SerializedName("flame_status")
	val flameStatus: String,

	@field:SerializedName("mq_status")
	val mqStatus: String,

	@field:SerializedName("temperature")
	val temperature: Any,

	@field:SerializedName("humidity")
	val humidity: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)
