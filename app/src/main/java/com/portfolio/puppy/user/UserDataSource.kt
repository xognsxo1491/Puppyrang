package com.portfolio.puppy.user

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.portfolio.puppy.etc.RetrofitAPI
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream
import java.util.*

class UserDataSource {

    // 회원가입
    fun signUp(email: String, pw: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        api.signUp(email, pw, "null", "null", "null").enqueue(object : Callback<String> {
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

        api.loadUserData(email, pw).enqueue(object : Callback<String> {
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

                        } else {
                            it.onSuccess("validate false")
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

                        } else {
                            it.onSuccess("validate false")
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

    // 프로필 이미지 업로드
    fun uploadImage(email: String, bitmap: Bitmap) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

        val imageName = UUID.randomUUID().toString()

        api.uploadImage(email, imageName, byteArray).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.e("UserViewModel", "234")

                val jsonObject = JSONObject(response.body().toString())
                Log.e("UserViewModel", "123")

                if (jsonObject.optString("result").equals("true")) {
                    Log.e("UserViewModel", "uploadImage")
                    it.onSuccess("uploadImage true")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "uploadImage 에러")
                it.onError(t)
            }
        })
    }

    // 닉네임 변경
    fun changeName(email: String, name: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        api.changeName(email, name).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onSuccess("changeName true")
                    }

                } catch (e: Exception) {
                    Log.e("UserViewModel", "changeName 에러")
                    it.onError(e)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "changeName 에러")
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