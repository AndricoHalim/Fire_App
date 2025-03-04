package com.andricohalim.fireapp.di

import android.content.Context
import android.util.Log
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.repository.FireRepository
import com.andricohalim.fireapp.data.retrofit.ApiConfig
import com.andricohalim.fireapp.util.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.andricohalim.fireapp.util.UserPreference


object Injection {
    fun provideRepository(context: Context): FireRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        Log.d(context.getString(R.string.token_disimpan), user.token)
        val apiService = ApiConfig.getApiService(user.token)
        return FireRepository(apiService, pref)
    }

}