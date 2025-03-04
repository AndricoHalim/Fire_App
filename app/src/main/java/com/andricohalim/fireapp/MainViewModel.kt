package com.andricohalim.fireapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andricohalim.fireapp.data.repository.FireRepository
import com.andricohalim.fireapp.data.retrofit.UserModel


class MainViewModel(private val fireRepository: FireRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return fireRepository.getSession().asLiveData()
    }
}