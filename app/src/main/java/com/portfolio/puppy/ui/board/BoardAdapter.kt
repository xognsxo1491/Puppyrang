package com.portfolio.puppy.ui.board

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.portfolio.puppy.R
import kotlin.collections.ArrayList

class BoardAdapter internal constructor(list: ArrayList<Uri>) : RecyclerView.Adapter<BoardAdapter.ViewHolder>() {
    private var mData: ArrayList<Uri> = list
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_write_board, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.image.setImageURI(item)

        val items = arrayOf<CharSequence>("이미지 변경", "이미지 삭제")

        holder.image.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setItems(items) { _, which ->
                when (which) {
                    0 -> { // 이미지 변경
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    }

                    1 -> { // 이미지 삭제
                        holder.image.setImageDrawable(null)
                        mData.removeAt(position)
                    }
                }
            }.create().show()
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_item_writeBoard)
    }
}