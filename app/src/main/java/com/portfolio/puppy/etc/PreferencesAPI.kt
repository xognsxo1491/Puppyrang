package com.portfolio.puppy.etc

import android.content.Context

class PreferencesAPI(val context: Context) {

    // 이메일, 비밀번호 저장
    fun putEmailAndPw(email: String, pw: String) {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putString("userEmail", email)
        editor.putString("userPw", pw)

        editor.apply()
    }

    // 이메일 불러오기
    fun getEmail(): String {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return pref.getString("userEmail", "").toString()
    }

    // 비밀번호 불러오기
    fun getPw(): String {
        val pref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return pref.getString("userPw", "").toString()
    }
}