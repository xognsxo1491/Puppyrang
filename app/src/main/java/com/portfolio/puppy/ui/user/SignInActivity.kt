package com.portfolio.puppy.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivitySignInBinding
import com.portfolio.puppy.ui.main.MainActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignInActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()

    private lateinit var mViewModel: UserViewModel
    private lateinit var mBinding: ActivitySignInBinding

    private var mIsEmail: Boolean = false // 이메일 형식 체크
    private var mIsPw: Boolean = false // 비밀번호 형식 체크

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        mViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val email = mBinding.inputLayoutSignInEmail.editText!!
        val pw = mBinding.inputLayoutSignInPw.editText!!

        // 로그인
        mBinding.buttonSignIn.setOnClickListener {
            if (!mIsEmail) {
                mBinding.inputLayoutSignInEmail.error = getString(R.string.email_error)
            }

            if (!mIsPw) {
                mBinding.inputLayoutSignInPw.error = getString(R.string.error_blank)
            }

            if (mIsEmail && mIsPw) {
                mBinding.progressBarSignIn.visibility = View.VISIBLE
                mViewModel.signIn(email.text.toString(), pw.text.toString())
            }

            // 키보드 내리기
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mBinding.inputLayoutSignInPw.windowToken, 0)
        }

        // 회원가입
        mBinding.buttnSignInSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        mViewModel.mIsSuccess = MutableLiveData()
        mViewModel.mIsSuccess.observe(this, {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

            } else {
                Snackbar.make(mBinding.layoutSignIn, getString(R.string.error_signIn), Snackbar.LENGTH_LONG).show()
                mBinding.progressBarSignIn.visibility = View.INVISIBLE
            }
        })

        // 이메일 형식 체크
        email.addTextChangedListener {
            val pattern = android.util.Patterns.EMAIL_ADDRESS

            if (!pattern.matcher(it.toString()).matches()) {
                mIsEmail = false
                mBinding.inputLayoutSignInEmail.error = getString(R.string.email_error)

            } else {
                mIsEmail = true
                mBinding.inputLayoutSignInEmail.error = null
            }
        }

        // 비밀번호 빈칸 확인
        pw.addTextChangedListener {
            if (it.isNullOrBlank()) {
                mIsPw = false
                mBinding.inputLayoutSignInPw.error = getString(R.string.error_blank)

            } else {
                mIsPw = true
                mBinding.inputLayoutSignInPw.error = null
            }
        }

        mViewModel.mErrorMessage = MutableLiveData()
        mViewModel.mErrorMessage.observe(this, {
            Snackbar.make(mBinding.layoutSignIn, it, Snackbar.LENGTH_LONG).show()
        })
    }
}