package com.portfolio.puppy.data.main

import android.content.Context
import com.portfolio.puppy.util.PreferencesUtil

class MainRepository(private val dataSource: MainDataSource, private val context: Context) {

    fun getEmail(): String {
        return PreferencesUtil(context).getEmail()
    }

    fun getName(): String {
        return PreferencesUtil(context).getName()
    }

    fun getAuth(): Boolean {
        return PreferencesUtil(context).getAuth()
    }

    fun loadUserImage(email: String) = dataSource.loadUserImage(email)
}