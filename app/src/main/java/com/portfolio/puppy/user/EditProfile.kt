package com.portfolio.puppy.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityEditProfileBinding
import com.portfolio.puppy.main.MainActivity

class EditProfile : AppCompatActivity() {
    private lateinit var mViewModel: UserViewModel
    private lateinit var mBinding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val email = intent.getStringExtra("userEmail").toString()

         mBinding.textViewEditProfileSubmit.setOnClickListener {
             mBinding.progressBarEditProfile.visibility = View.VISIBLE
             mViewModel.validateName(mBinding.inputLayoutEditProfile.editText!!.text.toString())
        }

        mViewModel.mIsSuccess = MutableLiveData()
        mViewModel.mIsSuccess.observe(this, {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("value", "editProfile")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        })

        mViewModel.mIsValidate = MutableLiveData()
        mViewModel.mIsValidate.observe(this, {
            if (it) {
                mViewModel.editProfile(email, "null", mBinding.inputLayoutEditProfile.editText!!.text!!.toString())
            }
            else {
                mBinding.progressBarEditProfile.visibility = View.INVISIBLE
                mBinding.inputLayoutEditProfile.error = getString(R.string.error_name)
            }
        })


        mBinding.inputLayoutEditProfile.editText!!.addTextChangedListener {
            if (it.toString().length in 8 downTo 2) {
                mBinding.inputLayoutEditProfile.error = null
                mBinding.textViewEditProfileSubmit.isClickable = true
                mBinding.textViewEditProfileSubmit.setTextColor(ContextCompat.getColor(this, R.color.color_blue))

            } else {
                mBinding.inputLayoutEditProfile.error = getString(R.string.nickName_error)
                mBinding.textViewEditProfileSubmit.isClickable = false
                mBinding.textViewEditProfileSubmit.setTextColor(ContextCompat.getColor(this, R.color.color_gray))
            }
        }
    }
}