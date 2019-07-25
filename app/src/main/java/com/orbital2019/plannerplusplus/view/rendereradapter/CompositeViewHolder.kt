package com.orbital2019.plannerplusplus.view.rendereradapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class CompositeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * RecyclerView containing child items
     */
    var mRecyclerView: RecyclerView? = null
}