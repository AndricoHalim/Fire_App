package com.andricohalim.fireapp.ui.home

import FireRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andricohalim.fireapp.data.model.DataFire
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FireRepository) : ViewModel(){
    private val _dataHistory = MutableLiveData<List<DataFire>>()
    val dataHistory: LiveData<List<DataFire>> get() = _dataHistory

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getAllLatestData() {
        viewModelScope.launch {
            _loading.postValue(true)
            val data = repository.getAllLatestSensorData()
            _dataHistory.postValue(data)
            _loading.postValue(false)
        }
    }
}