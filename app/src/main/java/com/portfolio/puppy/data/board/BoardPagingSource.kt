package com.portfolio.puppy.data.board

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject

// 게시글 목록 페이징
class BoardPagingSource(private val no: Int, val type: String) : RxPagingSource<Int, JSONObject>() {
    private lateinit var list: ArrayList<JSONObject>

    override fun getRefreshKey(state: PagingState<Int, JSONObject>): Int {
        return no
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, JSONObject>> {
        val param = params.key ?: no

        return BoardDataSource().loadBoardData(type, param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { toLoadResult(it, param) }
                .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(jsonArray: JSONArray, pageNumber: Int): LoadResult<Int, JSONObject> {
        list = ArrayList()

        return if (jsonArray.length() != 0) {
            for (i: Int in 0 until jsonArray.length()) {
                list.add(jsonArray.getJSONObject(i))
            }

            LoadResult.Page(list, null, pageNumber - 2)

        } else {
            LoadResult.Page(list, null, null)
        }
    }
}