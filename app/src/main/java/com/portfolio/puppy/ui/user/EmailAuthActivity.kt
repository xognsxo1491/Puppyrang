package com.portfolio.puppy.ui.user

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityEmailAuthBinding
import com.portfolio.puppy.ui.main.MainActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import java.util.concurrent.TimeUnit

class EmailAuthActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()

    private lateinit var mViewModel: UserViewModel
    private lateinit var mBinding: ActivityEmailAuthBinding

    private var mCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_email_auth)
        mViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val email = mViewModel.email

        setSupportActionBar(mBinding.toolbarEmailAuth)
        mBinding.toolbarEmailAuth.setNavigationIcon(R.drawable.ic_close_24)
        mBinding.buttonSubmit.isClickable = false

        // 인증 버튼
        mBinding.buttonEmailAuth.setOnClickListener {
            mBinding.buttonSubmit.setBackgroundColor(ContextCompat.getColor(this, R.color.color_blue))
            mBinding.buttonSubmit.isClickable = true

            mCode = numberGen()

            val timer = object: CountDownTimer(180000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hms = String.format(Locale.KOREA,
                            "남은 시간:  %02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))

                    mBinding.textInputLayoutEmailAuth.helperText = hms
                }

                override fun onFinish() {
                    mCode = numberGen()
                    mBinding.textInputLayoutEmailAuth.helperText = null
                }
            }
            timer.start()

            mViewModel.sendEmail(getString(R.string.title_email),
                    "${getString(R.string.message_email)}\n인증 번호: $mCode\n${getString(R.string.alert_timer)}",
                    "xognsxo1491@naver.com")
        }

        // 제출 버튼
        mBinding.buttonSubmit.setOnClickListener {
            mBinding.progressEmailAuth.visibility = View.VISIBLE

            if (mCode == mBinding.textInputLayoutEmailAuth.editText!!.text.toString()) {
                mViewModel.changeAuth(email)

            } else {
                mBinding.progressEmailAuth.visibility = View.INVISIBLE
                Snackbar.make(mBinding.layoutEmailAuth, R.string.error_auth, Snackbar.LENGTH_LONG).show()
            }
        }

        // 인증 성공
        mViewModel.mIsSuccess = MutableLiveData()
        mViewModel.mIsSuccess.observe(this, {
            if (it) {
                mViewModel.putAuth(true)

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("value", "AuthEmail")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                mBinding.progressEmailAuth.visibility = View.INVISIBLE
            }
        })

        mViewModel.mErrorMessage = MutableLiveData()
        mViewModel.mErrorMessage.observe(this, {
            Snackbar.make(mBinding.layoutEmailAuth, it, Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun numberGen(): String {
        val rand = Random()
        var numStr = ""

        for (i: Int in 0..5) {
            val ran = rand.nextInt(10).toString()
            numStr += ran
        }

        return numStr
    }
}