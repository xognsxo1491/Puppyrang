package com.portfolio.puppy.user

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityEditProfileBinding
import com.portfolio.puppy.etc.ImageAPI
import com.portfolio.puppy.etc.PreferencesAPI
import com.portfolio.puppy.main.MainActivity

class EditProfileActivity : AppCompatActivity() {
    private lateinit var mViewModel: UserViewModel
    private lateinit var mBinding: ActivityEditProfileBinding

    private var mValue: String = "null"
    private lateinit var mUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        setSupportActionBar(mBinding.toolbarEditProfile)

        val email = PreferencesAPI(this).getEmail() // 이메일
        mValue = intent.getStringExtra("value").toString() // 닉네임

        // 메인화면에서 넘어왔을 경우
        if (mValue == "main") {
            mBinding.toolbarEditProfile.setNavigationIcon(R.drawable.ic_close_24)
        }

        // 제출 버튼 클릭 이벤트
         mBinding.textViewEditProfileSubmit.setOnClickListener {
             mBinding.progressBarEditProfile.visibility = View.VISIBLE
             mViewModel.validateName(mBinding.inputLayoutEditProfile.editText!!.text.toString())
        }

        // 프로필 사진 저장
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val intent = result.data!!
                mBinding.imageEditProfile.setImageURI(intent.data)
                mUri = intent.data!!
            }
        }

        // 프로필 사진 클릭 이벤트
        mBinding.imageEditProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            resultLauncher.launch(intent)
        }

        // 프로필 수정 완료 이벤트
        mViewModel.mIsSuccess = MutableLiveData()
        mViewModel.mIsSuccess.observe(this, {
            if (it) {
                PreferencesAPI(this).putName(mBinding.inputLayoutEditProfile.editText!!.text.toString())

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("value", "editProfile")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        })

        // 닉네임 중복 체크
        mViewModel.mIsValidate = MutableLiveData()
        mViewModel.mIsValidate.observe(this, {
            if (it) {
                if (this::mUri.isInitialized) { // 이미지 선택 시
                    val bitmap = ImageAPI().resize(this, mUri, 200)

                    mViewModel.uploadImage(email, bitmap)
                    mViewModel.changeName(email, mBinding.inputLayoutEditProfile.editText!!.text!!.toString())

                } else { // 이미지 미선택 시
                    mViewModel.changeName(email, mBinding.inputLayoutEditProfile.editText!!.text!!.toString())
                }
            }
            else {
                mBinding.progressBarEditProfile.visibility = View.INVISIBLE
                mBinding.inputLayoutEditProfile.error = getString(R.string.error_name)
            }
        })

        // 닉네임 형식 체크
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mValue == "main") {
            finish()
        }
    }
}