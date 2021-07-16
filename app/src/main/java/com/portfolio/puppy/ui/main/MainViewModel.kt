package com.portfolio.puppy.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.portfolio.puppy.data.main.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val repository: MainRepository): ViewModel() {
    private val disposable = CompositeDisposable()
    private var _fragmentStatus = MutableLiveData<Int>() // 프레그먼트 네비게이션 상태

    lateinit var mLoadUserImage: MutableLiveData<String> // 프로필 이미지 불러오기
    lateinit var mErrorMessage: MutableLiveData<String> // 에러 메시지

    val email = repository.getEmail() // 이메일
    val name = repository.getName() // 닉네임
    val auth = repository.getAuth() // 인증 여부

    val fragmentStatus: LiveData<Int>
        get() = _fragmentStatus

    init {
        _fragmentStatus.value = 1
    }

    fun logout() {
        repository.logout()
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
        val data = repository.loadUserImage(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({  mLoadUserImage.value = it},
                        { mErrorMessage.value = "이미지 업로드 중 오류가 발생하였습니다."})

        disposable.add(data)
    }
}