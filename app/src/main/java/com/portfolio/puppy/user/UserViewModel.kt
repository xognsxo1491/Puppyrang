package com.portfolio.puppy.user

import android.graphics.Bitmap
import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.portfolio.puppy.main.MainDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.mail.Authenticator

class UserViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    lateinit var mIsSuccess: MutableLiveData<Boolean> // 로그인, 회원가입 성공 여부
    lateinit var mIsValidate: MutableLiveData<Boolean> // 이메일 중복 확인
    lateinit var mErrorMessage: MutableLiveData<String> // 에러 메시지
    lateinit var mUploadUserImage: MutableLiveData<Boolean> // 프로필 이미지 업로드
    lateinit var mLoadUserImage: MutableLiveData<String> // 프로필 이미지 불러오기
    lateinit var mDeleteUserImage: MutableLiveData<Boolean> // 프로필 이미지 삭제

    var mAuth: Boolean = false // 이메일 인증 여부

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
                    if (!it.equals("SignIn Failed")) {
                        mAuth = it.toBoolean()
                        mIsSuccess.value = true

                    } else {
                        mIsSuccess.value = false
                    }

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
                }, {
                    mErrorMessage.value = "이메일 중복확인 중 오류가 발생하였습니다."
                })

        disposable.add(data)
    }

    fun validateName(name: String) {
        val data = UserDataSource().validateName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsValidate.value = it.equals("validate false")
                }, {
                    mErrorMessage.value = "닉네임 중복확인 중 오류가 발생하였습니다."
                })

        disposable.add(data)
    }

    fun changeName(email: String, name: String) {
        val data = UserDataSource().changeName(email, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsSuccess.value = it.equals("changeName true")
                }, {
                    mErrorMessage.value = "프로필 변경 중 오류가 발생하였습니다."
                })

        disposable.add(data)
    }

    fun uploadUserImage(email: String, bitmap: Bitmap, imageName: String) {
        val data = UserDataSource().uploadUserImage(email, bitmap, imageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mUploadUserImage.value = true
                }, {
                    mErrorMessage.value = "이미지 업로드 중 오류가 발생하였습니다."
                })

        disposable.add(data)
    }

    fun deleteUserImage(email: String, image: String) {
        val data = UserDataSource().deleteUserImage(email, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mDeleteUserImage.value = true
                }, {
                    mErrorMessage.value = "이미지 삭제 중 오류가 발생하였습니다."
                })

        disposable.add(data)
    }

    fun loadUserImage(email: String) {
        val data = UserDataSource().loadUserImage(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mLoadUserImage.value = it },
                        {
                            mErrorMessage.value = "이미지 로딩 중 오류가 발생하였습니다."
                        })

        disposable.add(data)
    }

    fun sendEmail(title: String, code: String, dest: String) {
        val data = UserDataSource().sendEmail(title, code, dest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {
                    mErrorMessage.value = "이메일 전송 중 오류가 발생하였습니다."
                })

        disposable.add(data)
    }

    fun changeAuth(email: String) {
        val data = UserDataSource().changeAuth(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsSuccess.value = it
                }, {
                    mErrorMessage.value = "이메일 인증 중 오류가 발생하였습니다."
                })

        disposable.add(data)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}