package com.portfolio.puppy.data.user

import android.content.Context
import android.graphics.Bitmap
import com.portfolio.puppy.util.PreferencesUtil

class UserRepository(private val dataSource: UserDataSource, private val context: Context) {

    fun getEmail(): String {
        return PreferencesUtil(context).getEmail()
    }

    fun getPw(): String {
        return PreferencesUtil(context).getPw()
    }

    fun getName(): String {
        return PreferencesUtil(context).getName()
    }

    fun getImage(): String {
        return PreferencesUtil(context).getProfileImage()
    }

    fun putUserData(email: String, pw: String) {
        PreferencesUtil(context).putUserData(email, pw, false)
    }

    fun putName(name: String) {
        PreferencesUtil(context).putName(name)
    }

    fun putAuth(value: Boolean) {
        PreferencesUtil(context).putAuth(value)
    }

    fun putProfileImage(imageName: String) {
        PreferencesUtil(context).putProfileImage(imageName)
    }

    fun signUp(email: String, pw: String) = dataSource.signUp(email, pw)

    fun signIn(email: String, pw: String) = dataSource.signIn(email, pw)

    fun validateEmail(email: String) = dataSource.validateEmail(email)

    fun validateName(name: String) = dataSource.validateName(name)

    fun uploadUserImage(email: String, bitmap: Bitmap, imageName: String) = dataSource.uploadUserImage(email, bitmap, imageName)

    fun loadUserImage(email: String) = dataSource.loadUserImage(email)

    fun deleteUserImage(email: String, image: String) = dataSource.deleteUserImage(email, image)

    fun changeName(email: String, name: String) = dataSource.changeName(email, name)

    fun sendEmail(title: String, code: String, dest: String) = dataSource.sendEmail(title, code, dest)

    fun changeAuth(email: String) = dataSource.changeAuth(email)

}