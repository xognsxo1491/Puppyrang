package com.portfolio.puppy.ui.board.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.portfolio.puppy.R
import com.portfolio.puppy.util.TimeFormatUtil
import org.json.JSONObject

// 게시글 어댑터
class BoardAdapter: PagingDataAdapter<JSONObject, BoardAdapter.ViewHolder>(USER_COMPARATOR) {
    private val userImageUri = "https://puppyrang0222.cafe24.com/puppyrang/images/profile/" // 변경필요
    private lateinit var mContext: Context
    lateinit var mSlide: MutableLiveData<String> // 슬라이드뷰에 uuid 전달

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_board, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.id.text = item.optString("user_name")
            holder.content.text = item.optString("content")
            holder.content2.text = item.optString("content")
            holder.time.text = TimeFormatUtil().formatting(item.optLong("time"))
            holder.recommend.text = item.optString("recommend")
            holder.comment.text = item.optString("comment")

            holder.card.setOnClickListener {
                if (holder.content.visibility == View.VISIBLE) {
                    holder.content.visibility = View.GONE
                    holder.content2.visibility = View.VISIBLE

                } else {
                    holder.content.visibility = View.VISIBLE
                    holder.content2.visibility = View.GONE
                }
            }

            holder.card.setOnLongClickListener {
                Log.e("테스트", "123")
                return@setOnLongClickListener true
            }

            holder.cardComment.setOnClickListener {
                mSlide.value = item.optString("uuid")
            }

            if (item.optString("user_image") != "null") {
                Glide.with(mContext)
                        .load("$userImageUri${item.optString("user_image")}.jpg")
                        .into(holder.profile)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.text_boardItem_id)
        val time: TextView = view.findViewById(R.id.text_boardItem_time)
        val content: TextView = view.findViewById(R.id.text_boardItem_content)
        val content2: TextView = view.findViewById(R.id.text_boardItem_content2)
        val recommend: TextView = view.findViewById(R.id.text_boardItem_recommend)
        val comment: TextView = view.findViewById(R.id.text_boardItem_comment)
        val card: CardView = view.findViewById(R.id.card_item_board)
        val profile: ImageView = view.findViewById(R.id.image_item_board_profile)
        val cardComment: CardView = view.findViewById(R.id.card_board_comment)
    }

    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<JSONObject>() {
            override fun areItemsTheSame(oldItem: JSONObject, newItem: JSONObject): Boolean =
                    oldItem.optString("no") == newItem.optString("no")

            override fun areContentsTheSame(oldItem: JSONObject, newItem: JSONObject): Boolean =
                    newItem.optString("no") == oldItem.optString("no")
        }
    }
}