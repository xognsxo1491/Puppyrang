package com.portfolio.puppy.data.user

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.portfolio.puppy.util.EmailUtil
import com.portfolio.puppy.util.RetrofitUtil
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream

class UserDataSource {

    // 회원가입
    fun signUp(email: String, pw: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.signUp(email, pw, "null", "null", false, 0).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onSuccess("SignUp Succeed")
                        } else {
                            it.onSuccess("SignUp Failed")
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "SignUp 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "SignUp 에러")
                t.printStackTrace()
                it.onError(t)
            }
        })
    }

    // 로그인
    fun signIn(email: String, pw: String) = Single.create<JSONObject> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.loadUserData(email, pw).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        it.onSuccess(jsonObject)

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "SignIn 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "SignIn 에러")
                t.printStackTrace()
                it.onError(t)
            }
        })
    }

    // 이메일 중복 체크
    fun validateEmail(email: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.validateEmail(email).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onSuccess("validate true")

                        } else {
                            it.onSuccess("validate false")
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "validateEmail 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "validate 에러")
                t.printStackTrace()
                it.onError(t)
            }
        })
    }

    // 닉네임 중복 체크
    fun validateName(name: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.validateName(name).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onSuccess("validate true")

                        } else {
                            it.onSuccess("validate false")
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "validateName 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "validate 에러")
                t.printStackTrace()
                it.onError(t)
            }
        })
    }

    // 프로필 이미지 업로드
    fun uploadUserImage(email: String, bitmap: Bitmap, imageName: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

        api.uploadUserImage(email, imageName, byteArray).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val jsonObject = JSONObject(response.body().toString())

                if (jsonObject.optString("result").equals("true")) {
                    it.onSuccess("uploadImage true")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "uploadImage 에러")
                it.onError(t)
                it.onError(t)
            }
        })
    }

    // 프로필 이미지 불러오기
    fun loadUserImage(email: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.loadUserImage(email).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            if (!jsonObject.optString("userImage").equals("null")) {
                                it.onSuccess(jsonObject.optString("userImage"))
                            }
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "loadUserImage 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "loadUserImage 에러")
                t.printStackTrace()
                it.onError(t)
            }
        })
    }

    // 프로필 이미지 삭제
    fun deleteUserImage(email: String, image: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.deleteUserImage(email, image).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onComplete()
                        }

                    } catch (e: Exception) {
                        Log.e("UserViewModel", "deleteUserImage 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "deleteUserImage 에러")
                t.printStackTrace()
                it.onError(t)
            }
        })
    }

    // 닉네임 변경
    fun changeName(email: String, name: String) = Single.create<String> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.changeName(email, name).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onSuccess("changeName true")
                    }

                } catch (e: Exception) {
                    Log.e("UserViewModel", "changeName 에러")
                    it.onError(e)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "changeName 에러")
                t.printStackTrace()
                it.onError(t)
            }
        })
    }

    // 이메일 전송
    fun sendEmail(title: String, code: String, dest: String) = Completable.create {
        try {
            EmailUtil().sendEmail(title, code, dest)
            it.onComplete()

        } catch (e: Exception) {
            it.onError(e)
        }
    }

    fun changeAuth(email: String) = Single.create<Boolean> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.changeAuth(email).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onSuccess(true)

                    } else {
                        Log.e("UserViewModel", "changeAuth 에러")
                        it.onSuccess(false)
                    }

                } catch (e: Exception) {
                    Log.e("UserViewModel", "changeAuth 에러")
                    it.onError(e)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("UserViewModel", "changeAuth 에러")
                t.printStackTrace()
                it.onError(t)
            }
        })
    }

    private fun retrofitBuilder(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(RetrofitUtil.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
    }
}