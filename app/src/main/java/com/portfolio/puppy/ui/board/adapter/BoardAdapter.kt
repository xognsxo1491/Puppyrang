package com.portfolio.puppy.ui.board.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.portfolio.puppy.R
import com.portfolio.puppy.util.PreferencesUtil
import com.portfolio.puppy.util.TimeFormatUtil
import org.json.JSONArray
import org.json.JSONObject

// 게시글 어댑터
class BoardAdapter(owner: LifecycleOwner): PagingDataAdapter<JSONObject, BoardAdapter.ViewHolder>(USER_COMPARATOR) {
    private val userImageUri =  "https://puppyrang0222.cafe24.com/puppyrang/images/profile/" // 변경 필요
    private val boardUri =  "https://puppyrang0222.cafe24.com/puppyrang/images/" // 변경 필요
    private lateinit var mContext: Context
    private var mOwner: LifecycleOwner = owner
    lateinit var mSlide: MutableLiveData<String> // 슬라이드뷰에 uuid 전달
    lateinit var mRecommend: MutableLiveData<String> // 좋아요
    lateinit var mArray: MutableLiveData<JSONArray> // 좋아요 리스트
    lateinit var mDelete: MutableLiveData<String> // 게시글 삭제

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
               if (PreferencesUtil(mContext).getEmail() == item.optString("user_email")) {
                   val items = arrayOf<CharSequence>("게시글 삭제")
                   val builder = AlertDialog.Builder(mContext)

                   builder.setItems(items) { _, _ ->
                       holder.card.visibility = View.GONE
                       mDelete.value = item.optString("uuid")
                   }.create().show()
               }

                else {
                   val items = arrayOf<CharSequence>("쪽지 보내기")
                   val builder = AlertDialog.Builder(mContext)

                   builder.setItems(items) { _, _ ->

                   }.create().show()
               }

                return@setOnLongClickListener true
            }

            holder.cardComment.setOnClickListener { // 댓글창 보기
                mSlide.value = item.optString("uuid")
            }

            holder.cardRecommend.setOnClickListener { // 좋아요
                val count = holder.recommend.text

                if (holder.imageRecommend.tag == "true") {
                    holder.imageRecommend.setImageResource(R.drawable.ic_favorite_border_24)
                    mRecommend.value = item.optString("uuid") + "false"
                    holder.imageRecommend.tag = "false"
                    holder.recommend.text = (count.toString().toInt() - 1).toString()

                } else if (holder.imageRecommend.tag == "false") {
                    holder.imageRecommend.setImageResource(R.drawable.ic_favorite_24)
                    mRecommend.value = item.optString("uuid") + "true"
                    holder.imageRecommend.tag = "true"
                    holder.recommend.text = (count.toString().toInt() + 1).toString()
                }
            }

            if (item.optString("user_image") != "null") {
                Glide.with(mContext)
                        .load("$userImageUri${item.optString("user_image")}.jpg")
                        .into(holder.profile)
            }

            if (item.optString("image1") != "") {
                holder.layout.visibility = View.VISIBLE
                holder.image1.visibility = View.VISIBLE

                Glide.with(mContext)
                        .load("$boardUri${item.optString("type")}/${item.optString("uuid")}/${item.optString("image1")}.jpg")
                        .into(holder.image1)
            }

            if (item.optString("image2") != "") {
                holder.image2.visibility = View.VISIBLE

                Glide.with(mContext)
                        .load("$boardUri${item.optString("type")}/${item.optString("uuid")}/${item.optString("image2")}.jpg")
                        .into(holder.image2)
            }

            if (item.optString("image3") != "") {
                holder.image3.visibility = View.VISIBLE

                Glide.with(mContext)
                        .load("$boardUri${item.optString("type")}/${item.optString("uuid")}/${item.optString("image3")}.jpg")
                        .into(holder.image3)
            }

            if (item.optString("image4") != "") {
                holder.image4.visibility = View.VISIBLE

                Glide.with(mContext)
                        .load("$boardUri${item.optString("type")}/${item.optString("uuid")}/${item.optString("image4")}.jpg")
                        .into(holder.image4)
            }

            if (item.optString("image5") != "") {
                holder.image5.visibility = View.VISIBLE

                Glide.with(mContext)
                        .load("$boardUri${item.optString("type")}/${item.optString("uuid")}/${item.optString("image5")}.jpg")
                        .into(holder.image5)
            }

            // 좋아요 리스트 불러오기
            mArray.observe(mOwner, {
                holder.imageRecommend.setImageResource(R.drawable.ic_favorite_border_24)
                holder.imageRecommend.tag = "false"

                for (i: Int in 0 until it.length()) {
                    if (item.optString("uuid") == it.getJSONObject(i).optString("uuid")) {
                        holder.imageRecommend.setImageResource(R.drawable.ic_favorite_24)
                        holder.imageRecommend.tag = "true"
                    }
                }
            })
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.text_boardItem_id) // 닉네임
        val time: TextView = view.findViewById(R.id.text_boardItem_time) // 시간
        val content: TextView = view.findViewById(R.id.text_boardItem_content) // 미리보기 내용
        val content2: TextView = view.findViewById(R.id.text_boardItem_content2) // 확장 내용
        val recommend: TextView = view.findViewById(R.id.text_boardItem_recommend) // 좋아요 개수
        val comment: TextView = view.findViewById(R.id.text_boardItem_comment) // 댓글 개수
        val card: CardView = view.findViewById(R.id.card_item_board) // 카드뷰
        val profile: ImageView = view.findViewById(R.id.image_item_board_profile) // 프로필 이미지
        val cardComment: CardView = view.findViewById(R.id.card_board_comment) // 댓글 카드뷰
        val cardRecommend: CardView = view.findViewById(R.id.card_board_recommend) // 좋아요 카드뷰
        val imageRecommend: ImageView = view.findViewById(R.id.image_item_board_recommend) // 좋아요 이미지

        val image1: ImageView = view.findViewById(R.id.image_board_im1)
        val image2: ImageView = view.findViewById(R.id.image_board_im2)
        val image3: ImageView = view.findViewById(R.id.image_board_im3)
        val image4: ImageView = view.findViewById(R.id.image_board_im4)
        val image5: ImageView = view.findViewById(R.id.image_board_im5)

        val layout: HorizontalScrollView = view.findViewById(R.id.scroll_board_image)
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