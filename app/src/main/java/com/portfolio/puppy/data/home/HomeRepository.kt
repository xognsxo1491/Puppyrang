package com.portfolio.puppy.data.home

import android.content.Context

class HomeRepository(private val dataSource: HomeDataSource, private val context: Context) {

    fun loadAdView() = dataSource.loadAdView(context)
}