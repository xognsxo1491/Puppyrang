package com.portfolio.puppy.ui.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityBoardBinding
import com.portfolio.puppy.ui.main.MainViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class BoardActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: BoardViewModelFactory by instance()

    private lateinit var mBinding: ActivityBoardBinding
    private lateinit var mViewModel: BoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_board)
        mViewModel = ViewModelProvider(this, factory).get(BoardViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        setSupportActionBar(mBinding.toolbarBoard)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        mBinding.toolbarBoard.setNavigationIcon(R.drawable.ic_arrow_back_24)

        when (intent.getStringExtra("value")) {
            "free" -> mBinding.textBoardTitle.text = getString(R.string.dashboard_free) // 자유 게시판
            "success" -> Snackbar.make(mBinding.layoutBoard, R.string.success_write, Snackbar.LENGTH_SHORT).show()
        }

        mBinding.fabBoard.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            intent.putExtra("value", "free")
            startActivity(intent)
        }

        mViewModel.mError = MutableLiveData()
        mViewModel.mError.observe(this, {
            Snackbar.make(mBinding.layoutBoard, it, Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}