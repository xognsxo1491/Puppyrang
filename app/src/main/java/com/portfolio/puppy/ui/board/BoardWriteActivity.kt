package com.portfolio.puppy.ui.board

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityBoardWriteBinding
import kotlin.collections.ArrayList

class BoardWriteActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityBoardWriteBinding
    private lateinit var mViewModel: BoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)
        mViewModel = ViewModelProvider(this).get(BoardViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        setSupportActionBar(mBinding.toolbarBoardWrite)
        mBinding.toolbarBoardWrite.setNavigationIcon(R.drawable.ic_close_24)

        val list = ArrayList<Uri>()

        // 이미지 리사이클러뷰
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mBinding.recyclerWriteBoard.layoutManager = layoutManager

        // 이미지 선택 결과
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                mBinding.recyclerWriteBoard.visibility = View.VISIBLE

                val intent = result.data!!
                if (intent.data != null) { // 한개
                    list.add(intent.data!!)
                    mBinding.recyclerWriteBoard.adapter = BoardAdapter(list)

                } else { // 여러개
                    for (i: Int in 0 until result.data!!.clipData!!.itemCount) {
                        list.add(intent.clipData!!.getItemAt(i).uri)
                        mBinding.recyclerWriteBoard.adapter = BoardAdapter(list)
                    }
                }
            }
        }

        mBinding.imageWriteBoard.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            resultLauncher.launch(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}