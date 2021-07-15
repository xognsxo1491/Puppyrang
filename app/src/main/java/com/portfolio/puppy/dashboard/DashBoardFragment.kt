package com.portfolio.puppy.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.FragmentDashBoardBinding
import com.portfolio.puppy.home.HomeViewModel

class DashBoardFragment : Fragment() {
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mBinding: FragmentDashBoardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)

        return mBinding.root
    }
}