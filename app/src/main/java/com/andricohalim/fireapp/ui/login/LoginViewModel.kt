package com.andricohalim.fireapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andricohalim.fireapp.data.repository.FireRepository

class LoginViewModel(private val repository: FireRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>> get() = _loginResult

    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password) { success, message ->
            _loginResult.value = Pair(success, message)
        }
    }
}
