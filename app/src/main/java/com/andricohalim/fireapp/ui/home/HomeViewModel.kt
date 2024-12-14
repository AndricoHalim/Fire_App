package com.andricohalim.fireapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.data.repository.FireRepository

class HomeViewModel(private val repository: FireRepository) : ViewModel() {
    private val _dataHistory = MutableLiveData<List<DataFire>>()
    val dataHistory: LiveData<List<DataFire>> get() = _dataHistory

    fun observeAllDevicesRealtime() {
        repository.listenToAllDevicesRealtime(
            onDataChanged = { data ->
                _dataHistory.postValue(data)
            },
            onError = { error ->
                println("Error in listener: ${error.message}")
            }
        )
    }

    fun getLocationForDevice(deviceId: String, onResult: (String?, Exception?) -> Unit) {
        repository.getLocationForDevice(deviceId, onResult)
    }
}
