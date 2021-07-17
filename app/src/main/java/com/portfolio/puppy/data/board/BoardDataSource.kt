package com.portfolio.puppy.data.board

import android.util.Log
import com.portfolio.puppy.util.RetrofitUtil
import io.reactivex.Completable
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class BoardDataSource {

    // 게시글 작성
    fun writeBoard(email: String,
                   name: String,
                   type: String,
                   uuid: String,
                   title: String,
                   content: String,
                   image1: String,
                   image2: String,
                   image3: String,
                   image4: String,
                   image5: String,
                   imageName1: String,
                   imageName2: String,
                   imageName3: String,
                   imageName4: String,
                   imageName5: String,
                   imageCount: Int,
                   time: String
                   ) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.writeBoard(email, name, type, uuid, title, content, image1, image2, image3, image4, image5, imageName1, imageName2, imageName3, imageName4, imageName5, imageCount.toString(), time)
                .enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optString("result").equals("true")) {
                            it.onComplete()
                        }

                    } catch (e: Exception) {
                        Log.e("BoardViewModel", "writeBoard 에러")
                        it.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "writeBoard 에러")
                t.printStackTrace()
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