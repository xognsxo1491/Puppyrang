package com.portfolio.puppy.ui.board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.FragmentDashBoardBinding

class DashBoardFragment : Fragment() {
    private lateinit var mViewModel: BoardViewModel
    private lateinit var mBinding: FragmentDashBoardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewModel = ViewModelProvider(this).get(BoardViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)

        mBinding.cardDashBoardFree.setOnClickListener {
            val intent = Intent(context, BoardActivity::class.java)
            intent.putExtra("value", "free")
            startActivity(intent)
        }

        return mBinding.root
    }
}