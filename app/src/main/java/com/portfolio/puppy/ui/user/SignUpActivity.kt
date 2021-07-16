package com.portfolio.puppy.ui.user

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivitySignUpBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : AppCompatActivity(),KodeinAware {
    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()

    private lateinit var mViewModel: UserViewModel
    private lateinit var mBinding: ActivitySignUpBinding
    private lateinit var mPw: Editable

    private var mIsEmail: Boolean = false
    private var mIsPw: Boolean = false
    private var mIsPwCertification: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        mViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        setSupportActionBar(mBinding.toolbarSignUp)
        mBinding.toolbarSignUp.setNavigationIcon(R.drawable.ic_close_24)

        val email = mBinding.inputLayoutSignUpEmail.editText!!
        val pw = mBinding.inputLayoutSignUpPw.editText!!
        val pwCertification = mBinding.inputLayoutSignUpPwCertification.editText!!

        // 회원가입 버튼 클릭 이벤트
        mBinding.buttonSignup.setOnClickListener {
            mBinding.progressBarSignUp.visibility = View.VISIBLE

            // 키보드 내리기
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(pwCertification.windowToken, 0)

            mViewModel.validateEmail(email.text.toString())
        }

        // 회원가입 성공 여부
        mViewModel.mIsSuccess = MutableLiveData()
        mViewModel.mIsSuccess.observe(this, {
            if (it) {
                val intent = Intent(this, EditProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

            } else {
                mBinding.progressBarSignUp.visibility = View.INVISIBLE
            }
        })

        // 에러메시지 출력
        mViewModel.mErrorMessage = MutableLiveData()
        mViewModel.mErrorMessage.observe(this, {

        })

        // 이메일 중복 체크
        mViewModel.mIsValidate = MutableLiveData()
        mViewModel.mIsValidate.observe(this, {
            if (it) {
                mViewModel.signUp(email.text.toString(), pw.text.toString())
            } else {
                mBinding.inputLayoutSignUpEmail.error = getString(R.string.pw_validate)
                mBinding.progressBarSignUp.visibility = View.INVISIBLE
            }
        })

        // 이메일 형식 체크
        email.addTextChangedListener {
            val pattern = android.util.Patterns.EMAIL_ADDRESS

            if (!pattern.matcher(it.toString()).matches()) {
                mIsEmail = false
                mBinding.inputLayoutSignUpEmail.error = getString(R.string.email_error)

            } else {
                mIsEmail = true
                mBinding.inputLayoutSignUpEmail.error = null
            }

            isClickable()
        }

        // 비밀번호 형식 체크
        pw.addTextChangedListener {
            mPw = it!!
            val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#\$%^&*()+|=]{8,16}\$")

            if (regex.matchEntire(it.toString()) != null) {
                mIsPw = true
                mBinding.inputLayoutSignUpPw.error = null

            } else {
                mIsPw = false
                mBinding.inputLayoutSignUpPw.error = getString(R.string.pw_error)
            }

            isClickable()
        }

        // 비밀번호 확인 형식 체크
        pwCertification.addTextChangedListener {
            if (this::mPw.isInitialized) {
                if (pwCertification.text.toString() != mPw.toString()) {
                    mIsPwCertification = false
                    mBinding.inputLayoutSignUpPwCertification.error = getString(R.string.pw_certification_error)
                } else {
                    mIsPwCertification = true
                    mBinding.inputLayoutSignUpPwCertification.error = null
                }
            } else {
                mBinding.inputLayoutSignUpPwCertification.error = getString(R.string.pw_certification_error)
            }

            isClickable()
        }

        mViewModel.mErrorMessage = MutableLiveData()
        mViewModel.mErrorMessage.observe(this, {
            Snackbar.make(mBinding.layoutSignUp, it, Snackbar.LENGTH_LONG).show()
        })
    }

    // 회원가입 버튼 활성화
    private fun isClickable() {
        if (mIsEmail && mIsPw && mIsPwCertification) {
            mBinding.buttonSignup.isClickable = true
            mBinding.buttonSignup.setBackgroundColor(ContextCompat.getColor(this, R.color.color_blue))
        }

        else {
            mBinding.buttonSignup.isClickable = false
            mBinding.buttonSignup.setBackgroundColor(ContextCompat.getColor(this, R.color.color_gray))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}