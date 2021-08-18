package com.portfolio.puppy.ui.board

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
    lateinit var mMessage: MutableLiveData<String> // 에러 메세지
    lateinit var mIsWrite: MutableLiveData<Boolean> // 게시글 작성 양식 체크
    lateinit var mCountBoard: MutableLiveData<Int> // 게시글 개수
    lateinit var mLoadComment: MutableLiveData<Boolean> // 댓글 작성 성공 여부
    lateinit var mComment: MutableLiveData<JSONArray> // 댓글 정보
    lateinit var mDelete: MutableLiveData<Boolean> // 댓글 삭제
    lateinit var mRecommend: MutableLiveData<JSONArray> // 좋아요 리스트

    // 게시글 페이징 플로우
    fun flowBoard(type: String): Flow<PagingData<JSONObject>> {
        return Pager(PagingConfig(19)) {
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
                    mMessage.value = "게시글 작성에 실패하였습니다."
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
                    mMessage.value = "댓글 작성에 실패하였습니다."
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
                    mMessage.value = "게시글 개수를 불러오는데 실패하였습니다."
                })

        mDisposable.add(data)
    }

    fun changeBoardCountPlus(uuid: String) {
        val data = repository.changeBoardCountPlus(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        mDisposable.add(data)
    }

    fun changeBoardCountMinus(uuid: String) {
        val data = repository.changeBoardCountMinus(uuid)
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
                    mMessage.value = "댓글을 불러오는데 실패하였습니다."
                })

        mDisposable.add(data)
    }

    fun deleteCommentData(uuid: String, value: Int) {
        val data = repository.deleteCommentData(uuid, value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           mDelete.value = true
                }, {
                    mMessage.value = "댓글을 삭제하는데 실패하였습니다."
                })

        mDisposable.add(data)
    }

    fun recommend(uuid: String) {
        val data = repository.recommend(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           mMessage.value = "좋아요를 눌렀습니다."
                }, {
                    mMessage.value = "좋아요에 실패하였습니다."
                })

        mDisposable.add(data)
    }

    fun oppose(uuid: String) {
        val data = repository.oppose(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mMessage.value = "좋아요를 취소하였습니다."
                }, {
                    mMessage.value = "좋아요 취소에 실패하였습니다."
                })
    }

    fun changeRecommendCountPlus(uuid: String) {
        val data = repository.changeRecommendCountPlus(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        mDisposable.add(data)
    }

    fun changeRecommendCountMinus(uuid: String) {
        val data = repository.changeRecommendCountMinus(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        mDisposable.add(data)
    }

    fun loadRecommend() {
        val data = repository.loadRecommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           mRecommend.value = it
                }, {
                    mMessage.value = "좋아요를 불러오는데 실패하였습니다."
                })

        mDisposable.add(data)
    }

    fun deleteBoardData(uuid: String, type: String) {
        val data = repository.deleteBoardData(uuid, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        mDisposable.add(data)
    }

    override fun onCleared() {
        super.onCleared()

        mDisposable.clear()
    }
}