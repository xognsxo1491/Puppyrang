package com.portfolio.puppy.ui.user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.data.user.UserDataSource
import com.portfolio.puppy.data.user.UserRepository

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(
                    repository = UserRepository(UserDataSource(), context = context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}