package com.portfolio.puppy.main

import android.util.Log
import android.widget.ImageView
import com.portfolio.puppy.etc.RetrofitAPI
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainDataSource {

    private fun retrofitBuilder(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(RetrofitAPI.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
    }
}