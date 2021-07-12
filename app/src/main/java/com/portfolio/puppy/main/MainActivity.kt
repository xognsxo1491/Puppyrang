package com.portfolio.puppy.main

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.*
import com.portfolio.puppy.dashboard.DashBoardFragment
import com.portfolio.puppy.databinding.DrawerMainBinding
import com.portfolio.puppy.etc.PreferencesAPI
import com.portfolio.puppy.home.HomeFragment
import com.portfolio.puppy.user.EditProfileActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: DrawerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.drawer_main)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val toolbar = mBinding.includeMain.toolbarMain
        setSupportActionBar(toolbar)

        val email = PreferencesAPI(this).getEmail()
        val name = PreferencesAPI(this).getName()

        val navView = mBinding.navView.getHeaderView(0)
        val navEmail = navView.findViewById<TextView>(R.id.textView_drawer_email)
        val navName = navView.findViewById<TextView>(R.id.textView_drawer_name)
        val navProfile = navView.findViewById<ImageView>(R.id.image_drawer_profile)

        navEmail.text = email
        navName.text = name

        setBottomNavClickListener()

        when (intent.getStringExtra("value")) {
            "editProfile" -> {
                Toast.makeText(this, getString(R.string.success_editProfile), Toast.LENGTH_LONG).show()
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

        mViewModel.mUri.observe(this, {
            if (!it.equals("")) {
                navProfile.setImageURI(Uri.parse(it))
            }
        })

        mBinding.navView.setNavigationItemSelectedListener { it ->
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.navigation_pets -> {
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
        val builder = AlertDialog.Builder(this)
                .setTitle(getString(R.string.message))
                .setMessage(getString(R.string.message_logout))
                .setPositiveButton(getString(R.string.ok)) { dialog: DialogInterface, i: Int ->
                    PreferencesAPI(this).logout()

                    finishAffinity()
                    exitProcess(0)
                }.setNegativeButton(getString(R.string.cancel)) { dialog: DialogInterface, i: Int ->

                }
        val dlg = builder.create()
        dlg.show()
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