package com.portfolio.puppy.main

import android.content.Context
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
    private var _fragmentStatus = MutableLiveData<Int>() // 프레그먼트 네비게이션 상태

    lateinit var mLoadUserImage: MutableLiveData<String> // 프로필 이미지 불러오기
    lateinit var mErrorMessage: MutableLiveData<String> // 에러 메시지

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

    fun loadUserImage(email: String) {
        val data = MainDataSource().loadUserImage(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({  mLoadUserImage.value = it},
                        { mErrorMessage.value = "이미지 업로드 중 오류가 발생하였습니다."})

        disposable.add(data)
    }
}