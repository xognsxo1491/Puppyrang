package com.portfolio.puppy.user

import com.google.gson.annotations.SerializedName

data class UserModel(
        @SerializedName("USEREMAIL")
        var userEmail: String,

        @SerializedName("USERPW")
        var userPw: String,

        @SerializedName("USERIMAGE")
        var userImage: String
)