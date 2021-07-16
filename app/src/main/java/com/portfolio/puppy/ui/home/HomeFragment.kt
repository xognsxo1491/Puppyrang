package com.portfolio.puppy.ui.home

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
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment: Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()

    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mViewModel: HomeViewModel

    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        mViewModel.loadAdView(mBinding.adView)
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