package com.portfolio.puppy.user

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
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityEmailAuthBinding
import com.portfolio.puppy.etc.PreferencesAPI
import com.portfolio.puppy.main.MainActivity
import java.util.*
import java.util.concurrent.TimeUnit

class EmailAuthActivity : AppCompatActivity() {
    private lateinit var mViewModel: UserViewModel
    private lateinit var mBinding: ActivityEmailAuthBinding

    private var mCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_email_auth)
        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val email = PreferencesAPI(this).getEmail()

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
            }
        }

        // 인증 성공
        mViewModel.mIsSuccess = MutableLiveData()
        mViewModel.mIsSuccess.observe(this, {
            if (it) {
                PreferencesAPI(this).putAuth(true)

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("value", "AuthEmail")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                mBinding.progressEmailAuth.visibility = View.INVISIBLE
            }
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