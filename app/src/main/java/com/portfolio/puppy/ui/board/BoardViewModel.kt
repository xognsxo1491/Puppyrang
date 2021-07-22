package com.portfolio.puppy.ui.board

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.portfolio.puppy.data.board.BoardPagingSource
import com.portfolio.puppy.data.board.BoardRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class BoardViewModel(private val repository: BoardRepository) : ViewModel() {
    private val mDisposable = CompositeDisposable()
    lateinit var mSuccess: MutableLiveData<Boolean>
    lateinit var mError: MutableLiveData<String>
    lateinit var mIsWrite: MutableLiveData<Boolean> // 게시글 작성 양식 체크
    lateinit var mCountBoard: MutableLiveData<Int>
    lateinit var mCountComment: MutableLiveData<Int>
    lateinit var mLoadComment: MutableLiveData<Boolean>
    lateinit var mComment: MutableLiveData<JSONArray>

    fun flowBoard(type: String): Flow<PagingData<JSONObject>> {
        return Pager(PagingConfig(1)) {
            BoardPagingSource(mCountBoard.value!!, type)
        }.flow.cachedIn(viewModelScope)
    }

    fun writeBoard(type: String, content: String, image1: String, image2: String, image3: String, image4: String, image5: String,
                   imageName1: String, imageName2: String, imageName3: String, imageName4: String, imageName5: String) {
        val data = repository.writeBoard(type, content, image1, image2, image3, image4, image5, imageName1, imageName2, imageName3, imageName4, imageName5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mSuccess.value = true
                }, {
                    mError.value = "게시글 작성에 실패하였습니다."
                    mSuccess.value = false
                    it.printStackTrace()
                })

        mDisposable.add(data)
    }

    fun writeComment(type: String, uuid: String, comment: String) {
        val data = repository.writeComment(type, uuid, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           mLoadComment.value = true
                }, {
                    mError.value = "댓글 작성에 실패하였습니다."
                })

        mDisposable.add(data)
    }

    fun loadBoardCount(type: String) {
        val data = repository.loadBoardCount(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           mCountBoard.value = it
                }, {
                    mError.value = "게시글 개수를 불러오는데 실패하였습니다."
                })

        mDisposable.add(data)
    }

    fun changeBoardCount(comment: Int, uuid: String) {
        val data = repository.changeBoardCount(comment, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        mDisposable.add(data)
    }

    fun loadCommentData(uuid: String, type: String) {
        val data = repository.loadCommentData(uuid, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           mComment.value = it
                }, {
                    mError.value = "댓글을 불러오는데 실패하였습니다."
                })

        mDisposable.add(data)
    }

    override fun onCleared() {
        super.onCleared()

        mDisposable.clear()
    }
}