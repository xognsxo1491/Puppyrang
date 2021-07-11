package com.portfolio.puppy.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel: ViewModel() {
    private val mDisposable = CompositeDisposable()

    // AdView 불러오기
    fun loadAdView(context: Context, adView: AdView) {
        val data = HomeDataSource(context)
            .loadAdView()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({adView.loadAd(it)}, {})

        mDisposable.add(data)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }
}