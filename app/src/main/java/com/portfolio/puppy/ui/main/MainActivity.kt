package com.portfolio.puppy.ui.main

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.portfolio.puppy.*
import com.portfolio.puppy.ui.board.DashBoardFragment
import com.portfolio.puppy.databinding.DrawerMainBinding
import com.portfolio.puppy.ui.home.HomeFragment
import com.portfolio.puppy.ui.user.EditProfileActivity
import com.portfolio.puppy.ui.user.EmailAuthActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), KodeinAware {
    private val userImageUri = "https://puppyrang0222.cafe24.com/puppyrang/images/profile/" // 변경 필요

    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: DrawerMainBinding

    private lateinit var mEmail: String
    private lateinit var mNavProfile: ImageView
    private var mAuth: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.drawer_main)
        mViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val toolbar = mBinding.includeMain.toolbarMain
        setSupportActionBar(toolbar)

        mEmail = mViewModel.email
        val name = mViewModel.name
        mAuth = mViewModel.auth

        if (name == "null") {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        val navView = mBinding.navView.getHeaderView(0)
        val navEmail = navView.findViewById<TextView>(R.id.textView_drawer_email)
        val navName = navView.findViewById<TextView>(R.id.textView_drawer_name)
        mNavProfile = navView.findViewById(R.id.image_drawer_profile)
        val progressBar = navView.findViewById<ProgressBar>(R.id.progressBar_drawer)

        navEmail.text = mEmail
        navName.text = name

        setBottomNavClickListener()

        when (intent.getStringExtra("value")) {
            "editProfile" -> {
                Snackbar.make(mBinding.drawerLayout, getString(R.string.success_editProfile), Snackbar.LENGTH_LONG).show()
            }

            "AuthEmail" -> {
                Snackbar.make(mBinding.drawerLayout, getString(R.string.auth_success), Snackbar.LENGTH_LONG).show()
            }
        }

        // BottomNav Fragment 상태 변화 감지
        mViewModel.fragmentStatus.observe(this, {
            when (it) {
                1 -> {
                    val homeFragment = HomeFragment()
                    loadFragment(homeFragment)
                }

                2 -> {
                    val dashBoardFragment = DashBoardFragment()
                    loadFragment(dashBoardFragment)
                }

                3 -> {
                    val notificationFragment = NotificationFragment()
                    loadFragment(notificationFragment)
                }
            }
        })

        mViewModel.mLoadUserImage = MutableLiveData()
        mViewModel.mLoadUserImage.observe(this, {
            if (!it.equals("null")) {
                Glide.with(this)
                        .load("$userImageUri$it.jpg")
                        .into(mNavProfile)
                progressBar.visibility = View.INVISIBLE

            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })

        mBinding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_logout -> {
                    showDialogLogout()
                    true
                }

                R.id.navigation_editProfile -> {
                    val intent = Intent(this, EditProfileActivity::class.java)
                    intent.putExtra("value", "main")
                    startActivity(intent)

                    true
                }

                else -> {
                    false
                }
            }
        }

        mViewModel.mErrorMessage = MutableLiveData()
        mViewModel.mErrorMessage.observe(this, {
            Snackbar.make(mBinding.drawerLayout, it, Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onResume() {
        super.onResume()

        // 이메일 인증
        if (!mAuth) {
            Snackbar.make(mBinding.drawerLayout, getString(R.string.email_authentication), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(ContextCompat.getColor(this, R.color.color_blue))
                    .setAction(getString(R.string.done)) {
                        val intent = Intent(this, EmailAuthActivity::class.java)
                        startActivity(intent)
                    }.show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.navigation_profile -> {
                mNavProfile.setImageDrawable(null)
                mViewModel.loadUserImage(mEmail)
                mBinding.drawerLayout.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Fragment 불러오기
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_main, fragment)
        transaction.commit()
    }

    // BottomNav 클릭 이벤트
    private fun setBottomNavClickListener() {
        mBinding.includeMain.bottomNavMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    mViewModel.updateFragmentStatus(1)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_dashboard -> {
                    mViewModel.updateFragmentStatus(2)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_notification -> {
                    mViewModel.updateFragmentStatus(3)
                    return@setOnItemSelectedListener true
                }
            }

            false
        }
    }

    // 로그아웃 다이얼로그
    private fun showDialogLogout() {
        val builder = MaterialAlertDialogBuilder(this, R.style.AlertDialogStyle)
                .setTitle(getString(R.string.message))
                .setMessage(getString(R.string.message_logout))
                .setPositiveButton(getString(R.string.ok)) { _: DialogInterface, _: Int ->
                    mViewModel.logout()

                    finishAffinity()
                    exitProcess(0)
                }.setNegativeButton(getString(R.string.cancel)) { _: DialogInterface, _: Int ->

                }
        val dlg = builder.create()
        dlg.show()
        dlg.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.color_gray))
        dlg.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.color_blue))
    }

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.END)

        } else {
            finishAffinity()
            exitProcess(0)
        }
    }
}