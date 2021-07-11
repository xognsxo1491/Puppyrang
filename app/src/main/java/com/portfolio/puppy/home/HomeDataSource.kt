package com.portfolio.puppy.home

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.reactivex.Single
import java.lang.Exception

class HomeDataSource(private val context: Context) {

    // AdView 불러오기
    fun loadAdView() = Single.create<AdRequest> {
        try {
            MobileAds.initialize(context)

            val adRequest =  AdRequest.Builder().build()
            it.onSuccess(adRequest)

        } catch (e: Exception) {
            Log.e("HomeViewModel", "loadAdView 오류 발생")
            it.onError(e)
        }
    }
}