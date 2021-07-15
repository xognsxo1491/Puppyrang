package com.portfolio.puppy.etc

import android.annotation.SuppressLint
import android.content.Context

class PreferencesAPI(val context: Context) {

    // 이메일, 비밀번호 저장
    fun putUserData(email: String, pw: String, auth: Boolean) {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putString("userEmail", email)
        editor.putString("userPw", pw)
        editor.putBoolean("userAuth", auth)

        editor.apply()
    }

    // 닉네임 저장
    fun putName(name: String) {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putString("userName", name)
        editor.apply()
    }

    // 프로필 이미지 이름 저장
    fun putProfileImage(imageName: String) {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putString("userImage", imageName)
        editor.apply()
    }

    // 프로필 이미지 이름 저장
    fun putAuth(auth: Boolean) {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putBoolean("userAuth", auth)
        editor.apply()
    }

    // 이메일 불러오기
    fun getEmail(): String {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return pref.getString("userEmail", "null").toString()
    }

    // 비밀번호 불러오기
    fun getPw(): String {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return pref.getString("userPw", "null").toString()
    }

    // 닉네임 불러오기
    fun getName(): String {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return pref.getString("userName", "null").toString()
    }

    // 프로필 이미지 불러오기
    fun getProfileImage(): String {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return pref.getString("userImage", "null").toString()
    }

    // 이메일 인증여부 불러오기
    fun getAuth(): Boolean {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return pref.getBoolean("userAuth", false)
    }

    // 로그아웃
    @SuppressLint("ApplySharedPref")
    fun logout() {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.clear()
        editor.commit()
    }
}