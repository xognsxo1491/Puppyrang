package com.portfolio.puppy.ui.board

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.portfolio.puppy.R

class BoardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var image = itemView.findViewById<ImageView>(R.id.image_item_writeBoard)

    fun setItem(uri: Uri) {
        image.setImageURI(uri)
    }
}