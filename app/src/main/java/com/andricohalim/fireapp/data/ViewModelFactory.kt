package com.andricohalim.fireapp.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andricohalim.fireapp.MainViewModel
import com.andricohalim.fireapp.data.repository.FireRepository
import com.andricohalim.fireapp.di.Injection
import com.andricohalim.fireapp.ui.home.HomeViewModel
import com.andricohalim.fireapp.ui.login.LoginViewModel
import com.andricohalim.fireapp.ui.register.RegisterViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val repository: FireRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        HomeViewModel::class.java -> HomeViewModel(repository)
        LoginViewModel::class.java -> LoginViewModel(repository)
        RegisterViewModel::class.java -> RegisterViewModel(repository)
        MainViewModel::class.java -> MainViewModel(repository)
//        ProfileViewModel::class.java -> ProfileViewModel(repository)
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    } as T

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return ViewModelFactory(Injection.provideRepository(context))
        }
    }
}
