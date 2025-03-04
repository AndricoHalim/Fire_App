package com.andricohalim.fireapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andricohalim.fireapp.data.repository.FireRepository
import com.andricohalim.fireapp.data.retrofit.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val fireRepository: FireRepository) : ViewModel() {

    fun loginUser(email: String, password: String)=
        fireRepository.login(email, password)

    fun saveSession(user: UserModel)=
        viewModelScope.launch {
            fireRepository.saveSession(user)
        }
}