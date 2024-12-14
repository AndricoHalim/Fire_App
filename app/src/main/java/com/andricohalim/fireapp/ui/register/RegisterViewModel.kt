package com.andricohalim.fireapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andricohalim.fireapp.data.model.User
import com.andricohalim.fireapp.data.repository.FireRepository

class RegisterViewModel(private val repository: FireRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Pair<Boolean, String?>>()
    val registerResult: LiveData<Pair<Boolean, String?>> get() = _registerResult

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    fun registerUser(user: User) {
        repository.registerUser(user) { success, message ->
            _registerResult.value = Pair(success, message)
        }
    }

    fun checkUserLoginStatus() {
        _isUserLoggedIn.value = repository.isUserLoggedIn()
    }
}
