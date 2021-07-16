package com.portfolio.puppy.data.board

import android.content.Context
import com.portfolio.puppy.util.PreferencesUtil
import java.util.*

class BoardRepository(private val dataSource: BoardDataSource, private val context: Context) {
    private val uuid = UUID.randomUUID().toString()
    private val time = System.currentTimeMillis().toString()

    fun getEmail(): String {
        return PreferencesUtil(context).getEmail()
    }

    fun getName(): String {
        return PreferencesUtil(context).getName()
    }

    fun writeBoard(type: String, title: String, content: String, image1: String, image2: String, image3: String, image4: String, image5: String) =
            dataSource.writeBoard(getEmail(), getName(), type, uuid, title, content, image1, image2, image3, image4, image5, time)
}