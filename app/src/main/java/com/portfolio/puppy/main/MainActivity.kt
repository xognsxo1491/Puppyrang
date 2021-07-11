package com.portfolio.puppy.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.portfolio.puppy.*
import com.portfolio.puppy.dashboard.DashBoardFragment
import com.portfolio.puppy.databinding.DrawerMainBinding
import com.portfolio.puppy.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: DrawerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.drawer_main)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val toolbar: androidx.appcompat.widget.Toolbar = mBinding.includeMain.toolbarMain
        setSupportActionBar(toolbar)

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

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }

    }
}