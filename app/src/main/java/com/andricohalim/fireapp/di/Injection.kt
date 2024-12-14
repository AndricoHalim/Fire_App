package com.andricohalim.fireapp.di

import android.content.Context
import com.andricohalim.fireapp.data.repository.FireRepository

object Injection {
    fun provideRepository(context: Context): FireRepository {
        return FireRepository.getInstance(context)
    }
}