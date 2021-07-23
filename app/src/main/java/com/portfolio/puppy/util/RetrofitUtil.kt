package com.portfolio.puppy.util

import retrofit2.Call
import retrofit2.http.*


interface RetrofitUtil {
    companion object {
        const val URL = "http://puppyrang0222.cafe24.com/puppyrang/"
    }

    // 회원가입
    @FormUrlEncoded
    @POST("signUp.php")
    fun signUp(
            @Field("USEREMAIL") userEmail: String,
            @Field("USERPW") userPw: String,
            @Field("USERIMAGE") userImage: String,
            @Field("USERNAME") userName: String,
            @Field("USERAUTH") userAuth: Boolean,
            @Field("USERREPORT") userReport: Int
    ): Call<String>

    // 유저정보 불러오기
    @FormUrlEncoded
    @POST("loadUserData.php")
    fun loadUserData(
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

    // 닉네임 변경
    @FormUrlEncoded
    @POST("changeName.php")
    fun changeName(
            @Field("USEREMAIL") userEmail: String,
            @Field("USERNAME") userName: String
    ): Call<String>

    // 이미지 업로드
    @FormUrlEncoded
    @POST("uploadImage.php")
    fun uploadUserImage(
            @Field("USEREMAIL") userEmail: String, // 이메일
            @Field("USERIMAGE") userImage: String, // 이미지 이름
            @Field("IMAGEDATA") imageData: String, // 이미지 데이터
    ): Call<String>

    // 유저 이미지 불러오기
    @FormUrlEncoded
    @POST("loadUserImage.php")
    fun loadUserImage(
            @Field("USEREMAIL") userEmail: String,
    ): Call<String>

    // 유저 이미지 삭제
    @FormUrlEncoded
    @POST("deleteUserImage.php")
    fun deleteUserImage(
            @Field("USEREMAIL") userEmail: String,
            @Field("USERIMAGE") userImage: String,
    ): Call<String>

    // 이메일 인증
    @FormUrlEncoded
    @POST("changeAuth.php")
    fun changeAuth(
            @Field("USEREMAIL") userEmail: String
    ): Call<String>

    // 게시글 작성
    @FormUrlEncoded
    @POST("writeBoard.php")
    fun writeBoard(
            @Field("USEREMAIL") userEmail: String,
            @Field("USERNAME") userName: String,
            @Field("USERIMAGE") userImage: String,
            @Field("TYPE") type: String,
            @Field("UUID") uuid: String,
            @Field("CONTENT") content: String,
            @Field("IMAGE1") image1: String,
            @Field("IMAGE2") image2: String,
            @Field("IMAGE3") image3: String,
            @Field("IMAGE4") image4: String,
            @Field("IMAGE5") image5: String,
            @Field("IMAGENAME1") imageName1: String,
            @Field("IMAGENAME2") imageName2: String,
            @Field("IMAGENAME3") imageName3: String,
            @Field("IMAGENAME4") imageName4: String,
            @Field("IMAGENAME5") imageName5: String,
            @Field("TIME") time: String,
    ): Call<String>

    // 게시글 불러오기
    @FormUrlEncoded
    @POST("loadBoardData.php")
    fun loadBoardData(
            @Field("TYPE") type: String,
            @Field("NO") no: Int
     ): Call<String>

    // 게시글 작성
    @FormUrlEncoded
    @POST("writeComment.php")
    fun writeComment(
            @Field("TYPE") type: String,
            @Field("UUIDBOARD") uuidBoard: String,
            @Field("UUIDCOMMENT") uuidComment: String,
            @Field("USEREMAIL") userEmail: String,
            @Field("USERNAME") userName: String,
            @Field("USERIMAGE") userImage: String,
            @Field("CONTENT") comment: String,
            @Field("TIME") time: String
    ): Call<String>

    // 댓글 불러오기
    @FormUrlEncoded
    @POST("loadCommentData.php")
    fun loadCommentData(
            @Field("UUIDBOARD") uuidBoard: String,
            @Field("TYPE") type: String
    ): Call<String>

    // 게시글 개수
    @FormUrlEncoded
    @POST("loadBoardCount.php")
    fun loadBoardCount(
            @Field("TYPE") no: String
    ): Call<String>

    @FormUrlEncoded
    @POST("changeBoardCount.php")
    fun changeBoardCount(
            @Field("COMMENT") comment: Int,
            @Field("UUID") uuid: String
    ): Call<String>

    @FormUrlEncoded
    @POST("loadBoardData2.php")
    fun loadBoardData2(
            @Field("TYPE") type: String
    ): Call<String>
}