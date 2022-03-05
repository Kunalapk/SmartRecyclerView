package com.kunalapk.demo

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.kunalapk.smartrecyclerview.extensions.loadImage

object BindingAdapter {

    @BindingAdapter("setIcon")
    @JvmStatic
    fun setIcon(imageView: AppCompatImageView,icon:String?){
        imageView.loadImage(icon)
    }

}