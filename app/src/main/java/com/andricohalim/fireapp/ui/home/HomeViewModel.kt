package com.andricohalim.fireapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andricohalim.fireapp.data.response.Result
import com.andricohalim.fireapp.data.repository.FireRepository
import com.andricohalim.fireapp.data.response.FireResponse
import com.andricohalim.fireapp.data.retrofit.UserModel
import kotlinx.coroutines.launch

class HomeViewModel(private val fireRepository: FireRepository) : ViewModel() {

    private val _listData = MutableLiveData<Result<FireResponse>>()
    val listData: LiveData<Result<FireResponse>> = _listData

    init {
        getListData()
    }

    fun getListData() {
        viewModelScope.launch {
            val response = fireRepository.getLatestData()
            response.asFlow().collect {
                _listData.value = it
            }

        }
    }

    fun logout() {
        viewModelScope.launch {
            fireRepository.logout()
        }
    }
}
