package com.portfolio.puppy.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    lateinit var mIsSuccess: MutableLiveData<Boolean> // 로그인, 회원가입 성공 여부
    lateinit var mIsValidate: MutableLiveData<Boolean> // 이메일 중복 확인
    lateinit var mErrorMessage: MutableLiveData<String> // 에러 메시지

    fun signUp(email: String, pw: String) {
        val data = UserDataSource().signUp(email, pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsSuccess.value = it.equals("SignUp Succeed")
                }, {
                    mErrorMessage.value = "회원가입 중 오류가 발생하였습니다."
                })

        disposable.add(data)
    }

    fun signIn(email: String, pw: String) {
        val data = UserDataSource().signIn(email, pw)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                       mIsSuccess.value = it.equals("SignIn Succeed")
            }, {
                mErrorMessage.value = "로그인 중 오류가 발생하였습니다."
            })

        disposable.add(data)
    }

    fun validateEmail(email: String) {
        val data = UserDataSource().validateEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsValidate.value = it.equals("validate false")
                }, {})

        disposable.add(data)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}