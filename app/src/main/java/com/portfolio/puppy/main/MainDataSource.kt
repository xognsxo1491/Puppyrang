package com.portfolio.puppy.main

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.portfolio.puppy.etc.RetrofitAPI
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainDataSource {

    // 프로필 이미지 불러오기
    fun loadUserImage(email: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitAPI::class.java)

        api.loadUserImage(email).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            if (!jsonObject.optString("userImage").equals("null")) {
                                it.onSuccess(jsonObject.optString("userImage"))

                            } else {
                                it.onSuccess("null")
                            }
                        }

                    } catch (e: Exception) {
                        Log.e("MainViewModel", "loadUserImage 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("MainViewModel", "loadUserImage 에러")
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