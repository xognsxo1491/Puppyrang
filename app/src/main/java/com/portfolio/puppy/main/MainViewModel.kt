package com.portfolio.puppy.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private var _fragmentStatus = MutableLiveData<Int>()

    val fragmentStatus: LiveData<Int>
        get() = _fragmentStatus

    init {
        _fragmentStatus.value = 1
    }

    // Fragment 로딩 화면 설정
    fun updateFragmentStatus(num: Int) {
        _fragmentStatus.value = num
    }

}