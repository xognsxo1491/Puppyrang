package com.portfolio.puppy.ui.board.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.portfolio.puppy.R
import com.portfolio.puppy.util.PreferencesUtil
import com.portfolio.puppy.util.TimeFormatUtil
import org.json.JSONObject

// 댓글 어댑터
class BoardCommentAdapter(list: ArrayList<JSONObject>): RecyclerView.Adapter<BoardCommentAdapter.ViewHolder>() {
    private var mData: ArrayList<JSONObject> = list
    private lateinit var mContext: Context
    lateinit var mUUID: MutableLiveData<String>
    private lateinit var mCard: CardView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_comment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        mCard = holder.card

        holder.name.text = item.optString("USER_NAME")
        holder.content.text = item.optString("CONTENT")
        holder.time.text = TimeFormatUtil().formatting(item.optLong("TIME"))

        holder.card.setOnClickListener {
            if (PreferencesUtil(mContext).getEmail() == item.optString("USER_EMAIL")) {
                val items = arrayOf<CharSequence>("댓글 삭제")

                val builder = AlertDialog.Builder(mContext)
                builder.setItems(items) { _, which ->
                    mUUID.value = item.optString("UUID_COMMENT")
                }.create().show()
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_item_comment_name)
        val content: TextView = view.findViewById(R.id.text_item_comment_content)
        val time: TextView = view.findViewById(R.id.text_item_comment_time)
        val card: CardView = view.findViewById(R.id.card_item_comment)
    }

    fun setInVisible() {
       mCard.visibility = View.GONE
    }
}