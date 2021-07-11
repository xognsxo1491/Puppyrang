package com.portfolio.puppy.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        mViewModel.loadAdView(mContext, mBinding.adView)
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onPause() {
        mBinding.adView.pause()
        super.onPause()
    }

    override fun onResume() {
        mBinding.adView.resume()
        super.onResume()
    }

    override fun onDestroy() {
        mBinding.adView.destroy()
        super.onDestroy()
    }
}