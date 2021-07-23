package com.portfolio.puppy.ui.board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.FragmentDashBoardBinding
import com.portfolio.puppy.ui.board.adapter.DashBoardAdapter
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DashBoardFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: BoardViewModelFactory by instance()

    private lateinit var mViewModel: BoardViewModel
    private lateinit var mBinding: FragmentDashBoardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)
        mViewModel = ViewModelProvider(this, factory).get(BoardViewModel::class.java)

        mViewModel.loadBoardData("free") // 자유게시판

        mViewModel.mLoadFree = MutableLiveData()
        mViewModel.mLoadFree.observe(viewLifecycleOwner, {
            val list = ArrayList<JSONObject>()
            for (i: Int in 0 until it.length()) {
                list.add(it.getJSONObject(i))
            }

            val adapter = DashBoardAdapter(list)
            mBinding.recyclerDashBoardFree.adapter = adapter
        })

        return mBinding.root
    }
}