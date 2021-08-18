package com.portfolio.puppy.data.board

import android.util.Log
import com.portfolio.puppy.util.RetrofitUtil
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONArray
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
                   imageName: String,
                   type: String,
                   uuid: String,
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
                   time: String
                   ) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.writeBoard(email, name, imageName, type, uuid, content, image1, image2, image3, image4, image5, imageName1, imageName2, imageName3, imageName4, imageName5, time)
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

    // 게시글 로드 (페이징)
    fun loadBoardData(type: String, no: Int) = Single.create<JSONArray> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.loadBoardData(type, no).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jArray = JSONArray(response.body().toString())
                    it.onSuccess(jArray)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "loadBoardData 에러")
                it.onError(t)
            }
        })
    }
    
    // 댓글 작성
    fun writeComment(type: String, uuidBoard: String, uuidComment: String, email: String, name: String, image: String, comment: String, time: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.writeComment(type, uuidBoard, uuidComment, email, name, image, comment, time).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onComplete()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "writeComment 에러")
                it.onError(t)
            }
        })
    }

    // 댓글 삭제
    fun deleteComment(uuid: String, value: Int) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        if (value == 1) {
            api.deleteCommentData(uuid).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful && response.body() != null) {
                        val jsonObject = JSONObject(response.body().toString())

                        if (jsonObject.optString("result").equals("true")) {
                            Log.e("테스트", "성공")
                            it.onComplete()
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("BoardViewModel", "deleteComment 에러")
                    it.onError(t)
                }
            })
        }

        else if (value == 2) {
            api.deleteCommentData2(uuid).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful && response.body() != null) {
                        val jsonObject = JSONObject(response.body().toString())

                        if (jsonObject.optString("result").equals("true")) {
                            Log.e("테스트", "성공")
                            it.onComplete()
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("BoardViewModel", "deleteComment 에러")
                    it.onError(t)
                }
            })
        }
    }

    // 댓글 로드
    fun loadCommentData(uuid: String, type: String) = Single.create<JSONArray> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.loadCommentData(uuid, type).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jArray = JSONArray(response.body().toString())
                    it.onSuccess(jArray)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "loadCommentData 에러")
                it.onError(t)
            }
        })
    }

    // 게시글 개수 로드
    fun loadBoardCount(type: String) = Single.create<Int> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.loadBoardCount(type).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onSuccess(jsonObject.optInt("no"))
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "loadBoardCount 에러")
                it.onError(t)
            }
        })
    }

    // 개시글 개수 변경
    fun changeBoardCountPlus(uuid: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.changeBoardCountPlus(uuid).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onComplete()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "changeBoardCountPlus 에러")
                it.onError(t)
            }
        })
    }

    // 개시글 개수 변경
    fun changeBoardCountMinus(uuid: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.changeBoardCountMinus(uuid).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onComplete()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "changeBoardCountMinus 에러")
                it.onError(t)
            }
        })
    }

    // 좋아요
    fun recommend(userEmail: String, userName: String, uuid: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.recommend(userEmail, userName, uuid).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onComplete()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "recommend 에러")
                it.onError(t)
            }
        })
    }

    // 좋아요 취소
    fun oppose(userEmail: String, userName: String, uuid: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        Log.e("테스트" ,"$userEmail and $userName and $uuid")

        api.oppose(userEmail, userName, uuid).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onComplete()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "recommend 에러")
                it.onError(t)
            }
        })
    }

    // 좋아요 불러오기
    fun loadRecommend(userEmail: String, userName: String) = Single.create<JSONArray> {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.loadRecommend(userEmail, userName).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jArray = JSONArray(response.body().toString())
                    it.onSuccess(jArray)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "recommend 에러")
                it.onError(t)
            }
        })
    }

    // 좋아요 개수 (증가)
    fun changeRecommendCountPlus(uuid: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.changeRecommendCountPlus(uuid).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onComplete()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "changeRecommendCountPlus 에러")
                it.onError(t)
            }
        })
    }

    // 좋아요 개수 (감소)
    fun changeRecommendCountMinus(uuid: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.changeRecommendCountMinus(uuid).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onComplete()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "changeRecommendCountMinus 에러")
                it.onError(t)
            }
        })
    }

    fun deleteBoardData(uuid: String, type: String) = Completable.create {
        val retrofit = retrofitBuilder()
        val api = retrofit.create(RetrofitUtil::class.java)

        api.deleteBoardData(uuid, type).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optString("result").equals("true")) {
                        it.onComplete()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("BoardViewModel", "deleteBoardData 에러")
                it.onError(t)
            }
        })
    }

    // 레트로핏 빌더
    private fun retrofitBuilder(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(RetrofitUtil.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
    }
}