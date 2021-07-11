package com.portfolio.puppy.etc

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitAPI {
    companion object {
        const val URL = "http://puppyrang0222.cafe24.com/puppyrang/"
    }

    // 회원가입
    @FormUrlEncoded
    @POST("signUp.php")
    fun signUp(
            @Field("USEREMAIL") userEmail: String,
            @Field("USERPW") UserPw: String,
            @Field("USERIMAGE") UserImage: String,
            @Field("USERNAME") UserName: String,
    ): Call<String>

    // 로그인
    @FormUrlEncoded
    @POST("loadUserData.php")
    fun signIn(
        @Field("USEREMAIL") userEmail: String,
        @Field("USERPW") UserPw: String,
    ): Call<String>

    // 이메일 중복 체크
    @FormUrlEncoded
    @POST("validateEmail.php")
    fun validateEmail(
            @Field("USEREMAIL") userEmail: String,
    ): Call<String>

    // 닉네임 중복 체크
    @FormUrlEncoded
    @POST("validateName.php")
    fun validateName(
        @Field("USERNAME") userName: String,
    ): Call<String>

    // 프로필 정보 변경
    @FormUrlEncoded
    @POST("editProfile.php")
    fun editProfile(
        @Field("USEREMAIL") userEmail: String,
        @Field("USERIMAGE") userImage: String,
        @Field("USERNAME") userName: String
    ): Call<String>
}