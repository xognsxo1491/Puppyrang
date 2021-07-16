package com.portfolio.puppy.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivitySplashBinding
import com.portfolio.puppy.ui.main.MainActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()

    private lateinit var mViewModel: UserViewModel
    private lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        mViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val email = mViewModel.email
        val pw = mViewModel.pw

        mViewModel.signIn(email, pw)
        mViewModel.mIsSuccess = MutableLiveData()
        mViewModel.mIsSuccess.observe(this, {
            if (it) {
                // 자동 로그인 성공
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

            } else {
                // 자동 로그인 실패
                val intent = Intent(this, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        })

        mViewModel.mErrorMessage = MutableLiveData()
        mViewModel.mErrorMessage.observe(this, {
            Snackbar.make(mBinding.layoutSplash, it, Snackbar.LENGTH_LONG).show()
        })
    }
}