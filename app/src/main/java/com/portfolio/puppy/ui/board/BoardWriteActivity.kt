package com.portfolio.puppy.ui.board

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityBoardWriteBinding
import com.portfolio.puppy.ui.board.adapter.BoardWriteAdapter
import com.portfolio.puppy.util.ImageUtil
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

class BoardWriteActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: BoardViewModelFactory by instance()

    private lateinit var mBinding: ActivityBoardWriteBinding
    private lateinit var mViewModel: BoardViewModel
    private val mList = ArrayList<Uri>()

    private var bCount = true // 사진 개수 확인

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)
        mViewModel = ViewModelProvider(this, factory).get(BoardViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        setSupportActionBar(mBinding.toolbarBoardWrite)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        mBinding.toolbarBoardWrite.setNavigationIcon(R.drawable.ic_close_24)

        val type = intent.getStringExtra("board")
        val content = mBinding.textInputLayoutBoardWriteContent.editText

        // 이미지 리사이클러뷰
        val layoutManager = GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false)
        mBinding.recyclerWriteBoard.layoutManager = layoutManager

        val adapter = BoardWriteAdapter(mList)
        mBinding.recyclerWriteBoard.adapter = adapter

        // 이미지 선택 런처
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                mBinding.recyclerWriteBoard.visibility = View.VISIBLE

                val intent = result.data!!
                if (intent.data != null) { // 한개
                    mList.add(intent.data!!)
                    adapter.notifyDataSetChanged()

                } else { // 여러개
                    for (i: Int in 0 until intent.clipData!!.itemCount) {
                        mList.add(intent.clipData!!.getItemAt(i).uri)
                        adapter.notifyDataSetChanged()
                    }
                }

                mBinding.textBoardWriteCount.text = "${mList.size}/5"
                if (mList.size > 5) {
                    bCount = false
                    mBinding.textBoardWriteCount.setTextColor(ContextCompat.getColor(this, R.color.color_pink_error))

                } else {
                    bCount = true
                    mBinding.textBoardWriteCount.setTextColor(ContextCompat.getColor(this, R.color.color_gray))
                }
            }
        }

        // 이미지 첨부
        mBinding.fabBoardWrite.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            resultLauncher.launch(intent)
        }

        //  성공 여부
        mViewModel.mSuccess = MutableLiveData()
        mViewModel.mSuccess.observe(this, {
            if (it) {
                val intent = Intent(this, BoardActivity::class.java)
                intent.putExtra("value", "success")
                intent.putExtra("board", type)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            } else {
                mBinding.progressBoardWrite.visibility = View.GONE
            }
        })

        mViewModel.mIsWrite = MutableLiveData(false)
        mViewModel.mIsWrite.observe(this, {
            if (it) {
                mBinding.textViewBoardWriteSubmit
                        .setTextColor(ContextCompat.getColor(this, R.color.color_blue))
            } else {
                mBinding.textViewBoardWriteSubmit
                        .setTextColor(ContextCompat.getColor(this, R.color.color_gray))
            }
        })

        // 제출 버튼 클릭
        mBinding.textViewBoardWriteSubmit.setOnClickListener {
            if (mViewModel.mIsWrite.value!! && bCount) {
                mBinding.progressBoardWrite.visibility = View.VISIBLE

                if (mList.count() == 0) {
                    mViewModel.writeBoard(type.toString(), content!!.text.toString(),
                            "", "", "", "", "", "", "", "", "", "")

                } else {
                    when (mList.size) {
                        1 -> {
                            mViewModel.writeBoard(type.toString(), content!!.text.toString(), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[0], this)), "", "", "", "",
                                    UUID.randomUUID().toString(), "", "", "", "")
                        }

                        2 -> {
                            mViewModel.writeBoard(type.toString(), content!!.text.toString(), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[0], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[1], this)), "", "", "",
                                    UUID.randomUUID().toString(), UUID.randomUUID().toString(), "", "", "")
                        }

                        3 -> {
                            mViewModel.writeBoard(type.toString(), content!!.text.toString(), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[0], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[1], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[2], this)), "", "",
                                    UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), "", "")
                        }

                        4 -> {
                            mViewModel.writeBoard(type.toString(), content!!.text.toString(), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[0], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[1], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[2], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[3], this)), "",
                                    UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), "")
                        }

                        5 -> {
                            mViewModel.writeBoard(type.toString(), content!!.text.toString(), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[0], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[1], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[2], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[3], this)), ImageUtil().bitmapToString(ImageUtil()
                                    .getCapturedImage(mList[4], this)),
                                    UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())
                        }
                    }
                }
            }
        }

        adapter.imageCount.observe(this, {
            mBinding.textBoardWriteCount.text = "$it/5"

            if (it <= 5) {
                bCount = true
                mBinding.textBoardWriteCount.setTextColor(ContextCompat.getColor(this, R.color.color_gray))
            }
        })

        content!!.addTextChangedListener {
            mViewModel.mIsWrite.value = it!!.length in 1..1000
        }

        mViewModel.mMessage = MutableLiveData()
        mViewModel.mMessage.observe(this, {
            Snackbar.make(mBinding.layoutBoardWrite, it, Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}