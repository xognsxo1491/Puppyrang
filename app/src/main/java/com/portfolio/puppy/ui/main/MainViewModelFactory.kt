package com.portfolio.puppy.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.data.main.MainDataSource
import com.portfolio.puppy.data.main.MainRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                repository = MainRepository(MainDataSource(), context = context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}