package com.portfolio.puppy.main

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.portfolio.puppy.user.UserDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.URI

class MainViewModel: ViewModel() {
    private val disposable = CompositeDisposable()
    private var _fragmentStatus = MutableLiveData<Int>()
    var mUri = MutableLiveData<String>()

    val fragmentStatus: LiveData<Int>
        get() = _fragmentStatus

    init {
        _fragmentStatus.value = 1
    }

    // Fragment 로딩 화면 설정
    fun updateFragmentStatus(num: Int) {
        _fragmentStatus.value = num
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}