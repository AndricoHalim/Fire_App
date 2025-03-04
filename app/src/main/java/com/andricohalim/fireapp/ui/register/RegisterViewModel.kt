package com.andricohalim.fireapp.ui.register

import androidx.lifecycle.ViewModel
import com.andricohalim.fireapp.data.repository.FireRepository

class RegisterViewModel(private val fireRepository: FireRepository) : ViewModel() {

    fun registerUser(username: String, email: String, password: String, location: String) =
        fireRepository.register(username, email, password, location)
}
