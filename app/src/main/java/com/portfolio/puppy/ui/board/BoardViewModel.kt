package com.portfolio.puppy.ui.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.portfolio.puppy.data.board.BoardRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BoardViewModel(private val repository: BoardRepository) : ViewModel() {
    private val mDisposable = CompositeDisposable()
    lateinit var mSuccess: MutableLiveData<Boolean>
    lateinit var mError: MutableLiveData<String>
    lateinit var mIsWrite: MutableLiveData<Boolean> // 게시글 작성 양식 체크

    fun writeBoard(type: String, title: String, content: String, image1: String, image2: String, image3: String, image4: String, image5: String,
                   imageName1: String, imageName2: String, imageName3: String, imageName4: String, imageName5: String, imageCount: Int) {
        val data = repository.writeBoard(type, title, content, image1, image2, image3, image4, image5, imageName1, imageName2, imageName3, imageName4, imageName5, imageCount)
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
}