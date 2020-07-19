package com.kunalapk.smartrecyclerview.listener

import android.view.View
import com.kunalapk.smartrecyclerview.viewholder.CustomViewHolder

interface ViewAttachListener<T> {
    fun onViewAttachedToWindow(holder: CustomViewHolder<T>,itemView:View,adapterPosition:Int)
    fun onViewDetachedFromWindow(holder: CustomViewHolder<T>,itemView:View,adapterPosition: Int)
}