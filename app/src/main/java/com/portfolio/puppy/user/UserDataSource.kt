package com.portfolio.puppy.user

import android.util.Log
import com.portfolio.puppy.etc.RetrofitAPI
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception

class UserDataSource {

    // 회원가입
    fun signUp(email: String, pw: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        api.signUp(email, pw, "null", "null").enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onSuccess("SignUp Succeed")
                        } else {
                            it.onSuccess("SignUp Failed")
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "SignUp 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "SignUp 에러")
                t.printStackTrace()
            }
        })
    }

    // 로그인
    fun signIn(email: String, pw: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        api.signIn(email, pw).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onSuccess("SignIn Succeed")
                        } else {
                            it.onSuccess("SignIn Failed")
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "SignIn 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "SignIn 에러")
                t.printStackTrace()
            }
        })

    }

    // 이메일 중복 체크
    fun validateEmail(email: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        api.validateEmail(email).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onSuccess("validate true")
                            Log.e("test", "validate true")

                        } else {
                            it.onSuccess("validate false")
                            Log.e("test", "validate false")
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "validateEmail 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "validate 에러")
                t.printStackTrace()
            }
        })
    }

    // 닉네임 중복 체크
    fun validateName(name: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        api.validateName(name).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onSuccess("validate true")
                            Log.e("test", "validate true")

                        } else {
                            it.onSuccess("validate false")
                            Log.e("test", "validate false")
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "validateName 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "validate 에러")
                t.printStackTrace()
            }
        })
    }

    // 프로필 수정
    fun editProfile(email: String, image: String, name: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        api.editProfile(email, image, name).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onSuccess("editProfile true")
                    }

                } catch (e: Exception) {
                    Log.e("UserViewModel", "editProfile 에러")
                    it.onError(e)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "editProfile 에러")
                t.printStackTrace()
            }
        })
    }

    private fun retrofitBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RetrofitAPI.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }
}