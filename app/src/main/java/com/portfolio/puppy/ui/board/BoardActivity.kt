package com.portfolio.puppy.ui.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityBoardBinding
import com.portfolio.puppy.ui.board.adapter.BoardAdapter
import com.portfolio.puppy.ui.board.adapter.BoardCommentAdapter
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class BoardActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: BoardViewModelFactory by instance()

    private lateinit var mBinding: ActivityBoardBinding
    private lateinit var mViewModel: BoardViewModel
    private lateinit var mLayout: SlidingUpPanelLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_board)
        mViewModel = ViewModelProvider(this, factory).get(BoardViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        setSupportActionBar(mBinding.toolbarBoard)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        mBinding.toolbarBoard.setNavigationIcon(R.drawable.ic_arrow_back_24)

        val valueBoard = intent.getStringExtra("board")
        mViewModel.loadBoardCount(valueBoard!!)

        when (intent.getStringExtra("value")) { // 게시글 작성 완료시
            "success" -> Snackbar.make(mBinding.layoutBoard, R.string.success_write, Snackbar.LENGTH_SHORT).show()
        }

        when (valueBoard) { // 게시판 종류 판별
            "free" -> mBinding.textBoardTitle.text = getString(R.string.dashboard_free) // 자유 게시판
        }

        mBinding.fabBoard.setOnClickListener { // 게시글 작성
            val intent = Intent(this, BoardWriteActivity::class.java)
            intent.putExtra("board", valueBoard)
            startActivity(intent)
        }


        val boardAdapter = BoardAdapter()
        mBinding.recyclerBoard.adapter = boardAdapter

        mViewModel.mCountBoard = MutableLiveData()
        mViewModel.mCountBoard.observe(this, {
            if (it != 1) {
                // 페이징
                ///////////////////////////////////////////////////////////////////////////
                lifecycleScope.launch {
                    mViewModel.flowBoard(valueBoard).collectLatest { emitter ->
                        boardAdapter.submitData(emitter)
                    }
                }
                ///////////////////////////////////////////////////////////////////////////
            }
        })

        mLayout = mBinding.slide
        var uuid = ""
        var list: ArrayList<JSONObject>
        var commentAdapter: BoardCommentAdapter

        // 슬라이드뷰 확장
        boardAdapter.mSlide = MutableLiveData()
        boardAdapter.mSlide.observe(this, {
            if (!it.isNullOrEmpty()) {
                uuid = it
                mLayout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                mViewModel.loadCommentData(uuid, valueBoard)
            }
        })

        // 새로고침
        mBinding.refreshBoard.setOnRefreshListener {
            boardAdapter.refresh()
            boardAdapter.notifyDataSetChanged()
            mBinding.refreshBoard.isRefreshing = false
        }

        // 오류 발생 메세지
        mViewModel.mError = MutableLiveData()
        mViewModel.mError.observe(this, {
            Snackbar.make(mBinding.layoutBoard, it, Snackbar.LENGTH_LONG).show()
        })

        // 댓글 작성
        mBinding.imageBoardSend.setOnClickListener {
            mViewModel.writeComment(valueBoard, uuid, mBinding.textInputLayoutBoardComment.editText!!.text.toString())
        }

        // 댓글 작성 완료시
        mViewModel.mLoadComment = MutableLiveData()
        mViewModel.mLoadComment.observe(this, {
            if (it) {
                // 키보드 내리기
                mViewModel.changeBoardCount(0, uuid)

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                mBinding.textInputLayoutBoardComment.editText!!.text.clear()
                Snackbar.make(mBinding.layoutBoard, getString(R.string.success_comment), Snackbar.LENGTH_LONG).show()

                mViewModel.loadCommentData(uuid, valueBoard)
            }
        })

        // 댓글 양식 체크
        mBinding.textInputLayoutBoardComment.editText!!.addTextChangedListener {
            if (it!!.length in 500 downTo 1) {
                mBinding.imageBoardSend.visibility = View.VISIBLE

            } else {
                mBinding.imageBoardSend.visibility = View.INVISIBLE
            }
        }

        // 댓글 불러오기
        mViewModel.mComment = MutableLiveData()
        mViewModel.mComment.observe(this, {
            list = ArrayList()

            if (!it.isNull(0)) {
                for (i: Int in 0 until it.length()) {
                    list.add(it.getJSONObject(i))
                }
            }

            commentAdapter = BoardCommentAdapter(list)
            mBinding.recyclerComment.adapter = commentAdapter
            commentAdapter.notifyDataSetChanged()
        })

        // 댓글 새로 고침
        mBinding.refreshComment.setOnRefreshListener {
            mViewModel.loadCommentData(uuid, valueBoard)
            mBinding.refreshComment.isRefreshing = false
        }

        // 슬라이드뷰 상태 변화
        mBinding.slide.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) { }

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                if (mLayout.panelState == SlidingUpPanelLayout.PanelState.ANCHORED || mLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mBinding.textInputLayoutBoardComment.editText!!.requestFocus()
                    mBinding.slide.isTouchEnabled = false
                }

                if (mLayout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(View(this@BoardActivity).windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mLayout.panelState == SlidingUpPanelLayout.PanelState.ANCHORED || mLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED

        } else {
            super.onBackPressed()
        }
    }
}