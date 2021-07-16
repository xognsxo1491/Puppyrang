package com.portfolio.puppy.ui.user

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.R
import com.portfolio.puppy.databinding.ActivityEditProfileBinding
import com.portfolio.puppy.util.ImageUtil
import com.portfolio.puppy.ui.main.MainActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class EditProfileActivity : AppCompatActivity(), KodeinAware {
    private val userImageUri = "https://puppyrang0222.cafe24.com/puppyrang/images/"

    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()

    private lateinit var mViewModel: UserViewModel
    private lateinit var mBinding: ActivityEditProfileBinding

    private var mValue: String = "null" // 액티비티 분류값

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        mViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        setSupportActionBar(mBinding.toolbarEditProfile)

        val email = mViewModel.email // 이메일
        val name = mViewModel.name
        val nameEdit = mBinding.inputLayoutEditProfile.editText!!.text

        mValue = intent.getStringExtra("value").toString()
        mViewModel.loadUserImage(email)

        // 메인화면에서 넘어왔을 경우
        if (mValue == "main") {
            mBinding.toolbarEditProfile.setNavigationIcon(R.drawable.ic_close_24)
            mBinding.textEditTitle.text = getString(R.string.change_profile)
            nameEdit.replace(0, nameEdit.length, name)
        }

        // 제출 버튼 클릭 이벤트
         mBinding.textViewEditProfileSubmit.setOnClickListener {
             mBinding.progressBarEditProfile.visibility = View.VISIBLE
             mViewModel.validateName(nameEdit.toString())
        }

        mBinding.textViewEditProfileSubmit.isClickable = false

        // 프로필 사진 저장
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val intent = result.data!!
                mBinding.imageEditProfile.setImageURI(intent.data)

                val imageName = UUID.randomUUID().toString()
                mViewModel.putProfileImage(imageName)

                val bitmap = ImageUtil()
                    .resize(this, intent.data!!, 200)
                mViewModel.uploadUserImage(email, bitmap, imageName)
            }
        }

        // 프로필 이미지 업로드 메세지
        mViewModel.mUploadUserImage = MutableLiveData()
        mViewModel.mUploadUserImage.observe(this, {
            if (it) {
                Snackbar.make(mBinding.layoutEditProfile, getString(R.string.profileImage_upload), Snackbar.LENGTH_LONG).show()
            }
        })

        // 프로필 이미지 삭제 메세지
        mViewModel.mDeleteUserImage = MutableLiveData()
        mViewModel.mDeleteUserImage.observe(this, {
            Snackbar.make(mBinding.layoutEditProfile, getString(R.string.profileImage_delete), Snackbar.LENGTH_LONG).show()
        })

        // 프로필 이미지 로딩 메시지
        mViewModel.mLoadUserImage = MutableLiveData()
        mViewModel.mLoadUserImage.observe(this, {
            Glide.with(this)
                    .load("$userImageUri$it.jpg")
                    .into(mBinding.imageEditProfile)
        })

        // 프로필 사진 클릭 이벤트
        mBinding.imageEditProfile.setOnClickListener {
            val items = arrayOf<CharSequence>("이미지 선택", "이미지 삭제")

            if (mValue == "main") {
                val builder = AlertDialog.Builder(this)
                builder.setItems(items) { _, which ->
                    when (which) {
                        0 -> { // 이미지 선택
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                            resultLauncher.launch(intent)
                        }
                        
                        1 -> { // 이미지 삭제
                            mBinding.imageEditProfile.setImageDrawable(null)
                            mViewModel.deleteUserImage(email, mViewModel.image)
                        }
                    }
                }.create().show()

            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                resultLauncher.launch(intent)
            }
        }

        // 프로필 수정 완료 이벤트
        mViewModel.mIsSuccess = MutableLiveData()
        mViewModel.mIsSuccess.observe(this, {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)

                if (mValue == "main") {
                    intent.putExtra("value", "editProfile")
                }
                if (mValue == "signUp") {
                    intent.putExtra("value", "signUp")
                }

                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        })

        // 닉네임 중복 체크
        mViewModel.mIsValidate = MutableLiveData()
        mViewModel.mIsValidate.observe(this, {
            if (it) {
                mViewModel.changeName(email, nameEdit.toString())

            } else {
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

        mViewModel.mErrorMessage = MutableLiveData()
        mViewModel.mErrorMessage.observe(this, {
            Snackbar.make(mBinding.layoutEditProfile, it, Snackbar.LENGTH_LONG).show()
        })
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