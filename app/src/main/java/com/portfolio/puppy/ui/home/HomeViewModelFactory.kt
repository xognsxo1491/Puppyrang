package com.portfolio.puppy.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.data.home.HomeDataSource
import com.portfolio.puppy.data.home.HomeRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                repository = HomeRepository(HomeDataSource(), context = context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}