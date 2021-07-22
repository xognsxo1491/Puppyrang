package com.portfolio.puppy.ui.user

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.portfolio.puppy.data.user.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val disposable = CompositeDisposable()
    lateinit var mIsSuccess: MutableLiveData<Boolean> // 로그인, 회원가입 성공 여부
    lateinit var mIsValidate: MutableLiveData<Boolean> // 이메일 중복 확인
    lateinit var mErrorMessage: MutableLiveData<String> // 에러 메시지
    lateinit var mUploadUserImage: MutableLiveData<Boolean> // 프로필 이미지 업로드
    lateinit var mLoadUserImage: MutableLiveData<String> // 프로필 이미지 불러오기
    lateinit var mDeleteUserImage: MutableLiveData<Boolean> // 프로필 이미지 삭제

    val email = repository.getEmail() // 이메일
    val pw = repository.getPw() // 비밀번호
    var name = repository.getName() // 닉네임

    var auth: Boolean = false // 이메일 인증 여부

    fun putAuth(bool: Boolean) {
        repository.putAuth(bool)
    }

    fun putProfileImage(imageName: String) {
        repository.putProfileImage(imageName)
    }

    fun deleteUserImage() {
        repository.deleteProfileImage()
    }

    fun signUp(email: String, pw: String) {
        val data = repository.signUp(email, pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsSuccess.value = it.equals("SignUp Succeed")
                    repository.putUserData(email, pw)
                }, {
                    mErrorMessage.value = "회원가입에 실패하였습니다."
                })

        disposable.add(data)
    }

    fun signIn(email: String, pw: String) {
        val data = repository.signIn(email, pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.optString("result").equals("true")) {
                        mIsSuccess.value = true
                        auth = it.optString("userAuth").equals("1")

                        repository.putUserData(it.optString("userEmail"), it.optString("userPw"))
                        repository.putName(it.optString("userName"))
                        repository.putAuth(auth)

                    } else {
                        mIsSuccess.value = false
                    }

                }, {
                    mErrorMessage.value = "로그인에 실패하였습니다."
                })

        disposable.add(data)
    }

    fun validateEmail(email: String) {
        val data = repository.validateEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsValidate.value = it.equals("validate false")
                }, {
                    mErrorMessage.value = "이메일 중복확인에 실패하였습니다."
                })

        disposable.add(data)
    }

    fun validateName(name: String) {
        val data = repository.validateName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsValidate.value = it.equals("validate false")
                }, {
                    mErrorMessage.value = "닉네임 중복확인에 실패하였습니다."
                })

        disposable.add(data)
    }

    fun changeName(email: String, name: String) {
        val data = repository.changeName(email, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsSuccess.value = it.equals("changeName true")
                    if (mIsSuccess.value!!) {
                        repository.putName(name)
                    }
                }, {
                    mErrorMessage.value = "프로필 변경에 실패하였습니다."
                })

        disposable.add(data)
    }

    fun uploadUserImage(email: String, bitmap: Bitmap, imageName: String) {
        val data = repository.uploadUserImage(email, bitmap, imageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mUploadUserImage.value = true
                    putProfileImage(imageName)
                }, {
                    mErrorMessage.value = "이미지 업로드에 실패하였습니다."
                })

        disposable.add(data)
    }

    fun deleteUserImage(email: String) {
        val data = repository.deleteUserImage(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mDeleteUserImage.value = true
                }, {
                    mErrorMessage.value = "이미지 삭제에 실패하였습니다."
                })

        disposable.add(data)
    }

    fun loadUserImage(email: String) {
        val data = repository.loadUserImage(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mLoadUserImage.value = it },
                        {
                            mErrorMessage.value = "이미지 로딩에 실패하였습니다."
                        })

        disposable.add(data)
    }

    fun sendEmail(title: String, code: String, dest: String) {
        val data = repository.sendEmail(title, code, dest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {
                    mErrorMessage.value = "이메일 전송에 실패하였습니다."
                })

        disposable.add(data)
    }

    fun changeAuth(email: String) {
        val data = repository.changeAuth(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsSuccess.value = it
                }, {
                    mErrorMessage.value = "이메일 변경에 실패하였습니다."
                })

        disposable.add(data)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}