package com.andricohalim.fireapp.di

import FireRepository
import android.content.Context

object Injection {
    fun provideRepository(context: Context): FireRepository {
        return FireRepository.getInstance(context)
    }
}