package com.portfolio.puppy.data.board

import android.content.Context
import com.portfolio.puppy.util.PreferencesUtil
import java.util.*

class BoardRepository(private val dataSource: BoardDataSource, private val context: Context) {
    private val time = System.currentTimeMillis().toString()

    private fun getEmail(): String {
        return PreferencesUtil(context).getEmail()
    }

    private fun getName(): String {
        return PreferencesUtil(context).getName()
    }

    private fun getProfileImage(): String {
        return PreferencesUtil(context).getProfileImage()
    }

    fun writeBoard(type: String,
                   content: String,
                   image1: String,
                   image2: String,
                   image3: String,
                   image4: String,
                   image5: String,
                   imageName1: String,
                   imageName2: String,
                   imageName3: String,
                   imageName4: String,
                   imageName5: String
                   ) =
            dataSource.writeBoard(getEmail(), getName(), getProfileImage(), type, UUID.randomUUID().toString(), content, image1, image2, image3, image4, image5,
                    imageName1, imageName2, imageName3, imageName4, imageName5, time)

    fun writeComment(type: String, uuidBoard: String, comment: String) =
            dataSource.writeComment(type, uuidBoard, UUID.randomUUID().toString(), getEmail(), getName(), getProfileImage(), comment, time)

    fun loadCommentData(uuid: String, type: String) =
            dataSource.loadCommentData(uuid, type)

    fun deleteCommentData(uuid: String, value: Int) =
            dataSource.deleteComment(uuid, value)

    fun loadBoardCount(type: String) =
            dataSource.loadBoardCount(type)

    fun changeBoardCountPlus(uuid: String) =
            dataSource.changeBoardCountPlus(uuid)

    fun changeBoardCountMinus(uuid: String) =
            dataSource.changeBoardCountMinus(uuid)

    fun recommend(uuid: String) =
            dataSource.recommend(getEmail(), getName(), uuid)

    fun oppose(uuid: String) =
            dataSource.oppose(getEmail(), getName(), uuid)

    fun loadRecommend() =
            dataSource.loadRecommend(getEmail(), getName())

    fun changeRecommendCountPlus(uuid: String) =
            dataSource.changeRecommendCountPlus(uuid)

    fun changeRecommendCountMinus(uuid: String) =
            dataSource.changeRecommendCountMinus(uuid)

    fun deleteBoardData(uuid: String, type: String) =
            dataSource.deleteBoardData(uuid, type)
}