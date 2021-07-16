package com.portfolio.puppy.ui.home

import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdView
import com.portfolio.puppy.data.home.HomeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    private val mDisposable = CompositeDisposable()

    // AdView 불러오기
    fun loadAdView(adView: AdView) {
        val data = repository
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